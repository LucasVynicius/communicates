package com.comunica.api.servidores.adapters.in.web;

import com.comunica.api.servidores.application.port.in.CreateWorkspaceUseCase.CreateWorkspaceCommand;
import com.comunica.api.servidores.application.service.CreateWorkspaceService;
import com.comunica.api.servidores.domain.Workspace;
import com.comunica.api.servidores.domain.WorkspaceTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WorkspaceControllerTest {
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        var saved = new ArrayList<Workspace>();
        var service = new CreateWorkspaceService(saved::add);
        mockMvc = MockMvcBuilders.standaloneSetup(new WorkspaceController(service))
                .setControllerAdvice(new WorkspaceExceptionHandler())
                .build();
    }

    @Test
    void createsWorkspaceForAuthenticatedPrincipal() throws Exception {
        mockMvc.perform(post("/api/v1/workspaces")
                        .principal((Principal) () -> "user-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Team","template":"DEV"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Team"))
                .andExpect(jsonPath("$.template").value("DEV"))
                .andExpect(jsonPath("$.roles.length()").value(2))
                .andExpect(jsonPath("$.channels.length()").value(2));
    }

    @Test
    void rejectsMissingAuthentication() throws Exception {
        mockMvc.perform(post("/api/v1/workspaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Team","template":"DEV"}
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("unauthorized"));
    }

    @Test
    void rejectsInvalidRequest() throws Exception {
        mockMvc.perform(post("/api/v1/workspaces")
                        .principal((Principal) () -> "user-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":" ","template":"DEV"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("invalid_request"));
    }

    @Test
    void acceptsAllInitialTemplates() throws Exception {
        for (var template : List.of("DEV", "CREATOR", "COMMUNITY", "COMPANY")) {
            mockMvc.perform(post("/api/v1/workspaces")
                            .principal((Principal) () -> "user-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {"name":"Team","template":"%s"}
                                    """.formatted(template)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.template").value(template));
        }
    }
}
