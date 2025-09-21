package com.learning.loginapp.repository;

import com.learning.loginapp.domain.IdpConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface IdpConfigRepository extends JpaRepository<IdpConfig, UUID> {
    List<IdpConfig> findByTenantId(UUID tenantId);
}

