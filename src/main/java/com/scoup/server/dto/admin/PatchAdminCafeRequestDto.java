package com.scoup.server.dto.admin;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PatchAdminCafeRequestDto {
    private String name;
    private String phoneNumber;
    private String location;
    private String licenseeNumber;
    private String runningTime;
}
