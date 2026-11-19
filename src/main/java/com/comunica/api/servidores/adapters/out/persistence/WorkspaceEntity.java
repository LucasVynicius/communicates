package com.comunica.api.servidores.adapters.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "workspaces")
public class WorkspaceEntity {
    @Id
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 20)
    private String template;

    protected WorkspaceEntity() {
    }

    public WorkspaceEntity(UUID id, String name, String template) {
        this.id = id;
        this.name = name;
        this.template = template;
    }
}
