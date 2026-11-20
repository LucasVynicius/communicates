package com.comunica.api.chat.adapters.out.persistence;

import com.comunica.api.chat.application.IdempotencyConflictException;
import com.comunica.api.chat.application.port.in.GetMessageHistoryUseCase;
import com.comunica.api.chat.application.port.in.GetMessageHistoryUseCase.MessageHistoryQuery;
import com.comunica.api.chat.application.port.in.PublishMessageUseCase;
import com.comunica.api.chat.application.port.in.PublishMessageUseCase.PublishMessageCommand;
import com.comunica.api.servidores.application.port.out.WorkspaceRepository;
import com.comunica.api.servidores.domain.Workspace;
import com.comunica.api.servidores.domain.WorkspaceTemplate;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class ChatPersistenceIntegrationTest {
    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private PublishMessageUseCase publishMessage;

    @Autowired
    private GetMessageHistoryUseCase getMessageHistory;

    @Autowired
    private EntityManager entityManager;

    @Test
    void persistsMessageAndOutboxAtomicallyWithIdempotencyAndCursorHistory() {
        var owner = "owner-" + UUID.randomUUID();
        var workspace = Workspace.create("Chat test " + UUID.randomUUID(), WorkspaceTemplate.DEV, owner);
        workspaceRepository.save(workspace);
        var channelId = workspace.channels().getFirst().id();

        var first = publishMessage.publish(new PublishMessageCommand(channelId, owner, "one", "key-1"));
        var replay = publishMessage.publish(new PublishMessageCommand(channelId, owner, "one", "key-1"));
        publishMessage.publish(new PublishMessageCommand(channelId, owner, "two", "key-2"));
        publishMessage.publish(new PublishMessageCommand(channelId, owner, "three", "key-3"));

        assertTrue(first.created());
        assertFalse(replay.created());
        assertEquals(first.message().id(), replay.message().id());
        assertThrows(IdempotencyConflictException.class,
                () -> publishMessage.publish(new PublishMessageCommand(channelId, owner, "changed", "key-1")));

        var newestPage = getMessageHistory.get(new MessageHistoryQuery(channelId, owner, null, 2));
        var previousPage = getMessageHistory.get(new MessageHistoryQuery(
                channelId, owner, newestPage.nextCursor(), 2));
        var messageIds = new HashSet<UUID>();
        newestPage.messages().forEach(message -> messageIds.add(message.id()));
        previousPage.messages().forEach(message -> messageIds.add(message.id()));

        assertEquals(2, newestPage.messages().size());
        assertEquals(1, previousPage.messages().size());
        assertEquals(3, messageIds.size());
        assertEquals(3L, ((Number) entityManager.createNativeQuery("""
                        SELECT count(*) FROM outbox_events WHERE workspace_id = :workspaceId
                        """)
                .setParameter("workspaceId", workspace.id())
                .getSingleResult()).longValue());

        assertThrows(RuntimeException.class,
                () -> getMessageHistory.get(new MessageHistoryQuery(channelId, "outsider", null, 50)));
    }
}
