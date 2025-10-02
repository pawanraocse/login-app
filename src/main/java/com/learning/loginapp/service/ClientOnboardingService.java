package com.learning.loginapp.service;

import com.learning.loginapp.dto.tenant.ClientOnboardingRequestDto;
import com.learning.loginapp.dto.tenant.ClientOnboardingResponseDto;

public interface ClientOnboardingService {
    ClientOnboardingResponseDto onboardClient(ClientOnboardingRequestDto request);
}

