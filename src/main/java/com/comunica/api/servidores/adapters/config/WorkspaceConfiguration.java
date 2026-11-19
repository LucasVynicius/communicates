package com.comunica.api.servidores.adapters.config;

import com.comunica.api.servidores.application.port.in.CreateWorkspaceUseCase;
import com.comunica.api.servidores.application.port.out.WorkspaceRepository;
import com.comunica.api.servidores.application.service.CreateWorkspaceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkspaceConfiguration {
    @Bean
    CreateWorkspaceUseCase createWorkspaceUseCase(WorkspaceRepository workspaceRepository) {
        return new CreateWorkspaceService(workspaceRepository);
    }
}
