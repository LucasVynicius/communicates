package com.comunica.api.servidores.adapters.in.web;

import com.comunica.api.servidores.domain.Workspace;
import com.comunica.api.servidores.domain.WorkspaceTemplate;

import java.util.List;
import java.util.UUID;

public record WorkspaceResponse(
        UUID id,
        String name,
        WorkspaceTemplate template,
        List<String> roles,
        List<ChannelResponse> channels) {

    public static WorkspaceResponse from(Workspace workspace) {
        return new WorkspaceResponse(
                workspace.id(),
                workspace.name(),
                workspace.template(),
                workspace.roles().stream().map(Workspace.Role::name).toList(),
                workspace.channels().stream()
                        .map(channel -> new ChannelResponse(channel.name(), channel.kind().name()))
                        .toList());
    }

    public record ChannelResponse(String name, String kind) {
    }
}
