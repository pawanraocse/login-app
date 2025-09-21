package com.learning.loginapp.config;

import org.flywaydb.core.Flyway;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides DataSources for each tenant. On-demand creation and migration.
 * In production, replace with a config DB/service for tenant DB info.
 */
public class TenantDataSourceProvider {
    // Thread-safe cache of tenant DataSources
    private static final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();

    /**
     * Get or create a DataSource for the given tenant, running Flyway migration if new.
     * @param tenantId the tenant identifier
     * @param url JDBC URL for the tenant DB
     * @param username DB username
     * @param password DB password
     * @return DataSource for the tenant
     */
    public static DataSource getOrCreateAndMigrateDataSource(String tenantId, String url, String username, String password) {
        return dataSources.computeIfAbsent(tenantId, id -> {
            DataSource ds = createDataSource(url, username, password);
            // Run Flyway migration for this tenant DB
            Flyway flyway = Flyway.configure()
                    .dataSource(ds)
                    .locations("classpath:db/migration")
                    .baselineOnMigrate(true)
                    .load();
            flyway.migrate();
            return ds;
        });
    }

    private static DataSource createDataSource(String url, String username, String password) {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    /**
     * For legacy code: get DataSource if already created (else null).
     */
    public static DataSource getDataSource(String tenantId) {
        return dataSources.get(tenantId);
    }

    /**
     * For legacy code: get all DataSources (for admin/ops only).
     */
    public static Map<Object, Object> getAllDataSources() {
        return new ConcurrentHashMap<>(dataSources);
    }
}
