package com.scoup.server.dto.mainPage;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MainPageCafeResponseDto {
    private Long id;
    private String name;
    private int stamp;
    private List<String> menu;

}
