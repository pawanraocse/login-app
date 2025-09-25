package com.learning.loginapp.controller;

import com.learning.loginapp.dto.*;
import com.learning.loginapp.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Login API", description = "Endpoints for authentication and tenant discovery")
public class LoginController {
    private final LoginService loginService;

    @Operation(summary = "Authenticate user and return JWT")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        log.info("Login attempt for tenant={}, user={}", request.tenantId(), request.username());
        return ResponseEntity.ok(loginService.login(request));
    }

    @Operation(summary = "Validate JWT token")
    @PostMapping("/token/validate")
    public ResponseEntity<TokenValidationResponseDto> validateToken(@Valid @RequestBody TokenValidationRequestDto request) {
        log.info("Token validation for tenant={}", request.tenantId());
        return ResponseEntity.ok(loginService.validateToken(request));
    }

    @Operation(summary = "Discover tenant metadata and SSO URLs")
    @PostMapping("/tenant-discovery")
    public ResponseEntity<TenantDiscoveryResponseDto> tenantDiscovery(@Valid @RequestBody TenantDiscoveryRequestDto request) {
        log.info("Tenant discovery for tenant={}", request.tenantId());
        return ResponseEntity.ok(loginService.tenantDiscovery(request));
    }

    @Operation(summary = "Get SSO login URL for a tenant")
    @GetMapping("/sso-login-url")
    public ResponseEntity<SsoLoginUrlResponseDto> getSsoLoginUrl(@RequestParam String tenantId, @RequestParam String redirectUri) {
        log.info("SSO login URL requested for tenant={}", tenantId);
        return ResponseEntity.ok(loginService.getSsoLoginUrl(tenantId, redirectUri));
    }

    @Operation(summary = "Logout and invalidate refresh token")
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDto> logout(@Valid @RequestBody LogoutRequestDto request) {
        log.info("Logout for tenant={}, user={}", request.tenantId(), request.username());
        return ResponseEntity.ok(loginService.logout(request));
    }

    @Operation(summary = "Health check")
    @GetMapping("/health")
    public ResponseEntity<HealthResponseDto> health() {
        return ResponseEntity.ok(new HealthResponseDto("UP"));
    }
}
