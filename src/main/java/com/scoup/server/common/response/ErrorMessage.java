package com.scoup.server.common.response;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorMessage {
    /**
     * auth
     */


    /**
     * user
     */
    EXPIRED_TOKEN(UNAUTHORIZED, "만료된 토큰입니다."),
    NOT_FOUND_USER_EXCEPTION(NOT_FOUND, "유저를 찾을 수 없습니다."),
    NOT_FOUND_USER_PASSWORD_EXCEPTION(NOT_FOUND, "잘못된 비밀번호입니다."),
    CONFLICT_USER_PASSWORD_EXCEPTION(CONFLICT, "존재하는 유저 비밀번호 입니다."),
    CONFLICT_USER_NICKNAME_EXCEPTION(CONFLICT, "존재하는 유저 닉네임 입니다."),
    NOT_ADMIN_EXCEPTION(NOT_FOUND, "어드민 유저가 아닙니다."),

    /**
     * cafe
     */
    NOT_FOUND_CAFE_EXCEPTION(NOT_FOUND, "카페를 찾을 수 없습니다."),
    NOT_FOUND_MENU_EXCEPTION(NOT_FOUND, "메뉴를 찾을 수 없습니다."),
    REDUPLICATION_CAFE_EXCEPTION(NOT_FOUND, "같은 가게를 추가할 수 없습니다."),

    /**
     * coupon
     */
    NOT_FOUND_COUPON_EXCEPTION(NOT_FOUND, "쿠폰을 찾을 수 없습니다."),
    USED_COUPON_EXCEPTION(NOT_FOUND, "사용한 쿠폰입니다."),

    /**
     * event
     */
    NOT_FOUND_EVENT_EXCEPTION(NOT_FOUND, "이벤트를 찾을 수 없습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
