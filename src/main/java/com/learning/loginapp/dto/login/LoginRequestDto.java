package com.learning.loginapp.dto.login;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
    @NotBlank String tenantId,
    @NotBlank String username,
    @NotBlank String password
) {}
