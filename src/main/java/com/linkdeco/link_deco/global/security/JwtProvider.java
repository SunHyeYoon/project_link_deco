package com.linkdeco.link_deco.global.security;

import com.linkdeco.link_deco.global.exception.CustomException;
import com.linkdeco.link_deco.global.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public JwtToken generateJwtToken(Long memberId, String email) {
        String accessToken = generateToken(memberId, email, accessTokenExpiration);
        String refreshToken = generateToken(memberId, email, refreshTokenExpiration);

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String generateToken(Long memberId, String email, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(String.valueOf(memberId))
                .claim("email", email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getMemberId(String token) {
        try {
            return Long.parseLong(getClaims(token).getSubject());
        } catch (Exception e) {
            log.error("유효하지 않은 토큰에서 MemberId 추출 시도");
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    public String getEmail(String token) {
        try {
            return getClaims(token).get("email", String.class);
        } catch (Exception e) {
            log.error("유효하지 않은 토큰에서 Email 추출 시도");
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error(ErrorCode.MALFORMED_TOKEN.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn(ErrorCode.EXPIRED_TOKEN.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error(ErrorCode.UNSUPPORTED_TOKEN.getMessage());
        } catch (IllegalArgumentException e) {
            log.error(ErrorCode.TOKEN_NOT_FOUND.getMessage());
        }
        return false;
    }
}
