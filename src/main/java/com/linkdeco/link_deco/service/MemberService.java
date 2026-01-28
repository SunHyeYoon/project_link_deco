package com.linkdeco.link_deco.service;

import com.linkdeco.link_deco.domain.Member;
import com.linkdeco.link_deco.dto.LoginRequestDto;
import com.linkdeco.link_deco.dto.LoginResponseDto;
import com.linkdeco.link_deco.dto.MemberRequestDto;
import com.linkdeco.link_deco.global.exception.CustomException;
import com.linkdeco.link_deco.global.exception.ErrorCode;
import com.linkdeco.link_deco.global.security.JwtProvider;
import com.linkdeco.link_deco.global.security.JwtToken;
import com.linkdeco.link_deco.global.validator.SignUpValidator;
import com.linkdeco.link_deco.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignUpValidator signUpValidator;
    private final JwtProvider jwtProvider;

    @Transactional
    public Long signUp(MemberRequestDto requestDto) {
        // 1. 입력값 유효성 검증
        signUpValidator.validate(requestDto);

        // 2. 이메일 중복 확인
        validateDuplicateEmail(requestDto.email());

        // 3. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.password());

        // 4. 객체 생성 및 저장
        Member member = requestDto.toEntity(encodedPassword);

        return memberRepository.save(member).getId();
    }

    private void validateDuplicateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto) {
        // 1. 이메일로 회원 조회
        Member member = memberRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.password(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 3. 토큰 생성
        JwtToken jwtToken = jwtProvider.generateJwtToken(member.getId(), member.getEmail());

        // 4. 로그인 응답 반환
        return LoginResponseDto.of(jwtToken, member);
    }
}
