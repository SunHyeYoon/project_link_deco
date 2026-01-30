package com.linkdeco.link_deco.dto;

import com.linkdeco.link_deco.common.ValidationConstants;
import com.linkdeco.link_deco.domain.Member;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "이메일은 필수입니다.")
    @Size(max = ValidationConstants.MAX_EMAIL_LENGTH)
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = ValidationConstants.MIN_PASSWORD_LENGTH, max = ValidationConstants.MAX_PASSWORD_LENGTH)
    private String password;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = ValidationConstants.MIN_NICKNAME_LENGTH, max = ValidationConstants.MAX_NICKNAME_LENGTH)
    private String nickname;

    @Size(max = ValidationConstants.MAX_IMAGE_URL_LENGTH)
    private String imageUrl;

    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .email(this.getEmail())
                .password(encodedPassword)
                .nickname(this.getNickname())
                .imageUrl(this.getImageUrl())
                .build();
    }
}
