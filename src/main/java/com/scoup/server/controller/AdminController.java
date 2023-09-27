package com.scoup.server.controller;

import com.scoup.server.common.response.ApiResponse;
import com.scoup.server.common.response.SuccessMessage;
import com.scoup.server.config.resolver.UserId;
import com.scoup.server.dto.Event.UpdateEventRequestDto;
import com.scoup.server.dto.Event.EventResponseDto;
import com.scoup.server.dto.admin.PatchAdminCafeRequestDto;
import com.scoup.server.dto.auth.SignupRequestDTO;
import com.scoup.server.dto.auth.SignupResponseDTO;
import com.scoup.server.dto.cafe.AdminCafeReponseDto;
import com.scoup.server.dto.cafe.AdminCafeRequestDto;
import com.scoup.server.service.AuthService;
import com.scoup.server.service.CafeService;

import com.scoup.server.service.EventService;
import com.scoup.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final CafeService cafeService;
    private final EventService eventService;
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/mypage/event")
    public ApiResponse addEvent(
            @RequestHeader Long cafeId,
            @RequestBody String content,
            @UserId Long userId
    ) {

        cafeService.addEvent(cafeId, content, userId);

        return ApiResponse.success(SuccessMessage.HOME_CHECK_SUCCESS);

    }

    @GetMapping("/mypage/event")
    public ApiResponse<List<EventResponseDto>> getEvent(
            @RequestBody Long cafeId
    ){

        List<EventResponseDto> dto=cafeService.getEvent(cafeId);

        return ApiResponse.success(SuccessMessage.EVENT_CHECK_SUCCESS, dto);
    }

    @PatchMapping("/mypage/event/{eventId}")
    public ApiResponse patchEvent(
            @PathVariable Long eventId,
            @RequestBody UpdateEventRequestDto requestDto,
            @UserId Long userId
            ){
        eventService.patchEvent(eventId, requestDto, userId);

        return ApiResponse.success(SuccessMessage.EVENT_PATCH_SUCCESS);
    }

    @DeleteMapping("/mypage/event/{eventId}")
    public ApiResponse deleteEvent(
            @PathVariable Long eventId,
            @UserId Long userId
    ){
        eventService.deleteEvent(eventId, userId);

        return ApiResponse.success(SuccessMessage.EVENT_DELETE_SUCCESS);
    }

    @GetMapping("/mypage/shop")
    public ApiResponse<List<AdminCafeReponseDto>> getAdminShop(
        @UserId Long userId
    ){
        List<AdminCafeReponseDto> cafeList=userService.getAdminCafe(userId);

        return ApiResponse.success(SuccessMessage.ADMIN_CHECK_SUCCESS, cafeList);
    }

    @PostMapping("/auth/signup")
    public ApiResponse<SignupResponseDTO> adminSignup(
        @RequestBody SignupRequestDTO requestDTO
    ){
        SignupResponseDTO data = authService.adminSignupService(requestDTO);
        return ApiResponse.success(SuccessMessage.SIGNUP_SUCCESS, data);
    }

    @PatchMapping("/mypage/shop/{shopId}")
    public ApiResponse patchAdminCafe(
        @RequestBody PatchAdminCafeRequestDto requestDto,
        @PathVariable Long shopId,
        @UserId Long userId
    ) {
        cafeService.patchCafe(requestDto, shopId, userId);
        return ApiResponse.success(SuccessMessage.PATCH_CAFE_SUCCESS);
    }

    @DeleteMapping("/mypage/shop/{shopId}")
    public ApiResponse deleteAdminCafe(
        @UserId Long userId,
        @PathVariable Long shopId
    ) {
        cafeService.deleteCafe(shopId, userId);
        return ApiResponse.success(SuccessMessage.DELETE_CAFE_SUCCESS);
    }

    @PostMapping("/mypage/shop")
    public ApiResponse addAdminShop(
            @UserId Long userId,
            @RequestBody AdminCafeRequestDto adminCafeRequestDto
    ){
        cafeService.addAdminCafe(userId, adminCafeRequestDto);

        return ApiResponse.success(SuccessMessage.ADD_CAFE_SUCCESS);
    }
}
