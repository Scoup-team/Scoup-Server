package com.scoup.server.controller;

import com.scoup.server.common.response.ApiResponse;
import com.scoup.server.common.response.SuccessMessage;
import com.scoup.server.config.resolver.UserId;
import com.scoup.server.dto.Event.EventResponseDto;
import com.scoup.server.dto.coupon.CouponResponseDto;
import com.scoup.server.dto.mainPage.MainPageCafeResponseDto;
import com.scoup.server.dto.menu.MenuResponseDto;
import com.scoup.server.dto.menu.ReceiptRequestDto;
import com.scoup.server.dto.menu.ReceiptResponseDto;
import com.scoup.server.service.CafeService;
import com.scoup.server.service.CouponService;
import com.scoup.server.service.MenuService;
import com.scoup.server.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class HomeController {

    private final CafeService cafeService;
    private final MenuService menuService;
    private final CouponService couponService;
    private final UserService userService;


    @GetMapping("/home")
    public ApiResponse<List<MainPageCafeResponseDto>> getMainPage(
        @UserId Long userId
    ) {
        List<MainPageCafeResponseDto> responseDto = userService.getCafe(userId);

        return ApiResponse.success(SuccessMessage.HOME_CHECK_SUCCESS, responseDto);
    }


    @DeleteMapping("/home")
    public ApiResponse deleteMainPage(
        @UserId Long userId,
        @RequestBody Long cafeId
    ) {
        cafeService.deleteMainPageCafe(userId, cafeId);

        return ApiResponse.success(SuccessMessage.HOME_PATCH_SUCCESS);
    }


    @PostMapping("/shop/{shopId}")
    public ApiResponse mainPageAdd(
        @PathVariable("shopId") Long shopId,
        @UserId Long userId
    ) {

        cafeService.addCafe(userId, shopId);

        return ApiResponse.success(SuccessMessage.ADD_CAFE_SUCCESS);
    }


    @GetMapping("/home/{stampId}")
    public ApiResponse<MenuResponseDto> getMenu(
        @PathVariable("stampId") Long stampId,
        @UserId Long userId
    ) {

        MenuResponseDto menuDto = menuService.getMenuList(stampId);

        return ApiResponse.success(SuccessMessage.MENU_CHECK_SUCCESS, menuDto);
    }

    @GetMapping("/home/{shopId}/event")
    public ApiResponse<List<EventResponseDto>> getEvent(
        @PathVariable("shopId") Long shopId,
        @UserId Long userId
    ) {
        List<EventResponseDto> eventList = cafeService.getEvent(shopId);

        return ApiResponse.success(SuccessMessage.EVENT_CHECK_SUCCESS, eventList);

    }

    @GetMapping("/mypage/coupon")
    public ApiResponse<List<CouponResponseDto>> getCoupon(
        @UserId Long userId
    ) {
        List<CouponResponseDto> couponList = couponService.getCoupon(userId);

        return ApiResponse.success(SuccessMessage.COUPON_CHECK_SUCCESS, couponList);
    }

    @PostMapping("/mypage/coupon/{couponId}")
    public ApiResponse useCoupon(
        @PathVariable("couponId") Long couponId,
        @UserId Long userId
    ) {
        couponService.patchCoupon(couponId);

        return ApiResponse.success(SuccessMessage.COUPON_CHECK_SUCCESS);
    }

    @PostMapping("home/receipt")
    public ApiResponse<ReceiptResponseDto> postReceipt(
        @UserId Long userId,
        @RequestBody ReceiptRequestDto requestDto
    ){
        ReceiptResponseDto data = cafeService.postReceipt(requestDto, userId);

        return ApiResponse.success(SuccessMessage.CREATE_RECEIPT_SUCCESS, data);
    }
}