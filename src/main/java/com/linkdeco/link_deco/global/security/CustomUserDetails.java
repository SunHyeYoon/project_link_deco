package com.linkdeco.link_deco.global.security;

import com.linkdeco.link_deco.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public record CustomUserDetails(Member member) implements UserDetails {

    // 1. 권한 목록 (일단 비워둠, 나중에 ROLE_USER 등 추가 가능)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }
}
