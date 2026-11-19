package com.comunica.api.servidores.adapters.in.web;

import com.comunica.api.servidores.domain.WorkspaceTemplate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateWorkspaceRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull WorkspaceTemplate template) {
}
