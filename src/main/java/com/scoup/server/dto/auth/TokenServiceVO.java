package com.scoup.server.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenServiceVO {
    String accessToken;
    String refreshToken;

    public static TokenServiceVO of(String accessToken, String refreshToken){
        return TokenServiceVO.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
