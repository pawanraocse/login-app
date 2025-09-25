package com.learning.loginapp.service;

import com.learning.loginapp.dto.LoginRequestDto;
import com.learning.loginapp.dto.LoginResponseDto;
import com.learning.loginapp.dto.LogoutRequestDto;
import com.learning.loginapp.dto.LogoutResponseDto;
import com.learning.loginapp.dto.SsoLoginUrlResponseDto;
import com.learning.loginapp.dto.TenantDiscoveryRequestDto;
import com.learning.loginapp.dto.TenantDiscoveryResponseDto;
import com.learning.loginapp.dto.TokenValidationRequestDto;
import com.learning.loginapp.dto.TokenValidationResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    private final WebClient webClient;
    private final String keycloakBaseUrl;
    private final String clientId;
    private final String clientSecret; // Optional, for confidential clients

    public LoginServiceImpl(
            @Value("${keycloak.base-url:http://localhost:8080}") String keycloakBaseUrl,
            @Value("${keycloak.client-id:dev-client}") String clientId,
            @Value("${keycloak.client-secret:}") String clientSecret
    ) {
        this.webClient = WebClient.builder().build();
        this.keycloakBaseUrl = keycloakBaseUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token", keycloakBaseUrl, request.tenantId());
        log.info("Authenticating user '{}' for tenant '{}' via Keycloak at {}", request.username(), request.tenantId(), tokenUrl);
        var form = BodyInserters.fromFormData("grant_type", "password")
                .with("client_id", clientId)
                .with("username", request.username())
                .with("password", request.password());
        if (clientSecret != null && !clientSecret.isBlank()) {
            form = form.with("client_secret", clientSecret);
        }
        try {
            Map<String, Object> response = webClient.post()
                    .uri(tokenUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            if (response == null || !response.containsKey("access_token")) {
                log.warn("Keycloak did not return an access_token for user '{}' in tenant '{}'", request.username(), request.tenantId());
                throw new RuntimeException("Invalid credentials or Keycloak error");
            }
            log.info("Response from Keycloak: {}", response);
            return new LoginResponseDto(
                    (String) response.get("access_token"),
                    (String) response.get("refresh_token"),
                    (String) response.getOrDefault("token_type", "Bearer"),
                    ((Number) response.getOrDefault("expires_in", 3600)).longValue(),
                    response // Optionally filter/parse claims
            );
        } catch (Exception ex) {
            log.error("Login failed for user '{}' in tenant '{}': {}", request.username(), request.tenantId(), ex.getMessage());
            throw new RuntimeException("Authentication failed: " + ex.getMessage());
        }
    }

    @Override
    public TokenValidationResponseDto validateToken(TokenValidationRequestDto request) {
        // TODO: Validate JWT using Keycloak public key
        return new TokenValidationResponseDto(true, null, null);
    }

    @Override
    public TenantDiscoveryResponseDto tenantDiscovery(TenantDiscoveryRequestDto request) {
        // TODO: Fetch tenant metadata, branding, SSO URLs
        return new TenantDiscoveryResponseDto("demo-realm", "demo-branding", "demo-config", "https://keycloak.example.com/auth");
    }

    @Override
    public SsoLoginUrlResponseDto getSsoLoginUrl(String tenantId, String redirectUri) {
        // TODO: Build SSO login URL for Keycloak
        return new SsoLoginUrlResponseDto("https://keycloak.example.com/auth?tenant=" + tenantId + "&redirect_uri=" + redirectUri);
    }

    @Override
    public LogoutResponseDto logout(LogoutRequestDto request) {
        // TODO: Invalidate refresh token/session in Keycloak
        return new LogoutResponseDto(true, null);
    }
}
