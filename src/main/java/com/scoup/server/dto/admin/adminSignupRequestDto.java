package com.scoup.server.dto.admin;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class adminSignupRequestDto {
    String email;
    String name;
    String nickname;
    String password;
}
