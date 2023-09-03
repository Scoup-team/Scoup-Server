package com.scoup.server.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.scoup.server.common.response.ApiResponse;
import com.scoup.server.controller.exception.BadRequestException;
import com.scoup.server.controller.exception.NotFoundDataException;
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



    /**
     * 404 NOT FOUND
     */
    @ExceptionHandler({
        NotFoundDataException.class
    })
    public ResponseEntity<ApiResponse> NotFoundException(BaseException exception) {
        return ResponseEntity.status(NOT_FOUND).body(ApiResponse.error(exception.getError(), exception.getMessage()));
    }



    /**
     * 500 INTERNAL SERVER ERROR
     */

}
