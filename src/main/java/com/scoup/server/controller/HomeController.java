package com.scoup.server.controller;


import com.scoup.server.common.response.ApiResponse;
import com.scoup.server.common.response.SuccessMessage;
import com.scoup.server.dto.Event.EventResponseDto;
import com.scoup.server.dto.cafe.SearchCafeRequestDto;
import com.scoup.server.dto.coupon.CouponResponseDto;
import com.scoup.server.dto.coupon.UpdateCouponRequestDto;
import com.scoup.server.dto.mainPage.MainPageResponseDto;
import com.scoup.server.dto.mainPage.UpdateMainPageRequestDto;
import com.scoup.server.dto.menu.MenuResponseDto;
import com.scoup.server.service.CafeService;
import com.scoup.server.service.CouponService;
import com.scoup.server.service.MenuService;
import com.scoup.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class HomeController {

    private final CafeService cafeService;
    private final MenuService menuService;
    private final CouponService couponService;
    private final UserService userService;



    @GetMapping("/home")
    public ApiResponse<MainPageResponseDto> mainPageCheck(
            @RequestHeader Long userId
    ) {
        MainPageResponseDto responseDto=userService.findCafe(userId);

        return ApiResponse.success(SuccessMessage.HOME_CHECK_SUCCESS, responseDto);
    }



    @PatchMapping("/home")
    public ApiResponse patchMainPage(
            @RequestHeader Long userId,
            @RequestBody UpdateMainPageRequestDto updateMainPageRequestDto
    ) {
        cafeService.patchCafe(userId, updateMainPageRequestDto);

        return ApiResponse.success(SuccessMessage.HOME_CHECK_SUCCESS);
    }


    @PostMapping("/shop/{shopId}")
    public ApiResponse mainPageAdd(
            @PathVariable("shopId") Long shopId,
            @RequestHeader Long userId,
            @RequestBody SearchCafeRequestDto requestDto
    ) {

        cafeService.addCafe(userId, requestDto);

        return ApiResponse.success(SuccessMessage.ADD_CAFE_SUCCESS);
    }



    @GetMapping("/home/{shopId}/{stampId}")
    public ApiResponse<MenuResponseDto> menuCheck(
            @PathVariable("shopId") Long shopId,
            @PathVariable("stampId") Long orderId
    ) {

        MenuResponseDto menuDto=menuService.getMenuList(shopId, orderId);

        return ApiResponse.success(SuccessMessage.MENU_CHECK_SUCCESS, menuDto);
    }

    @GetMapping("/home/{shopId}/event")
    public ApiResponse<EventResponseDto> eventCheck(
            @PathVariable("shopId") Long shopId
    ) {
        EventResponseDto eventList=cafeService.getEvent(shopId);

        return ApiResponse.success(SuccessMessage.SEARCH_CAFE_SUCCESS, eventList);

    }

    @GetMapping("/mypage/coupon")
    public ApiResponse<CouponResponseDto> couponCheck(
            @RequestHeader Long userId
    ) {
        CouponResponseDto couponList=couponService.getCoupon(userId);

        return ApiResponse.success(SuccessMessage.COUPON_CHECK_SUCCESS, couponList);
    }

    @PostMapping("/mypage/coupon/{couponId}")
    public ApiResponse couponUse(
            @PathVariable("couponId") Long couponId,
            @RequestBody UpdateCouponRequestDto requestDto
    ) {
        couponService.patchCoupon(couponId, requestDto);

        return ApiResponse.success(SuccessMessage.COUPON_CHECK_SUCCESS);
    }
}