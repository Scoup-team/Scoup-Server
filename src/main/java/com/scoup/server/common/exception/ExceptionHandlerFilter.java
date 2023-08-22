package com.scoup.server.common.exception;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scoup.server.common.response.ApiResponse;
import com.scoup.server.common.response.ErrorMessage;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BaseException exception) {
            setErrorResponse(response, exception.getError());
        }
    }

    private void setErrorResponse(
            HttpServletResponse response,
            ErrorMessage errorCode
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(errorCode.getHttpStatusCode());
        response.setContentType(APPLICATION_JSON_VALUE);
        ApiResponse baseResponse = ApiResponse.error(errorCode);
        try {
            response.getWriter()
                    .write(objectMapper.configure(JsonWriteFeature.ESCAPE_NON_ASCII.mappedFeature(), true)
                            .writeValueAsString(baseResponse));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
