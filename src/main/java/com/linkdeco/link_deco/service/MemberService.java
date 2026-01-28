package com.linkdeco.link_deco.service;

import com.linkdeco.link_deco.domain.Member;
import com.linkdeco.link_deco.dto.MemberRequestDto;
import com.linkdeco.link_deco.global.exception.CustomException;
import com.linkdeco.link_deco.global.exception.ErrorCode;
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
}
