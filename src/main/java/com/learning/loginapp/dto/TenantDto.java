package com.learning.loginapp.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO for Tenant entity. Used for API input/output. Never expose JPA entities directly.
 */
public record TenantDto(
    UUID id,
    String name,
    String realm,
    String displayName,
    String status,
    String dataResidency,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}

