package com.learning.loginapp.dto.app;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO for IdpConfig entity. Used for API input/output. Never expose JPA entities directly.
 */
public record IdpConfigDto(
    UUID id,
    UUID tenantId,
    String idpType,
    String config,
    boolean enabled,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
