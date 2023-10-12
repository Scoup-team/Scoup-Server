package com.scoup.server.dto.cafe;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;

@Getter
@Builder
public class AdminCafeReponseDto {
    private Long shopId;
    private String shopName;
    private String shopBranch;
    private String shopAddress;
    private String shopImageUrl;
    private String runningTime;
    private String phoneNumber;
    private String licenseeNumber;
}