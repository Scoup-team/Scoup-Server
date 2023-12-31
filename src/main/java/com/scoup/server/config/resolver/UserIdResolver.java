package com.scoup.server.config.resolver;

import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.UnauthorizedException;
import com.scoup.server.service.JwtService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


@Component
@RequiredArgsConstructor
public class UserIdResolver implements HandlerMethodArgumentResolver {

    private final JwtService jwtService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasToken = parameter.hasParameterAnnotation(UserId.class);
        boolean isLongType = Long.class.equals(parameter.getParameterType());
        return hasToken && isLongType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final String token = request.getHeader("Authorization");

        // 토큰 검증
        if (!jwtService.verifyToken(token)) {
            throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED_TOKEN);
        }

        // 유저 아이디 반환
        final String tokenContents = jwtService.getJwtContents(token);
        try {
            return Long.parseLong(tokenContents);
        } catch (NumberFormatException e) {
            throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED_TOKEN);
        }
    }
}
