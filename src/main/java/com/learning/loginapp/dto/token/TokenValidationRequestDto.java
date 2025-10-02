package com.learning.loginapp.dto.token;

import jakarta.validation.constraints.NotBlank;

public record TokenValidationRequestDto(
    @NotBlank String tenantId,
    @NotBlank String token
) {}
