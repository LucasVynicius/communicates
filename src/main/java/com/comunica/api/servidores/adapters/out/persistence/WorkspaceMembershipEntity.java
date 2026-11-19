package com.comunica.api.servidores.adapters.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "workspace_memberships")
public class WorkspaceMembershipEntity {
    @Id
    private UUID id;

    @Column(name = "workspace_id", nullable = false)
    private UUID workspaceId;

    @Column(name = "user_subject", nullable = false, length = 255)
    private String userSubject;

    @Column(name = "role_id", nullable = false)
    private UUID roleId;

    protected WorkspaceMembershipEntity() {
    }

    public WorkspaceMembershipEntity(UUID id, UUID workspaceId, String userSubject, UUID roleId) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.userSubject = userSubject;
        this.roleId = roleId;
    }
}
