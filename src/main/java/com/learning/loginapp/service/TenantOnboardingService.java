package com.learning.loginapp.service;

import com.learning.loginapp.dto.tenant.TenantOnboardingRequestDto;
import com.learning.loginapp.dto.tenant.TenantOnboardingResponseDto;

public interface TenantOnboardingService {
    TenantOnboardingResponseDto onboardTenant(TenantOnboardingRequestDto request);
}

