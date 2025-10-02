package com.learning.loginapp.service;

import com.learning.loginapp.dto.login.LoginRequestDto;
import com.learning.loginapp.dto.login.LoginResponseDto;
import com.learning.loginapp.dto.token.TokenValidationRequestDto;
import com.learning.loginapp.dto.token.TokenValidationResponseDto;
import com.learning.loginapp.dto.tenant.TenantDiscoveryRequestDto;
import com.learning.loginapp.dto.tenant.TenantDiscoveryResponseDto;
import com.learning.loginapp.dto.login.SsoLoginUrlResponseDto;
import com.learning.loginapp.dto.login.LogoutRequestDto;
import com.learning.loginapp.dto.login.LogoutResponseDto;

public interface LoginService {
    LoginResponseDto login(LoginRequestDto request);

    TokenValidationResponseDto validateToken(TokenValidationRequestDto request);

    TenantDiscoveryResponseDto tenantDiscovery(TenantDiscoveryRequestDto request);

    SsoLoginUrlResponseDto getSsoLoginUrl(String tenantId, String redirectUri);

    LogoutResponseDto logout(LogoutRequestDto request);
}
