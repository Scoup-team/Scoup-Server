package com.scoup.server.dto.mainPage;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MainPageCafeResponseDto {
    private Long shopId;
    private String name;
    private String cafeImageUrl;
    private int stamp;
    private List<String> menu;
    private List<String> menuImageUrl;
    //베스트 메뉴 사진과 이름 같이 넘겨줘야 함
}
