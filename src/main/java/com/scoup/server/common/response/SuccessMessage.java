package com.scoup.server.common.response;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessMessage {
    /**
     * auth
     */
    LOGIN_SUCCESS(CREATED, "로그인이 성공했습니다."),

    /**
     * user
     */
    GET_USER_DATA(OK, "유저 정보 조회에 성공했습니다."),
    DELETE_USER_SUCCESS(OK, "유저 삭제에 성공했습니다."),

    /**
     * cafe
     */
    CREATE_VIDEO_SUCCESS(CREATED, "카페 생성에 성공했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
