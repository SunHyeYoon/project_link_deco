package com.linkdeco.link_deco.dto;

import com.linkdeco.link_deco.common.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(@NotBlank
                              @Size(max = ValidationConstants.MAX_EMAIL_LENGTH)
                              String email,

                              @NotBlank
                              @Size(min = ValidationConstants.MIN_PASSWORD_LENGTH, max = ValidationConstants.MAX_PASSWORD_LENGTH)
                              String password
                              ) {
}
