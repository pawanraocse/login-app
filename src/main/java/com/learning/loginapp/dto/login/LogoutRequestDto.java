package com.learning.loginapp.dto.login;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequestDto(
    @NotBlank String tenantId,
    @NotBlank String username,
    @NotBlank String refreshToken
) {}
