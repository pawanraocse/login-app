package com.learning.loginapp.controller;

import com.learning.loginapp.dto.tenant.ClientOnboardingRequestDto;
import com.learning.loginapp.dto.tenant.ClientOnboardingResponseDto;
import com.learning.loginapp.service.ClientOnboardingService;
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

@Tag(name = "Client Onboarding API", description = "Endpoints for onboarding new clients for a tenant")
@RestController
@RequestMapping("/admin/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientOnboardingController {
    private final ClientOnboardingService clientOnboardingService;

    /**
     * Sample API request for onboarding a new client:
     *
     * POST /admin/clients
     * Content-Type: application/json
     *
     * {
     *   "tenantName": "acme-corp",
     *   "clientId": "acme-corp-app",
     *   "clientName": "Acme Corp App",
     *   "config": {
     *     "redirectUris": ["https://acme.com/callback"],
     *     "publicClient": true
     *   }
     * }
     *
     * Sample response:
     * {
     *   "tenantName": "acme-corp",
     *   "clientId": "acme-corp-app",
     *   "status": "SUCCESS",
     *   "message": "Client onboarded successfully"
     * }
     */
    @Operation(summary = "Onboard a new client for a tenant")
    @PostMapping
    public ResponseEntity<ClientOnboardingResponseDto> onboardClient(@Valid @RequestBody ClientOnboardingRequestDto request) {
        log.info("Onboarding client: {} for tenant: {}", request.clientId(), request.tenantName());
        ClientOnboardingResponseDto response = clientOnboardingService.onboardClient(request);
        return ResponseEntity.ok(response);
    }
}

