package com.scoup.server.controller.exception;

import com.scoup.server.common.exception.BaseException;
import com.scoup.server.common.response.ErrorMessage;
import lombok.Getter;

@Getter
public class NotFoundDataException extends BaseException {
    public NotFoundDataException(ErrorMessage error) {
        super(error, "[NotFoundDataException] "+ error.getMessage());
    }

}
