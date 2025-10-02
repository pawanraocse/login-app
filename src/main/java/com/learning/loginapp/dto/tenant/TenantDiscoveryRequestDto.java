package com.learning.loginapp.dto.tenant;

import jakarta.validation.constraints.NotBlank;

public record TenantDiscoveryRequestDto(
    @NotBlank String tenantId
) {}
