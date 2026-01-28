package com.linkdeco.link_deco.global.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class JwtToken {
    private String grantType;     // "Bearer" (JWT 표준 인증 방식)
    private String accessToken;
    private String refreshToken;
}
