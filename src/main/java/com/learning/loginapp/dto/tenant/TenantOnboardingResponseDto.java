package com.learning.loginapp.dto.tenant;

public record TenantOnboardingResponseDto(
    String tenantId,
    String realmName,
    String clientId,
    String status,
    String message
) {}
