package com.comunica.api.servidores.adapters.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "workspace_roles")
public class WorkspaceRoleEntity {
    @Id
    private UUID id;

    @Column(name = "workspace_id", nullable = false)
    private UUID workspaceId;

    @Column(nullable = false, length = 50)
    private String name;

    protected WorkspaceRoleEntity() {
    }

    public WorkspaceRoleEntity(UUID id, UUID workspaceId, String name) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.name = name;
    }
}
