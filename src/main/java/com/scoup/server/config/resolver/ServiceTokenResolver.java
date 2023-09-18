package com.scoup.server.config.resolver;

import com.scoup.server.common.response.ErrorMessage;
import com.scoup.server.controller.exception.UnauthorizedException;
import com.scoup.server.dto.auth.TokenServiceVO;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class ServiceTokenResolver implements HandlerMethodArgumentResolver {
    public static final String BEARER = "Bearer ";
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasServiceToken = parameter.hasParameterAnnotation(ServiceToken.class);
        boolean isServiceTokenVOType =
            TokenServiceVO.class.isAssignableFrom(parameter.getParameterType());
        return hasServiceToken && isServiceTokenVOType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String accessTokenHeader = webRequest.getHeader("accessToken");
        String refreshTokenHeader = webRequest.getHeader("refreshToken");

        if (accessTokenHeader == null || refreshTokenHeader == null
            || !refreshTokenHeader.startsWith(BEARER)
            || !accessTokenHeader.startsWith(BEARER)) {
            throw new UnauthorizedException(ErrorMessage.AUTHENTICATION_BEARER_EXCEPTION);
        }

        String accessToken = accessTokenHeader.substring(BEARER.length());
        String refreshToken = refreshTokenHeader.substring(BEARER.length());
        return TokenServiceVO.of(accessToken, refreshToken);
    }
}
