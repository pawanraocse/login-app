package com.learning.loginapp.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

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

    public String createRealm(String realmName, Map<String, Object> branding, Map<String, Object> policies) {
        Map<String, Object> payload = Map.of(
                "realm", realmName,
                "enabled", true,
                "displayName", branding != null ? branding.getOrDefault("displayName", realmName) : realmName,
                "attributes", branding
        );
        log.info("Creating realm {} via Keycloak Admin API", realmName);
        webClient.post()
                .uri(keycloakAdminUrl + "/admin/realms")
                .headers(headers -> headers.setBasicAuth(adminUsername, adminPassword))
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        return realmName;
    }

    public String createClient(String realmName) {
        Map<String, Object> payload = Map.of(
                "clientId", realmName + "-client",
                "enabled", true,
                "publicClient", true
        );
        log.info("Creating client for realm {}", realmName);
        webClient.post()
                .uri(keycloakAdminUrl + "/admin/realms/" + realmName + "/clients")
                .headers(headers -> headers.setBasicAuth(adminUsername, adminPassword))
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        return realmName + "-client";
    }

    public void createAdminUser(String realmName, String username, String email, String password) {
        Map<String, Object> payload = Map.of(
                "username", username,
                "email", email,
                "enabled", true,
                "credentials", new Object[]{Map.of("type", "password", "value", password, "temporary", false)}
        );
        log.info("Creating admin user {} for realm {}", username, realmName);
        webClient.post()
                .uri(keycloakAdminUrl + "/admin/realms/" + realmName + "/users")
                .headers(headers -> headers.setBasicAuth(adminUsername, adminPassword))
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}

