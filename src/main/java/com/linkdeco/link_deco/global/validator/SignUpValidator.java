package com.linkdeco.link_deco.global.validator;

import com.linkdeco.link_deco.common.ValidationConstants;
import com.linkdeco.link_deco.dto.MemberRequestDto;
import com.linkdeco.link_deco.global.exception.CustomException;
import com.linkdeco.link_deco.global.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class SignUpValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$"
    );

    public void validate(MemberRequestDto requestDto) {
        validateEmail(requestDto.getEmail());
        validatePassword(requestDto.getPassword());
        validateNickname(requestDto.getNickname());
    }

    private void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new CustomException(ErrorCode.INVALID_EMAIL_FORMAT);
        }
    }

    private void validatePassword(String password) {
        if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD_FORMAT);
        }
    }

    private void validateNickname(String nickname) {
        if (nickname == null || nickname.length() < ValidationConstants.MIN_NICKNAME_LENGTH || nickname.length() > ValidationConstants.MAX_NICKNAME_LENGTH) {
            throw new CustomException(ErrorCode.INVALID_NICKNAME_LENGTH);
        }
    }
}
