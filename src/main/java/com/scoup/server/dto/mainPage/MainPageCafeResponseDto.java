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

    @Builder.Default
    private List<StampResponseDto> stamp=new ArrayList<>();
    @Builder.Default
    private List<String> menu=new ArrayList<>();
    @Builder.Default
    private List<String> menuImageUrl=new ArrayList<>();
    //베스트 메뉴 사진과 이름 같이 넘겨줘야 함
}
