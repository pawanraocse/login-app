package com.learning.loginapp.dto.tenant;

import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TenantOnboardingRequestDto(
    @NotBlank String tenantName,
    @NotBlank String adminUsername,
    @NotBlank String adminEmail,
    @NotBlank String adminPassword,
    Map<String, Object> branding,
    Map<String, Object> policies
) {}
