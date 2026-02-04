package com.linkdeco.link_deco.global.validator;

import com.linkdeco.link_deco.dto.MemberRequestDto;
import com.linkdeco.link_deco.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SignupValidatorTest {

    @InjectMocks
    private SignupValidator signupValidator;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("중복 이메일이면 에러가 발생한다")
    void validate_DuplicateEmail() {
        // given
        String duplicateEmail = "test@test.com";
        MemberRequestDto requestDto = new MemberRequestDto(duplicateEmail, "password123!", "tester");
        given(memberRepository.existsByEmail(duplicateEmail)).willReturn(true);
        Errors errors = new BeanPropertyBindingResult(requestDto, "requestDto");

        // when
        signupValidator.validate(requestDto, errors);

        // then
        assertThat(errors.hasErrors()).isTrue();
        assertThat(errors.getFieldError("email")).isNotNull();
        assertThat(errors.getFieldError("email").getCode()).isEqualTo("invalid.email");
        assertThat(errors.getFieldError("email").getDefaultMessage()).isEqualTo("이미 사용 중인 이메일입니다.");

        verify(memberRepository).existsByEmail(duplicateEmail);
    }

    @Test
    @DisplayName("이메일이 중복되지 않으면 에러가 발생하지 않는다")
    void validate_NoDuplicateEmail() {
        // given
        String uniqueEmail = "unique@test.com";
        MemberRequestDto requestDto = new MemberRequestDto(uniqueEmail, "password123!", "tester");
        given(memberRepository.existsByEmail(uniqueEmail)).willReturn(false);
        Errors errors = new BeanPropertyBindingResult(requestDto, "requestDto");

        // when
        signupValidator.validate(requestDto, errors);

        // then
        assertThat(errors.hasErrors()).isFalse();
        verify(memberRepository).existsByEmail(uniqueEmail);
    }

    @Test
    @DisplayName("MemberRequestDto 클래스를 지원한다")
    void supports() {
        // when
        boolean result = signupValidator.supports(MemberRequestDto.class);

        // then
        assertThat(result).isTrue();
    }
}