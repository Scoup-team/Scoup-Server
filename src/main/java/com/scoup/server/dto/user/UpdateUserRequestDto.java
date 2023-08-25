package com.scoup.server.dto.user;

import lombok.Getter;

@Getter
public class UpdateUserRequestDto {
    private String nickname;
    private String password;
}
