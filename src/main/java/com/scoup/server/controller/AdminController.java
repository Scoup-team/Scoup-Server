package com.scoup.server.controller;

import com.scoup.server.common.response.ApiResponse;
import com.scoup.server.common.response.SuccessMessage;
import com.scoup.server.dto.Event.UpdateEventRequestDto;
import com.scoup.server.dto.Event.EventResponseDto;
import com.scoup.server.service.CafeService;

import com.scoup.server.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final CafeService cafeService;
    private final EventService eventService;
    @PostMapping("/mypage/event")
    public ApiResponse addEvent(
            @RequestHeader Long cafeId,
            @RequestBody String content
    ) {

        cafeService.addEvent(cafeId, content);

        return ApiResponse.success(SuccessMessage.HOME_CHECK_SUCCESS);

    }

    @GetMapping("/mypage/event")
    public ApiResponse<List<EventResponseDto>> getEvent(
            @RequestHeader Long cafeId
    ){

        List<EventResponseDto> dto=cafeService.getEvent(cafeId);

        return ApiResponse.success(SuccessMessage.EVENT_CHECK_SUCCESS, dto);
    }

    @PatchMapping("/mypage/event/{eventId}")
    public ApiResponse patchEvent(
            @PathVariable Long eventId,
            @RequestBody UpdateEventRequestDto requestDto
            ){
        eventService.patchEvent(eventId, requestDto);

        return ApiResponse.success(SuccessMessage.EVENT_CHECK_SUCCESS);
    }

    @DeleteMapping("/mypage/event/{eventId}")
    public ApiResponse deleteEvent(
            @PathVariable Long eventId
    ){
        eventService.deleteEvent(eventId);

        return ApiResponse.success(SuccessMessage.EVENT_CHECK_SUCCESS);
    }
}
