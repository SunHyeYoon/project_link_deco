package com.linkdeco.link_deco.dto;

import com.linkdeco.link_deco.domain.Member;
import com.linkdeco.link_deco.global.security.JwtToken;
import lombok.Builder;

@Builder
public record LoginResponseDto(
        String grantType,
        String accessToken,
        String refreshToken,
        String nickname
        ) {

    public static LoginResponseDto of(JwtToken jwtToken, Member member) {
        return new LoginResponseDto(jwtToken.getGrantType(),
                jwtToken.getAccessToken(),
                jwtToken.getRefreshToken(),
                member.getNickname());
    }
}
