package com.scoup.server.dto.user;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDateResponseDto {
    private Long id;
    private String nickname;
}
