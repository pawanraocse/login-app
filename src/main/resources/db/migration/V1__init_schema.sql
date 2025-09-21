CREATE TABLE tenants (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         name VARCHAR(100) NOT NULL UNIQUE,
                         realm VARCHAR(100) NOT NULL UNIQUE,
                         display_name VARCHAR(150),
                         status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
                         data_residency VARCHAR(32),
                         created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
                         updated_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- apps: registered client apps per tenant
CREATE TABLE apps (
                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                      tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
                      client_id VARCHAR(100) NOT NULL,
                      name VARCHAR(150),
                      type VARCHAR(32),
                      redirect_uris TEXT[],
                      created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
                      updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
                      UNIQUE(tenant_id, client_id)
);

-- idp_configs: external IdP configs per tenant/realm
CREATE TABLE idp_configs (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
                             idp_type VARCHAR(32) NOT NULL, -- e.g., 'auth0', 'okta', 'saml', etc.
                             config JSONB NOT NULL,
                             enabled BOOLEAN DEFAULT true,
                             created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
                             updated_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- extra_policies: for ABAC/OPA, optional
CREATE TABLE extra_policies (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                tenant_id UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
                                policy_type VARCHAR(32) NOT NULL, -- e.g., 'opa', 'custom'
                                policy JSONB NOT NULL,
                                enabled BOOLEAN DEFAULT true,
                                created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
                                updated_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- audit_events: append-only audit log
CREATE TABLE audit_events (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              tenant_id UUID,
                              event_type VARCHAR(64) NOT NULL,
                              principal VARCHAR(100),
                              details JSONB,
                              created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- app_redirect_uris: join table for App.redirectUris (@ElementCollection)
CREATE TABLE app_redirect_uris (
    app_id UUID NOT NULL REFERENCES apps(id) ON DELETE CASCADE,
    redirect_uris VARCHAR(255) NOT NULL
);

-- Indexes for performance
CREATE INDEX idx_tenants_realm ON tenants(realm);
CREATE INDEX idx_apps_tenant_id ON apps(tenant_id);
CREATE INDEX idx_idp_configs_tenant_id ON idp_configs(tenant_id);
CREATE INDEX idx_extra_policies_tenant_id ON extra_policies(tenant_id);
CREATE INDEX idx_audit_events_tenant_id ON audit_events(tenant_id);
CREATE INDEX idx_app_redirect_uris_app_id ON app_redirect_uris(app_id);
