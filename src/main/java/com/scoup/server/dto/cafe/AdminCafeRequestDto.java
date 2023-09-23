package com.scoup.server.dto.cafe;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminCafeRequestDto {
    private String shopName;
    private String phoneNumber;
    private String licenseeNumber;
    private String shopAddress;
    private String runningTime;
    private String shopImageUrl;
}
