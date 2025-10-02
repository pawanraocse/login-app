package com.learning.loginapp.service;

import com.learning.loginapp.domain.Tenant;
import com.learning.loginapp.dto.tenant.TenantOnboardingRequestDto;
import com.learning.loginapp.dto.tenant.TenantOnboardingResponseDto;
import com.learning.loginapp.exception.TenantOnboardingException;
import com.learning.loginapp.repository.TenantRepository;
import com.learning.loginapp.util.KeycloakAdminClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class TenantOnboardingServiceImpl implements TenantOnboardingService {
    private static final Logger log = LoggerFactory.getLogger(TenantOnboardingServiceImpl.class);
    private final TenantRepository tenantRepository;
    private final KeycloakAdminClient keycloakAdminClient;

    @Override
    @Transactional
    public TenantOnboardingResponseDto onboardTenant(TenantOnboardingRequestDto request) {
        String userId = "system"; // TODO: extract from security context
        String requestId = java.util.UUID.randomUUID().toString();
        log.info("[onboardTenant] userId={}, requestId={}, operation=onboardTenant, tenantName={}", userId, requestId, request.tenantName());
        if (tenantRepository.findByName(request.tenantName()).isPresent()) {
            return new TenantOnboardingResponseDto(null, null, null, "FAILURE", "Tenant name already exists");
        }
        try {
            // 1. Create realm in Keycloak
            String realmName = keycloakAdminClient.createRealm(request.tenantName(), request.branding(), request.policies());
            // 2. Create client in Keycloak
            String clientId = keycloakAdminClient.createClient(realmName);
            // 3. Create admin user in Keycloak
            keycloakAdminClient.createAdminUser(realmName, request.adminUsername(), request.adminEmail(), request.adminPassword());
            // 4. Persist tenant metadata
            Tenant tenant = new Tenant();
            tenant.setName(request.tenantName());
            tenant.setRealm(realmName);
            tenant.setDisplayName(request.branding() != null ? (String) request.branding().getOrDefault("displayName", request.tenantName()) : request.tenantName());
            tenant.setStatus("ACTIVE");
            tenant.setDataResidency(request.policies() != null ? (String) request.policies().getOrDefault("dataResidency", "default") : "default");
            OffsetDateTime now = OffsetDateTime.now();
            tenant.setCreatedAt(now);
            tenant.setUpdatedAt(now);
            tenantRepository.save(tenant);
            log.info("[onboardTenant] userId={}, requestId={}, tenant onboarded: {}", userId, requestId, tenant.getId());
            return new TenantOnboardingResponseDto(tenant.getId() != null ? tenant.getId().toString() : null, realmName, clientId, "SUCCESS", "Tenant onboarded successfully");
        } catch (Exception ex) {
            log.error("[onboardTenant] userId={}, requestId={}, error during onboarding: {}", userId, requestId, ex.getMessage(), ex);
            throw new TenantOnboardingException("Failed to onboard tenant: " + ex.getMessage(), ex);
        }
    }
}
