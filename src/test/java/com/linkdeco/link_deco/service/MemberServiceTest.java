package com.linkdeco.link_deco.service;

import com.linkdeco.link_deco.domain.Member;
import com.linkdeco.link_deco.dto.MemberRequestDto;
import com.linkdeco.link_deco.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private S3Service s3Service;

    private final String defaultProfileImage = "https://default-image.url";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(memberService, "defaultProfileImage", defaultProfileImage);
    }

    @Test
    @DisplayName("회원가입 성공 - 프로필 이미지 없음 (기본 이미지)")
    void signup_WithoutProfileImage() {
        // given
        MemberRequestDto requestDto = new MemberRequestDto("test@test.com", "password123!", "tester");
        given(passwordEncoder.encode(requestDto.getPassword())).willReturn("encodedPassword");
        given(memberRepository.save(any(Member.class))).willAnswer(invocation -> {
            Member member = invocation.getArgument(0);
            ReflectionTestUtils.setField(member, "id", 1L);
            return member;
        });

        // when
        Long memberId = memberService.signup(requestDto, null);

        // then
        assertThat(memberId).isEqualTo(1L);
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 성공 - 프로필 이미지 있음")
    void signup_WithProfileImage() {
        // given
        MemberRequestDto requestDto = new MemberRequestDto("test@test.com", "password123!", "tester");
        MockMultipartFile profileImage = new MockMultipartFile("profileImage", "test.png", "image/png", "test content".getBytes());
        
        given(passwordEncoder.encode(requestDto.getPassword())).willReturn("encodedPassword");
        given(s3Service.upload(profileImage)).willReturn("https://s3-image.url/test.png");
        given(memberRepository.save(any(Member.class))).willAnswer(invocation -> {
            Member member = invocation.getArgument(0);
            ReflectionTestUtils.setField(member, "id", 1L);
            return member;
        });

        // when
        Long memberId = memberService.signup(requestDto, profileImage);

        // then
        assertThat(memberId).isEqualTo(1L);
        verify(s3Service).upload(profileImage);
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("프로필 이미지 업데이트")
    void updateProfileImage() {
        // given
        Long memberId = 1L;
        String newImageUrl = "https://new-image.url";
        Member member = Member.builder()
                .email("test@test.com")
                .nickname("tester")
                .build();
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // when
        memberService.updateProfileImage(memberId, newImageUrl);

        // then
        assertThat(member.getProfileImage()).isEqualTo(newImageUrl);
    }

    @Test
    @DisplayName("프로필 이미지 삭제 - 기본 이미지로 변경")
    void deleteProfileImage() {
        // given
        Long memberId = 1L;
        Member member = Member.builder()
                .email("test@test.com")
                .nickname("tester")
                .profileImage("https://s3-image.url/old.png")
                .build();
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // when
        memberService.deleteProfileImage(memberId);

        // then
        assertThat(member.getProfileImage()).isEqualTo(defaultProfileImage);
        verify(s3Service).delete("old.png");
    }
}