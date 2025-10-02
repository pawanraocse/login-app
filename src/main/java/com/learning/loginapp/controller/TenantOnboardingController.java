package com.learning.loginapp.controller;

import com.learning.loginapp.dto.tenant.TenantOnboardingRequestDto;
import com.learning.loginapp.dto.tenant.TenantOnboardingResponseDto;
import com.learning.loginapp.service.TenantOnboardingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Tenant Onboarding API", description = "Endpoints for onboarding new tenants")
@RestController
@RequestMapping("/admin/tenants")
@RequiredArgsConstructor
@Slf4j
public class TenantOnboardingController {
    private final TenantOnboardingService tenantOnboardingService;

    /**
     * Sample API request for onboarding a new tenant:
     *
     * POST /admin/tenants
     * Content-Type: application/json
     *
     * {
     *   "tenantName": "acme-corp",
     *   "adminUsername": "acmeadmin",
     *   "adminEmail": "admin@acme.com",
     *   "adminPassword": "StrongPassword123!",
     *   "branding": {
     *     "displayName": "Acme Corporation",
     *     "logoUrl": "https://acme.com/logo.png",
     *     "themeColor": "#0055AA"
     *   },
     *   "policies": {
     *     "dataResidency": "EU",
     *     "passwordPolicy": "strong"
     *   }
     * }
     *
     * Sample response:
     * {
     *   "tenantId": "b1a2c3d4-5678-90ab-cdef-1234567890ab",
     *   "realmName": "acme-corp",
     *   "clientId": "acme-corp-client",
     *   "status": "SUCCESS",
     *   "message": "Tenant onboarded successfully"
     * }
     */
    @Operation(summary = "Onboard a new tenant (realm, client, admin user)")
    @PostMapping
    public ResponseEntity<TenantOnboardingResponseDto> onboardTenant(@Valid @RequestBody TenantOnboardingRequestDto request) {
        log.info("Onboarding tenant: {}", request.tenantName());
        TenantOnboardingResponseDto response = tenantOnboardingService.onboardTenant(request);
        return ResponseEntity.ok(response);
    }
}
