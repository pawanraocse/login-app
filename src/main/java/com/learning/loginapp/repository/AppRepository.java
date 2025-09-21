package com.learning.loginapp.repository;

import com.learning.loginapp.domain.App;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AppRepository extends JpaRepository<App, UUID> {
    List<App> findByTenantId(UUID tenantId);
    App findByTenantIdAndClientId(UUID tenantId, String clientId);
}

