package com.comunica.api.servidores.domain;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record Workspace(
        UUID id,
        String name,
        WorkspaceTemplate template,
        String ownerSubject,
        List<Role> roles,
        List<Channel> channels,
        Membership ownerMembership) {

    public Workspace {
        Objects.requireNonNull(id, "id is required");
        Objects.requireNonNull(template, "template is required");
        Objects.requireNonNull(ownerMembership, "owner membership is required");
        if (name == null || name.isBlank() || name.trim().length() > 100) {
            throw new IllegalArgumentException("name must contain between 1 and 100 characters");
        }
        if (ownerSubject == null || ownerSubject.isBlank()) {
            throw new IllegalArgumentException("owner is required");
        }
        name = name.trim();
        ownerSubject = ownerSubject.trim();
        roles = List.copyOf(Objects.requireNonNull(roles, "roles are required"));
        channels = List.copyOf(Objects.requireNonNull(channels, "channels are required"));
        if (roles.isEmpty() || channels.isEmpty()) {
            throw new IllegalArgumentException("initial roles and channels are required");
        }
    }

    public static Workspace create(String name, WorkspaceTemplate template, String ownerSubject) {
        Objects.requireNonNull(template, "template is required");
        if (ownerSubject == null || ownerSubject.isBlank()) {
            throw new IllegalArgumentException("owner is required");
        }
        ownerSubject = ownerSubject.trim();
        var adminRole = new Role(UUID.randomUUID(), "Admin");
        var memberRole = new Role(UUID.randomUUID(), "Member");
        var channels = List.of(
                new Channel(UUID.randomUUID(), "general", ChannelKind.TEXT),
                new Channel(UUID.randomUUID(), "voice", ChannelKind.VOICE));
        var membership = new Membership(UUID.randomUUID(), ownerSubject, adminRole.id());

        return new Workspace(
                UUID.randomUUID(), name, template, ownerSubject,
                List.of(adminRole, memberRole), channels, membership);
    }

    public record Role(UUID id, String name) {
        public Role {
            Objects.requireNonNull(id, "role id is required");
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("role name is required");
            }
        }
    }

    public record Channel(UUID id, String name, ChannelKind kind) {
        public Channel {
            Objects.requireNonNull(id, "channel id is required");
            Objects.requireNonNull(kind, "channel kind is required");
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("channel name is required");
            }
        }
    }

    public record Membership(UUID id, String userSubject, UUID roleId) {
        public Membership {
            Objects.requireNonNull(id, "membership id is required");
            Objects.requireNonNull(roleId, "membership role is required");
            if (userSubject == null || userSubject.isBlank()) {
                throw new IllegalArgumentException("membership user is required");
            }
        }
    }
}
