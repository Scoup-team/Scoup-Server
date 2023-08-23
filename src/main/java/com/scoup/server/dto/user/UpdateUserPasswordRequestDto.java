package com.scoup.server.dto.user;

import lombok.Getter;

@Getter
public class UpdateUserPasswordRequestDto {
    private String originPassword;
    private String newPassword;
}
