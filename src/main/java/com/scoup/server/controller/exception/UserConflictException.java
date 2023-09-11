package com.scoup.server.controller.exception;

import com.scoup.server.common.exception.BaseException;
import com.scoup.server.common.response.ErrorMessage;
import lombok.Getter;

@Getter
public class UserConflictException extends BaseException {
    public UserConflictException(ErrorMessage error) {
        super(error, "[UserConflictException] "+ error.getMessage());
    }

}
