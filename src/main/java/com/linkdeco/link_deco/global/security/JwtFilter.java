package com.linkdeco.link_deco.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 요청 헤더에서 JWT 토큰 추출
        String token = resolveToken(request);
        //log.info("추출된 토큰: {}", token);

        // 2. 토큰이 있고 유효하다면 Security Context에 인증 정보 저장
        if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
            Authentication authentication = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 '{}' 인증 정보 저장", authentication.getName());
        } else {
            log.debug("유효한 JWT 토큰이 없습니다.");
        }

        // 3. 다음 필터로 넘기기
        filterChain.doFilter(request, response);
    }

    // HTTP 헤더에서 "Authorization: Bearer <token>" 형태의 토큰을 꺼내는 메서드
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private Authentication getAuthentication(String token) {
        Long memberId = jwtProvider.getMemberId(token);

        return new UsernamePasswordAuthenticationToken(
                memberId,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
