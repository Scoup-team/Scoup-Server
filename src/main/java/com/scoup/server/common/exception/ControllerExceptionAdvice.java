package com.scoup.server.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.scoup.server.common.response.ApiResponse;
import com.scoup.server.controller.exception.BadRequestException;
import com.scoup.server.controller.exception.NotFoundDataException;
import com.scoup.server.controller.exception.TokenForbiddenException;
import com.scoup.server.controller.exception.UnauthorizedException;
import com.scoup.server.controller.exception.UserConflictException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    /**
     * 400 BAD REQUEST
     */
    @ExceptionHandler({
            BadRequestException.class
    })
    public ResponseEntity<ApiResponse> BadRequestException(BaseException exception) {
        return ResponseEntity.status(BAD_REQUEST).body(ApiResponse.error(exception.getError(), exception.getMessage()));
    }


    /**
     * 401 UNAUTHORIZED
     */
    @ExceptionHandler({
        UnauthorizedException.class
    })
    public ResponseEntity<ApiResponse> UnauthorizedException(BaseException exception) {
        return ResponseEntity.status(UNAUTHORIZED)
            .body(ApiResponse.error(exception.getError(), exception.getMessage()));
    }

    /**
     * 403 FORBIDDEN
     */
    @ExceptionHandler({
        TokenForbiddenException.class
    })
    public ResponseEntity<ApiResponse> ForbiddenException(BaseException exception) {
        return ResponseEntity.status(FORBIDDEN)
            .body(ApiResponse.error(exception.getError(), exception.getMessage()));
    }




    /**
     * 404 NOT FOUND
     */
    @ExceptionHandler({
        NotFoundDataException.class,
    })
    public ResponseEntity<ApiResponse> NotFoundException(BaseException exception) {
        return ResponseEntity.status(NOT_FOUND).body(ApiResponse.error(exception.getError(), exception.getMessage()));
    }

    /**
     * 409 CONFLICT
     */
    @ExceptionHandler({
        UserConflictException.class
    })
    public ResponseEntity<ApiResponse> ConflictException(BaseException exception) {
        return ResponseEntity.status(CONFLICT).body(ApiResponse.error(exception.getError(), exception.getMessage()));
    }


    /**
     * 500 INTERNAL SERVER ERROR
     */

}
