package com.scoup.server.controller.exception;

import com.scoup.server.common.exception.BaseException;
import com.scoup.server.common.response.ErrorMessage;
import lombok.Getter;

@Getter
public class UserForbiddenException extends BaseException {
    public UserForbiddenException(ErrorMessage error) {
        super(error, "[UserForbiddenException] "+ error.getMessage());
    }

}
