package com.scoup.server.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenServiceVO {
    String accessToken;
    String refreshToken;

}
