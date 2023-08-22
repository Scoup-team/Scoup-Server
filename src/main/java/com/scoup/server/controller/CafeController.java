package com.scoup.server.controller;

import com.scoup.server.common.response.ApiResponse;
import com.scoup.server.common.response.SuccessMessage;
import com.scoup.server.dto.cafe.SearchCafeResponseDto;
import com.scoup.server.service.CafeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class CafeController {

    private final CafeService cafeService;

    @GetMapping
    public ApiResponse<List<SearchCafeResponseDto>> searchCafe(
        @RequestHeader Long userId,
        @RequestParam String keyword
    ) {
        List<SearchCafeResponseDto> data = cafeService.searchCafe(userId, keyword);
        return ApiResponse.success(SuccessMessage.SEARCH_CAFE_SUCCESS, data);

    }
}
