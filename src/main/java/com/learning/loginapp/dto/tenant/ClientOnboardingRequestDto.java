package com.learning.loginapp.dto.tenant;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public record ClientOnboardingRequestDto(
    @NotBlank String tenantName,
    @NotBlank String clientId,
    @NotBlank String clientName,
    Map<String, Object> config
) {}

