package com.linkdeco.link_deco.global.validator;

import com.linkdeco.link_deco.dto.MemberRequestDto;
import com.linkdeco.link_deco.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@NullMarked
@Component
@RequiredArgsConstructor
public class SignupValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        // 어떤 객체를 검증할지 결정 (MemberRequestDto에 대해서만 작동)
        return MemberRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberRequestDto requestDto = (MemberRequestDto) target;

        // 이메일 중복 체크 (DB 조회)
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            errors.rejectValue("email", "invalid.email", "이미 사용 중인 이메일입니다.");
        }
    }
}
