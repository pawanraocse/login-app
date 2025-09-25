package com.learning.loginapp.dto;

public record TenantDiscoveryResponseDto(
    String realm,
    String branding,
    String config,
    String ssoLoginUrl
) {}

