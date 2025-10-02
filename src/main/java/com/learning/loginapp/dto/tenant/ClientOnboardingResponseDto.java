package com.learning.loginapp.dto.tenant;

public record ClientOnboardingResponseDto(
    String tenantName,
    String clientId,
    String status,
    String message
) {}

