package com.scoup.server.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SignupResponseDTO {
    String nickname;
    String accessToken;
    String refreshToken;
}
