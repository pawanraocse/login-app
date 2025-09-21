# Project Progress Tracker

## Completed
- Docker Compose for Keycloak + Postgres (local dev)
- Keycloak realm import with dev user/client
- Spring Boot app: application.properties for DB, Keycloak, security, logging, CORS
- Port conflict resolved (Spring Boot on 8081, Keycloak on 8080)
- PostgreSQL JDBC driver added to pom.xml
- Initial orchestration DB schema (tenants, apps, idp_configs, extra_policies, audit_events) – Flyway migration V1
- Login service and other services (e.g., image gallery) run behind an application gateway.
- User accesses UI and is prompted for tenant id, username, and password.
- On login, the service:
    - Looks up tenant DB by tenant id.
    - Runs Flyway migration for the tenant DB if needed (on-demand).
    - Authenticates user and issues a JWT token with tenant id.
- User receives JWT token and uses it to access other services.
- Application gateway and all services validate the token and enforce tenant boundaries.

## Next Up
- Scaffold JPA entities and repositories for orchestration tables
- Implement basic integration test for DB connectivity/schema
- Implement tenant resolver and admin API skeletons
- Integrate with Keycloak Admin API for provisioning

## Reference
- See login_auth_service_requirements.md for full requirements and architecture

# Progress Log

## Multi-Tenant Auth Flow (Latest)

- [x] Login service and other services (e.g., image gallery) run behind an application gateway.
- [x] User accesses UI and is prompted for tenant id, username, and password.
- [x] On login, the service:
    - Looks up tenant DB by tenant id.
    - Runs Flyway migration for the tenant DB if needed (on-demand).
    - Authenticates user and issues a JWT token with tenant id.
- [x] User receives JWT token and uses it to access other services.
- [x] Application gateway and all services validate the token and enforce tenant boundaries.
- [ ] Document token structure and validation logic.
- [ ] Add integration tests for multi-tenant login and service access.

## Next Steps
- Implement robust error handling for missing/invalid tenants.
- Automate tenant DB provisioning and migration.
- Harden JWT validation and tenant isolation in all services.
- Document onboarding and security best practices.
