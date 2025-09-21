package com.learning.loginapp.repository;

import com.learning.loginapp.domain.ExtraPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ExtraPolicyRepository extends JpaRepository<ExtraPolicy, UUID> {
    List<ExtraPolicy> findByTenantId(UUID tenantId);
}

