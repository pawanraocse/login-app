package com.learning.loginapp.repository;

import com.learning.loginapp.domain.AuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AuditEventRepository extends JpaRepository<AuditEvent, UUID> {
    List<AuditEvent> findByTenantId(UUID tenantId);
}
