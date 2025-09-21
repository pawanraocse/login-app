package com.learning.loginapp.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO for ExtraPolicy entity. Used for API input/output. Never expose JPA entities directly.
 */
public record ExtraPolicyDto(
    UUID id,
    UUID tenantId,
    String policyType,
    String policy,
    boolean enabled,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}

