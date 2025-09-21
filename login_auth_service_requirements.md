# Login / Authentication Service – Requirements

## 1. Purpose
A portable, multi-tenant Login & Authentication Service built with Spring Boot, orchestrating and integrating with Keycloak (primary IdP) and optionally Auth0 for specific tenants. The service does not re-implement token protocols but leverages Keycloak’s standards-compliant OIDC/SAML features while providing a unified admin API, provisioning, and per-tenant configuration. Supports a hybrid approach where Spring Security handles in-app authorization, and Keycloak handles identity federation, SSO, social login, and token issuance.

## 2. Target Capabilities
- **Keycloak Integration:** Full integration with Keycloak REST Admin API to provision realms, clients, roles, and users.
- **Auth0 Support:** Optional federation with Auth0 for tenants already on Auth0.
- **IdP Integrations via Keycloak:** OIDC/SAML providers (Okta, Azure AD, Google, Ping, Cognito, corporate SAML).
- **SSO:** Full OIDC Provider support (Authorization Code + PKCE, refresh rotation) and SAML SP via Keycloak.
- **Manual User CRUD:** Local user store in Keycloak with password/MFA flows.
- **Permissions:** RBAC via Keycloak; optional ABAC/policy engine (OPA integration).
- **Multi-Tenant:** Each tenant corresponds to a Keycloak realm and orchestration layer.
- **Token Introspection:** RFC 7662 endpoint for legacy services and admin tooling.
- **Audit & Compliance:** Append-only audit log, export/erase APIs, data residency flags.
- **Rate Limiting & Security Hardening:** Sliding-window per IP+tenant; strict CORS, PKCE enforced at edge.

## 3. Architecture Overview
```
Client (SPA / Mobile / Server)
   │
API Gateway / Edge
   │
┌───────────────────────────────────────────────────┐
│ Spring Boot Orchestration Service (Auth Service)  │
│ 1) Edge & Security Layer                          │
│ - TenantResolverFilter                            │
│ - RateLimit/CORS                                  │
│ - TokenIntrospection/JWT Validation (via Keycloak)│
│ 2) Keycloak Admin Integration                     │
│ - Per-tenant realm/client provisioning            │
│ - User & Role management via REST Admin API       │
│ 3) Extra Business Logic                           │
│ - ABAC/OPA integration                            │
│ - Billing hooks, tenant metadata                  │
└───────────────────────────────────────────────────┘
   │
┌───────────────────────────────────────────────────┐
│ Keycloak Cluster (separate service)               │
│ - OIDC Provider + SAML SP                         │
│ - Local user store + MFA                          │
│ - Identity Provider federation (Auth0, Okta, etc) │
└───────────────────────────────────────────────────┘
   │
Postgres (Keycloak DB), Redis (sessions), KMS/HSM keys
```
**Deployment:**
- Keycloak: Containerised (official image) + Postgres. Scaled separately.
- Auth Service: Containerised Spring Boot app; thin orchestration/provisioning layer.

## 4. Multi-Tenancy
- Each tenant corresponds to a Keycloak Realm.
- Tenant resolved via subdomain `{tenant}.youridp.com`, `X-Tenant-Id` header, or client_id mapping.
- Per-tenant config stored in orchestration DB + Keycloak realm settings.
- Per-tenant keys managed by Keycloak; JWKS endpoint per realm:
  - GET /realms/{realm}/protocol/openid-connect/certs
  - GET /realms/{realm}/.well-known/openid-configuration

## 5. Federation Strategy
- Primary OIDC/SAML Provider: Keycloak issues tokens and consumes SAML assertions.
- IdP Connectors via Keycloak:
  - Auth0 (OIDC/SAML) as upstream IdP per realm.
  - Ping, Cognito, Okta, Azure AD, Google via Keycloak identity provider config.

## 6. Local Identity & Manual Users
- Managed by Keycloak (user directory, Argon2id, optional TOTP MFA).
- Orchestration service calls Keycloak Admin API for CRUD operations.
- Self-service flows: register, verify email/phone, forgot/reset password.
- Admin flows: create/delete user, assign roles, lock/unlock, rotate credentials.

## 7. Authorization
- RBAC: Provided by Keycloak roles & groups.
- ABAC: Implemented in orchestration layer or via Keycloak policy SPI + OPA.
- Scopes: Standard (`openid`, `profile`, `email`) + custom (`tenant:admin`, `users:write`).

## 8. Token Model
- Access Token / ID Token: JWT issued by Keycloak per realm (per-tenant signing key).
- Refresh Token: rotating, revocation handled by Keycloak.
- Include `tid` (tenant), `sid` (session), `ver` (token schema version).
- Use Keycloak’s `/protocol/openid-connect/token/introspect` endpoint for introspection.

## 9. Security Hardening
- Argon2id password hashing in Keycloak.
- PKCE enforced, refresh rotation enabled.
- Rate limiting and CORS handled by orchestration service or API Gateway.
- Secrets in AWS KMS/Secrets Manager; no sensitive data logged.

## 10. Audit & Compliance
- Orchestration service captures high-level audit events and streams to SIEM.
- Keycloak logs + admin events exported to central store.
- Tenant-level data residency flags; export/erase APIs for compliance.

## 11. Core Tables (Orchestration DB)
- tenants, apps, idp_configs (mapping to Keycloak realms),
- extra_policies (if ABAC outside Keycloak),
- audit/event tables.

## 12. Critical Flows
- **SSO via Auth0:** tenant resolver → Keycloak realm with Auth0 identity provider → user authenticates at Auth0 → Keycloak issues tokens.
- **Cognito Hybrid:** tenant resolver → Keycloak realm with Cognito identity provider → validate → issue tokens.
- **Manual Login:** admin creates user in Keycloak → login with username/password (+TOTP) → Keycloak issues tokens.

## 13. API Endpoints (Minimum – Orchestration Service)
- POST /admin/tenants (creates Keycloak realm & local metadata)
- POST /admin/apps (registers clients in Keycloak)
- POST /admin/users / GET /admin/users / PATCH / DELETE (proxy to Keycloak)
- POST /admin/idp / GET /admin/idp/{id}/metadata (configure external IdPs for realm)
- POST /admin/idp/{id}/mappings (claim→attribute/role mapping)
- Keycloak endpoints handle `/protocol/openid-connect/authorize`, `/token`, `/logout`, discovery, JWKS.

## 14. Modules
- auth-orchestration: Spring Boot app; tenant resolver, admin APIs, Keycloak Admin integration, rate limit.
- auth-policy: RBAC + ABAC/OPA adapter.
- auth-persistence: JPA/Query DSL for orchestration DB.
- Cross-cutting: Correlation IDs, structured logging, OpenTelemetry, feature flags.

## 15. Non-Functional Requirements
- Containerised (Docker) for both Keycloak and orchestration service.
- Portable (runs anywhere, AWS optional).
- High availability, horizontal scalability.
- OpenTelemetry metrics & traces.
- Automated KMS key rotation with overlap window.
- Chaos tests for DB failover, Keycloak unavailability, key rotation scenarios.

## 16. Roadmap (Implementation Order)
1. Containerise Keycloak + Postgres for local dev.
2. Build orchestration service with tenant resolution, Postgres schema.
3. Integrate orchestration with Keycloak Admin API for realm/client/user provisioning.
4. Configure external IdPs (Auth0, Ping, Cognito) via Keycloak identity providers.
5. Implement admin APIs, audit log, RBAC core.
6. MFA (TOTP) + WebAuthn via Keycloak.
7. OPA integration, ABAC policies, per-tenant feature flags.
8. Ops: KMS key rotation automation, OpenTelemetry, SIEM pipeline, chaos tests.

## 17. Deployment / Local Development
- Docker Compose for Keycloak, Postgres, Redis, orchestration service locally.
- Optionally migrate to local Kubernetes clusters (Kind, k3d, Minikube).
- Production deployments on Kubernetes (EKS/GKE/AKS) for scalability and HA.