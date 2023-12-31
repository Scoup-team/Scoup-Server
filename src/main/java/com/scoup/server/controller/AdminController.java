package com.scoup.server.controller;

import com.scoup.server.common.response.ApiResponse;
import com.scoup.server.common.response.SuccessMessage;
import com.scoup.server.config.resolver.UserId;
import com.scoup.server.dto.Event.AddEventRequestDto;
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
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
            @UserId Long userId,
            @RequestBody AddEventRequestDto requestDto
    ) {

        cafeService .addEvent(requestDto, userId);

        return ApiResponse.success(SuccessMessage.EVENT_ADD_SUCCESS);

    }

    @GetMapping("/mypage/event")
    public ApiResponse<List<EventResponseDto>> getEvent(
            @UserId Long userId
    ){

        List<EventResponseDto> dto=cafeService.getAdminEvent(userId);

        return ApiResponse.success(SuccessMessage.EVENT_CHECK_SUCCESS, dto);
    }

    @PatchMapping("/mypage/event/{eventId}")
    public ApiResponse patchEvent(
            @UserId Long userId,
            @PathVariable Long eventId,
            @RequestBody UpdateEventRequestDto requestDto
            ){
        eventService.patchEvent(eventId, requestDto, userId);

        return ApiResponse.success(SuccessMessage.EVENT_PATCH_SUCCESS);
    }

    @DeleteMapping("/mypage/event")
    public ApiResponse deleteEvent(
            @UserId Long userId,
            @RequestBody Long eventId
    ){
        eventService.deleteEvent(eventId, userId);

        return ApiResponse.success(SuccessMessage.EVENT_DELETE_SUCCESS);
    }

    @GetMapping("/mypage/shop")
    public ApiResponse<AdminCafeReponseDto> getAdminShop(
        @UserId Long userId
    ){
        AdminCafeReponseDto cafe=userService.getAdminCafe(userId);

        return ApiResponse.success(SuccessMessage.ADMIN_CHECK_SUCCESS, cafe);
    }

    @PostMapping("/auth/signup")
    public ApiResponse<SignupResponseDTO> adminSignup(
        @RequestBody SignupRequestDTO requestDTO
    ){
        SignupResponseDTO data = authService.adminSignupService(requestDTO);
        return ApiResponse.success(SuccessMessage.SIGNUP_SUCCESS, data);
    }

    @PatchMapping("/mypage/shop")
    public ApiResponse patchAdminCafe(
        @UserId Long userId,
        @RequestBody PatchAdminCafeRequestDto requestDto
    ) {
        cafeService.patchCafe(requestDto, userId);
        return ApiResponse.success(SuccessMessage.PATCH_CAFE_SUCCESS);
    }

    @DeleteMapping("/mypage/shop")
    public ApiResponse deleteAdminCafe(
        @UserId Long userId
    ) {
        cafeService.deleteCafe(userId);

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
