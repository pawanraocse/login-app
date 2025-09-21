package com.learning.loginapp.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "apps", uniqueConstraints = @UniqueConstraint(columnNames = {"tenant_id", "client_id"}))
public class App {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Column(name = "client_id", nullable = false, length = 100)
    private String clientId;

    @Column(length = 150)
    private String name;

    @Column(length = 32)
    private String type;

    @ElementCollection
    @Column(name = "redirect_uris")
    private List<String> redirectUris;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    // Getters, setters, equals, hashCode, toString
}
