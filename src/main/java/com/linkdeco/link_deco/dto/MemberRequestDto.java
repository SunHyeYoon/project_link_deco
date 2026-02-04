package com.linkdeco.link_deco.dto;

import com.linkdeco.link_deco.common.ValidationConstants;
import com.linkdeco.link_deco.domain.Member;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {

    @Pattern(
            regexp = ValidationConstants.EMAIL_REGEX,
            message = "올바른 이메일 형식이 아닙니다."
    )
    private String email;

    @Pattern(
            regexp = ValidationConstants.PW_REGEX,
            message = "8~24자의 영문 대소문자, 숫자, 특수문자를 조합하여 설정해 주세요."
    )
    private String password;

    @Pattern(
            regexp = ValidationConstants.NICKNAME_REGEX,
            message = "닉네임은 공백 없이 2자 이상 10자 이하여야 합니다.")
    private String nickname;

//    @Size(max = ValidationConstants.MAX_IMAGE_URL_LENGTH)
//    private String profileImage;

    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .email(this.getEmail())
                .password(encodedPassword)
                .nickname(this.getNickname())
//                .profileImage(this.getProfileImage())
                .build();
    }
}
