package com.comunica.api.servidores.adapters.out.persistence;

import com.comunica.api.servidores.application.port.out.WorkspaceRepository;
import com.comunica.api.servidores.domain.Workspace;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JpaWorkspaceRepository implements WorkspaceRepository {
    private final EntityManager entityManager;

    public JpaWorkspaceRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(Workspace workspace) {
        entityManager.persist(new WorkspaceEntity(
                workspace.id(), workspace.name(), workspace.template().name()));
        workspace.roles().forEach(role -> entityManager.persist(
                new WorkspaceRoleEntity(role.id(), workspace.id(), role.name())));
        workspace.channels().forEach(channel -> entityManager.persist(
                new WorkspaceChannelEntity(
                        channel.id(), workspace.id(), channel.name(), channel.kind().name())));
        entityManager.persist(new WorkspaceMembershipEntity(
                workspace.ownerMembership().id(),
                workspace.id(),
                workspace.ownerMembership().userSubject(),
                workspace.ownerMembership().roleId()));
    }
}
