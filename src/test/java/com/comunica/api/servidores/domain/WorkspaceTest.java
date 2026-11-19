package com.comunica.api.servidores.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WorkspaceTest {
    @Test
    void createsInitialRolesChannelsAndOwnerMembership() {
        var workspace = Workspace.create("  Comunica  ", WorkspaceTemplate.COMMUNITY, "user-1");

        assertEquals("Comunica", workspace.name());
        assertEquals("user-1", workspace.ownerMembership().userSubject());
        assertEquals(2, workspace.roles().size());
        assertEquals("Admin", workspace.roles().getFirst().name());
        assertEquals(workspace.roles().getFirst().id(), workspace.ownerMembership().roleId());
        assertEquals(2, workspace.channels().size());
        assertEquals(ChannelKind.TEXT, workspace.channels().getFirst().kind());
        assertEquals(ChannelKind.VOICE, workspace.channels().getLast().kind());
    }

    @Test
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> Workspace.create("  ", WorkspaceTemplate.DEV, "user-1"));
    }

    @Test
    void rejectsNameLongerThanOneHundredCharacters() {
        assertThrows(IllegalArgumentException.class,
                () -> Workspace.create("x".repeat(101), WorkspaceTemplate.DEV, "user-1"));
    }

    @Test
    void rejectsMissingOwner() {
        assertThrows(IllegalArgumentException.class,
                () -> Workspace.create("Team", WorkspaceTemplate.DEV, " "));
    }
}
