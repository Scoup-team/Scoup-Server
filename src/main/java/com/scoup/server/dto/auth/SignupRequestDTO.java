package com.scoup.server.dto.auth;

import lombok.Getter;

@Getter
public class SignupRequestDTO {

    String email;
    String name;
    String nickname;
    String password;
}
