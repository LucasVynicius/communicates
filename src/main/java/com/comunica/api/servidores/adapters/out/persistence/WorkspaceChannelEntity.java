package com.comunica.api.servidores.adapters.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "workspace_channels")
public class WorkspaceChannelEntity {
    @Id
    private UUID id;

    @Column(name = "workspace_id", nullable = false)
    private UUID workspaceId;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false, length = 20)
    private String kind;

    protected WorkspaceChannelEntity() {
    }

    public WorkspaceChannelEntity(UUID id, UUID workspaceId, String name, String kind) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.name = name;
        this.kind = kind;
    }
}
