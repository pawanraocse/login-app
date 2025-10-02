package com.learning.loginapp.service;

import com.learning.loginapp.dto.tenant.ClientOnboardingRequestDto;
import com.learning.loginapp.dto.tenant.ClientOnboardingResponseDto;
import com.learning.loginapp.util.KeycloakAdminClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientOnboardingServiceImpl implements ClientOnboardingService {
    private static final Logger log = LoggerFactory.getLogger(ClientOnboardingServiceImpl.class);
    private final KeycloakAdminClient keycloakAdminClient;

    @Override
    @Transactional
    public ClientOnboardingResponseDto onboardClient(ClientOnboardingRequestDto request) {
        log.info("[onboardClient] tenantName={}, clientId={}, clientName={}", request.tenantName(), request.clientId(), request.clientName());
        try {
            String createdClientId = keycloakAdminClient.createClient(request.tenantName(), request.clientId(), request.clientName(), request.config());
            return new ClientOnboardingResponseDto(request.tenantName(), createdClientId, "SUCCESS", "Client onboarded successfully");
        } catch (Exception ex) {
            log.error("[onboardClient] error: {}", ex.getMessage(), ex);
            return new ClientOnboardingResponseDto(request.tenantName(), request.clientId(), "FAILURE", "Client onboarding failed: " + ex.getMessage());
        }
    }
}

