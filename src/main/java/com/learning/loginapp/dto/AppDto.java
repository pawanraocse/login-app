package com.learning.loginapp.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for App entity. Used for API input/output. Never expose JPA entities directly.
 */
public record AppDto(
    UUID id,
    UUID tenantId,
    String clientId,
    String name,
    String type,
    List<String> redirectUris,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
