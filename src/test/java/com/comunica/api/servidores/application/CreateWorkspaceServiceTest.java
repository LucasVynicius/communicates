package com.comunica.api.servidores.application;

import com.comunica.api.servidores.application.port.in.CreateWorkspaceUseCase.CreateWorkspaceCommand;
import com.comunica.api.servidores.application.port.out.WorkspaceRepository;
import com.comunica.api.servidores.application.service.CreateWorkspaceService;
import com.comunica.api.servidores.domain.Workspace;
import com.comunica.api.servidores.domain.WorkspaceTemplate;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreateWorkspaceServiceTest {
    @Test
    void createsWorkspaceAndPersistsItThroughTheOutputPort() {
        var persisted = new AtomicReference<Workspace>();
        WorkspaceRepository repository = persisted::set;
        var service = new CreateWorkspaceService(repository);

        var created = service.create(new CreateWorkspaceCommand("Team", WorkspaceTemplate.DEV, "user-1"));

        assertNotNull(persisted.get());
        assertEquals(created, persisted.get());
        assertEquals("Team", created.name());
        assertEquals("user-1", created.ownerSubject());
    }
}
