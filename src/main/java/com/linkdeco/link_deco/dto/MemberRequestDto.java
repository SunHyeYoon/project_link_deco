package com.linkdeco.link_deco.dto;

import com.linkdeco.link_deco.common.ValidationConstants;
import com.linkdeco.link_deco.domain.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberRequestDto(@NotBlank
                               @Size(max = ValidationConstants.MAX_EMAIL_LENGTH)
                               String email,

                               @NotBlank
                               @Size(min = ValidationConstants.MIN_PASSWORD_LENGTH, max = ValidationConstants.MAX_PASSWORD_LENGTH)
                               String password,

                               @NotBlank
                               @Size(min = ValidationConstants.MIN_NICKNAME_LENGTH, max = ValidationConstants.MAX_NICKNAME_LENGTH)
                               String nickname,

                               @Size(max = ValidationConstants.MAX_IMAGE_URL_LENGTH)
                               String imageUrl) {

    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .email(this.email())       // 혹은 그냥 email()
                .password(encodedPassword)
                .nickname(this.nickname())
                .imageUrl(this.imageUrl())
                .build();
    }
}
