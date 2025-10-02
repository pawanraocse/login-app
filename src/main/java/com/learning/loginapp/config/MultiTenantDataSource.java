package com.learning.loginapp.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Routes to the correct DataSource based on the current tenant.
 * Falls back to 'default' DataSource for onboarding/global operations.
 */
public class MultiTenantDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        String tenantId = TenantContext.getTenantId();
        // Fallback to 'default' DataSource if tenantId is not set or not found
        if (tenantId == null || getResolvedDataSources() == null || !getResolvedDataSources().containsKey(tenantId)) {
            return "default";
        }
        return tenantId;
    }
}
