package com.comunica.api.servidores.adapters.in.web;

import com.comunica.api.servidores.application.port.in.CreateWorkspaceUseCase;
import com.comunica.api.servidores.application.port.in.CreateWorkspaceUseCase.CreateWorkspaceCommand;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/workspaces")
public class WorkspaceController {
    private final CreateWorkspaceUseCase createWorkspace;

    public WorkspaceController(CreateWorkspaceUseCase createWorkspace) {
        this.createWorkspace = createWorkspace;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkspaceResponse create(
            @Valid @RequestBody CreateWorkspaceRequest request,
            Principal principal) {
        if (principal == null || principal.getName() == null || principal.getName().isBlank()) {
            throw new UnauthenticatedRequestException();
        }
        var command = new CreateWorkspaceCommand(request.name(), request.template(), principal.getName());
        return WorkspaceResponse.from(createWorkspace.create(command));
    }
}
