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
    PATCH_USER_SUCCESS(OK, "유저 수정에 성공했습니다."),
    HOME_CHECK_SUCCESS(OK, "홈 조회 성공하였습니다."),
    HOME_PATCH_SUCCESS(OK, "홈 수정 성공하였습니다."),
    ADMIN_CHECK_SUCCESS(OK, "카페 조회 성공하였습니다."),

    /**
     * cafe
     */
    CREATE_VIDEO_SUCCESS(CREATED, "카페 생성에 성공했습니다."),
    SEARCH_CAFE_SUCCESS(OK, "카페 검색에 성공했습니다."),
    ADD_CAFE_SUCCESS(OK, "카페 추가에 성공했습니다."),

    /**
     * cafe
     */
    MENU_CHECK_SUCCESS(OK, "메뉴 조회 성공하였습니다."),

    /**
     * coupon
     */
    COUPON_CHECK_SUCCESS(OK, "쿠폰 조회 성공하였습니다."),

    /**
     * event
     */
    EVENT_CHECK_SUCCESS(OK, "이벤트 조회 성공하였습니다."),
    EVENT_PATCH_SUCCESS(OK, "이벤트 수정 성공하였습니다."),
    EVENT_DELETE_SUCCESS(OK, "이벤트 삭제 성공하였습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
