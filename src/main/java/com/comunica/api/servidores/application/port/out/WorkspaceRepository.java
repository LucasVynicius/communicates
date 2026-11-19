package com.comunica.api.servidores.application.port.out;

import com.comunica.api.servidores.domain.Workspace;

public interface WorkspaceRepository {
    void save(Workspace workspace);
}
