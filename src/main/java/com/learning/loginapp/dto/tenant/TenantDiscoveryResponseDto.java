package com.learning.loginapp.dto.tenant;

public record TenantDiscoveryResponseDto(
    String realm,
    String branding,
    String config,
    String ssoLoginUrl
) {}
