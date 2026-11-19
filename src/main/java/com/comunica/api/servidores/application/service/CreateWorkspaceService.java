package com.comunica.api.servidores.application.service;

import com.comunica.api.servidores.application.port.in.CreateWorkspaceUseCase;
import com.comunica.api.servidores.application.port.out.WorkspaceRepository;
import com.comunica.api.servidores.domain.Workspace;

public class CreateWorkspaceService implements CreateWorkspaceUseCase {
    private final WorkspaceRepository workspaceRepository;

    public CreateWorkspaceService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public Workspace create(CreateWorkspaceCommand command) {
        var workspace = Workspace.create(command.name(), command.template(), command.ownerSubject());
        workspaceRepository.save(workspace);
        return workspace;
    }
}
