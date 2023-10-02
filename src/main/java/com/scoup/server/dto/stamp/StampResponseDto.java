package com.scoup.server.dto.stamp;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class StampResponseDto {
    private Long stampId;
    private List<String> menu;

}