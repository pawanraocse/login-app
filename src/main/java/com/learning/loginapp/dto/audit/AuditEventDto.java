package com.learning.loginapp.dto.audit;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO for AuditEvent entity. Used for API input/output. Never expose JPA entities directly.
 */
public record AuditEventDto(
    UUID id,
    UUID tenantId,
    String eventType,
    String principal,
    String details,
    OffsetDateTime createdAt
) {}
