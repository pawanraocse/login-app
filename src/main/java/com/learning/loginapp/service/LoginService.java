package com.learning.loginapp.service;

import com.learning.loginapp.dto.*;

public interface LoginService {
    LoginResponseDto login(LoginRequestDto request);

    TokenValidationResponseDto validateToken(TokenValidationRequestDto request);

    TenantDiscoveryResponseDto tenantDiscovery(TenantDiscoveryRequestDto request);

    SsoLoginUrlResponseDto getSsoLoginUrl(String tenantId, String redirectUri);

    LogoutResponseDto logout(LogoutRequestDto request);
}

