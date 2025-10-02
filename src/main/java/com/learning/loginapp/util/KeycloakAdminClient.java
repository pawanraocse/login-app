package com.learning.loginapp.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Component
@Slf4j
public class KeycloakAdminClient {
    @Value("${keycloak.admin.url}")
    private String keycloakAdminUrl;
    @Value("${keycloak.admin.username}")
    private String adminUsername;
    @Value("${keycloak.admin.password}")
    private String adminPassword;

    private WebClient webClient;

    public KeycloakAdminClient() {
        this.webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private String getAdminAccessToken() {
        try {
            Map<String, String> tokenResponse = webClient.post()
                .uri(keycloakAdminUrl + "/realms/master/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", "admin-cli")
                        .with("username", adminUsername)
                        .with("password", adminPassword))
                .retrieve()
                .bodyToMono(Map.class)
                .block();
            return (String) tokenResponse.get("access_token");
        } catch (WebClientResponseException ex) {
            log.error("Failed to get admin access token from Keycloak: {}", ex.getResponseBodyAsString());
            throw new RuntimeException("Keycloak admin token error: " + ex.getMessage(), ex);
        }
    }

    public String createRealm(String realmName, Map<String, Object> branding, Map<String, Object> policies) {
        String accessToken = getAdminAccessToken();
        Map<String, Object> payload = Map.of(
                "realm", realmName,
                "enabled", true,
                "displayName", branding != null ? branding.getOrDefault("displayName", realmName) : realmName,
                "attributes", branding
        );
        log.info("Creating realm {} via Keycloak Admin API", realmName);
        webClient.post()
                .uri(keycloakAdminUrl + "/admin/realms")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        return realmName;
    }

    public String createClient(String realmName) {
        String accessToken = getAdminAccessToken();
        Map<String, Object> payload = Map.of(
                "clientId", realmName + "-client",
                "enabled", true,
                "publicClient", true
        );
        log.info("Creating client for realm {}", realmName);
        webClient.post()
                .uri(keycloakAdminUrl + "/admin/realms/" + realmName + "/clients")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        return realmName + "-client";
    }

    public void createAdminUser(String realmName, String username, String email, String password) {
        String accessToken = getAdminAccessToken();
        Map<String, Object> payload = Map.of(
                "username", username,
                "email", email,
                "enabled", true,
                "credentials", new Object[]{Map.of("type", "password", "value", password, "temporary", false)}
        );
        log.info("Creating admin user {} for realm {}", username, realmName);
        webClient.post()
                .uri(keycloakAdminUrl + "/admin/realms/" + realmName + "/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
