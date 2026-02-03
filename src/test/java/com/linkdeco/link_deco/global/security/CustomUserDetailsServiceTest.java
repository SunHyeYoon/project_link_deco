package com.linkdeco.link_deco.global.security;

import com.linkdeco.link_deco.domain.Member;
import com.linkdeco.link_deco.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("이메일로 회원을 조회하여 닉네임을 반환한다")
    void loadUserByUsername_Success() {
        // given
        String email = "test@test.com";
        Member member = Member.builder()
                .email(email)
                .password("password123!")
                .nickname("tester")
                .build();
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

        // when
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // then
        assertThat(userDetails.getUsername()).isEqualTo("tester");
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 회원 조회 시 예외가 발생한다")
    void loadUserByUsername_NotFound() {
        // given
        String email = "none@test.com";
        given(memberRepository.findByEmail(email)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("해당 이메일의 회원을 찾을 수 없습니다: " + email);
    }
}