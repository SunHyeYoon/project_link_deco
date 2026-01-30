package com.linkdeco.link_deco.service;

import com.linkdeco.link_deco.domain.Member;
import com.linkdeco.link_deco.dto.MemberRequestDto;
import com.linkdeco.link_deco.global.exception.CustomException;
import com.linkdeco.link_deco.global.exception.ErrorCode;
import com.linkdeco.link_deco.global.validator.SignUpValidator;
import com.linkdeco.link_deco.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SignUpValidator signUpValidator;

    @Mock
    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() {
        // given
        String email = "test@example.com";
        String rawPassword = "password123!";
        String encodedPassword = "encodedPassword";
        String nickname = "테스트";

        MemberRequestDto requestDto = new MemberRequestDto(email, rawPassword, nickname, null);

        Member savedMember = Member.builder()
                .email(email)
                .password(encodedPassword)
                .nickname(nickname)
                .build();

        ReflectionTestUtils.setField(savedMember, "id", 1L);

        doNothing().when(signUpValidator).validate(any(MemberRequestDto.class));
        given(memberRepository.existsByEmail(email)).willReturn(false);
        given(passwordEncoder.encode(rawPassword)).willReturn(encodedPassword);
        given(memberRepository.save(any(Member.class))).willReturn(savedMember);

        // when
        Long memberId = memberService.signUp(requestDto);

        // then
        assertThat(memberId).isNotNull();
        verify(signUpValidator, times(1)).validate(requestDto);
        verify(memberRepository, times(1)).existsByEmail(email);
        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void signUp_fail_duplicateEmail() {
        // given
        String email = "duplicate@example.com";
        String password = "password123!";
        String nickname = "테스트";

        MemberRequestDto requestDto = new MemberRequestDto(email, password, nickname, null);

        doNothing().when(signUpValidator).validate(any(MemberRequestDto.class));
        given(memberRepository.existsByEmail(email)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.signUp(requestDto))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DUPLICATE_EMAIL);

        verify(signUpValidator, times(1)).validate(requestDto);
        verify(memberRepository, times(1)).existsByEmail(email);
        verify(passwordEncoder, never()).encode(anyString());
        verify(memberRepository, never()).save(any(Member.class));
    }
}