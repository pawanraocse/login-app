package com.learning.loginapp.dto;

import jakarta.validation.constraints.NotBlank;

public record TenantDiscoveryRequestDto(
    @NotBlank String tenantId
) {}

