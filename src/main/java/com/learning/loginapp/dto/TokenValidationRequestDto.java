package com.learning.loginapp.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenValidationRequestDto(
    @NotBlank String tenantId,
    @NotBlank String token
) {}

