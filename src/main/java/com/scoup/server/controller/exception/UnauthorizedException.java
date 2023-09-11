package com.scoup.server.controller.exception;

import com.scoup.server.common.exception.BaseException;
import com.scoup.server.common.response.ErrorMessage;
import lombok.Getter;

@Getter
public class UnauthorizedException extends BaseException {
    public UnauthorizedException(ErrorMessage error) {
        super(error, "[UnauthorizedException] "+ error.getMessage());
    }

}
