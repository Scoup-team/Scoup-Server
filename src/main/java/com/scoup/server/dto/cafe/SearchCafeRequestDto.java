package com.scoup.server.dto.cafe;

import lombok.Getter;

@Getter
public class SearchCafeRequestDto {
    private Long shopId;
    private String shopName;
    private String shopBranch;
    private String shopAddress;
    private String shopImageUrl;
}
