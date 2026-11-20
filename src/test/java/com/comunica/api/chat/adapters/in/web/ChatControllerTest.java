package com.comunica.api.chat.adapters.in.web;

import com.comunica.api.chat.application.port.out.ChatRepository;
import com.comunica.api.chat.application.service.GetMessageHistoryService;
import com.comunica.api.chat.application.service.PublishMessageService;
import com.comunica.api.chat.domain.Message;
import com.comunica.api.chat.domain.MessageCursor;
import com.comunica.api.chat.domain.MessagePublication;
import com.comunica.api.chat.domain.OutboxEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChatControllerTest {
    private final UUID channelId = UUID.randomUUID();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        var repository = new InMemoryChatRepository();
        mockMvc = MockMvcBuilders.standaloneSetup(new ChatController(
                        new PublishMessageService(repository), new GetMessageHistoryService(repository)))
                .setControllerAdvice(new ChatExceptionHandler())
                .build();
    }

    @Test
    void publishesAnAuthenticatedMessage() throws Exception {
        mockMvc.perform(post(path())
                        .principal((Principal) () -> "member")
                        .header("Idempotency-Key", "request-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"content":"hello"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("hello"))
                .andExpect(jsonPath("$.authorSubject").value("member"));
    }

    @Test
    void rejectsPublicationWithoutIdempotencyKey() throws Exception {
        mockMvc.perform(post(path())
                        .principal((Principal) () -> "member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"content":"hello"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("invalid_request"));
    }

    @Test
    void deniesNonMember() throws Exception {
        mockMvc.perform(post(path())
                        .principal((Principal) () -> "outsider")
                        .header("Idempotency-Key", "request-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"content":"hello"}
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void retrievesMessagesWithCursor() throws Exception {
        mockMvc.perform(post(path())
                        .principal((Principal) () -> "member")
                        .header("Idempotency-Key", "request-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"content":"hello"}
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get(path()).principal((Principal) () -> "member"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messages.length()").value(1))
                .andExpect(jsonPath("$.messages[0].content").value("hello"));
    }

    private String path() {
        return "/api/v1/channels/" + channelId + "/messages";
    }

    private final class InMemoryChatRepository implements ChatRepository {
        private final UUID workspaceId = UUID.randomUUID();
        private final List<Message> messages = new ArrayList<>();

        @Override
        public Optional<UUID> findWorkspaceIdForMember(UUID requestedChannel, String userSubject) {
            return channelId.equals(requestedChannel) && "member".equals(userSubject)
                    ? Optional.of(workspaceId)
                    : Optional.empty();
        }

        @Override
        public MessagePublication saveIdempotentlyIfMember(Message message, OutboxEvent event) {
            messages.add(message);
            return new MessagePublication(message, true);
        }

        @Override
        public List<Message> findPreviousMessagesIfMember(
                UUID requestedChannel, String userSubject, MessageCursor before, int limit) {
            return messages.stream()
                    .filter(message -> message.channelId().equals(requestedChannel))
                    .sorted(Comparator.comparing(Message::createdAt)
                            .thenComparing(Message::id).reversed())
                    .limit(limit)
                    .toList();
        }
    }
}
