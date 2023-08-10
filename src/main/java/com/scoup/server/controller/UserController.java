package com.scoup.server.controller;

import com.scoup.server.common.response.ApiResponse;
import com.scoup.server.common.response.SuccessMessage;
import com.scoup.server.dto.user.UpdateUserRequestDto;
import com.scoup.server.dto.user.UserDateResponseDto;
import com.scoup.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<UserDateResponseDto> getUser(
        @RequestHeader Long userId
    ) {
        UserDateResponseDto data = userService.getUser(userId);
        return ApiResponse.success(SuccessMessage.GET_USER_DATA, data);
    }

    @DeleteMapping
    public ApiResponse deleteUser(
        @RequestHeader Long userId
    ) {
        userService.deleteUser(userId);
        return ApiResponse.success(SuccessMessage.DELETE_USER_SUCCESS);
    }

    @PatchMapping
    public ApiResponse patchUser(
        @RequestHeader Long userId,
        @RequestBody UpdateUserRequestDto requestDto
    ) {
        userService.patchUser(userId, requestDto);
        return ApiResponse.success(SuccessMessage.PATCH_USER_SUCCESS);
    }

}
