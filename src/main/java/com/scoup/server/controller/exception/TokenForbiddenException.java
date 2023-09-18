package com.scoup.server.controller.exception;

import com.scoup.server.common.exception.BaseException;
import com.scoup.server.common.response.ErrorMessage;
import lombok.Getter;

@Getter
public class TokenForbiddenException extends BaseException {
    public TokenForbiddenException(ErrorMessage error) {
        super(error, "[TokenForbiddenException] "+ error.getMessage());
    }

}
