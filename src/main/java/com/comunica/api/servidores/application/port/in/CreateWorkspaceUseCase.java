package com.comunica.api.servidores.application.port.in;

import com.comunica.api.servidores.domain.Workspace;
import com.comunica.api.servidores.domain.WorkspaceTemplate;

public interface CreateWorkspaceUseCase {
    Workspace create(CreateWorkspaceCommand command);

    record CreateWorkspaceCommand(String name, WorkspaceTemplate template, String ownerSubject) {
    }
}
