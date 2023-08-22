package com.scoup.server.dto.cafe;


import com.scoup.server.domain.Cafe;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchCafeResponseDto {
    private Long shopId;
    private String shopName;
    private String shopBranch;
    private String shopAddress;
    private String shopImageUrl;

    public static SearchCafeResponseDto of(Cafe cafe) {
        return SearchCafeResponseDto.builder()
            .shopId(cafe.getId())
            .shopName(cafe.getName())
            .shopAddress(cafe.getLocation())
            .shopImageUrl(cafe.getImageUrl())
            .shopBranch(cafe.getBranch())
            .build();
    }
}
