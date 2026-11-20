package com.comunica.api.chat.application;

import com.comunica.api.chat.application.port.in.GetMessageHistoryUseCase.MessageHistoryQuery;
import com.comunica.api.chat.application.port.in.PublishMessageUseCase.PublishMessageCommand;
import com.comunica.api.chat.application.service.GetMessageHistoryService;
import com.comunica.api.chat.application.service.PublishMessageService;
import com.comunica.api.chat.application.port.out.ChatRepository;
import com.comunica.api.chat.domain.Message;
import com.comunica.api.chat.domain.MessageCursor;
import com.comunica.api.chat.domain.MessagePublication;
import com.comunica.api.chat.domain.OutboxEvent;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChatApplicationServiceTest {
    private final UUID workspaceId = UUID.randomUUID();
    private final UUID channelId = UUID.randomUUID();

    @Test
    void deniesPublishingWithoutWorkspaceMembership() {
        var service = new PublishMessageService(new InMemoryChatRepository(false));

        assertThrows(ChatAccessDeniedException.class,
                () -> service.publish(new PublishMessageCommand(channelId, "outsider", "hello", "key-1")));
    }

    @Test
    void rejectsMissingIdempotencyKey() {
        var service = new PublishMessageService(new InMemoryChatRepository(true));

        assertThrows(InvalidMessageRequestException.class,
                () -> service.publish(new PublishMessageCommand(channelId, "member", "hello", null)));
    }

    @Test
    void returnsPreviousPageInChronologicalOrder() {
        var repository = new InMemoryChatRepository(true);
        for (int second = 1; second <= 3; second++) {
            repository.messages.add(new Message(
                    UUID.randomUUID(), channelId, workspaceId, "member", "m" + second,
                    "key-" + second, Instant.parse("2026-11-20T12:00:0" + second + "Z")));
        }
        var service = new GetMessageHistoryService(repository);

        var newestPage = service.get(new MessageHistoryQuery(channelId, "member", null, 2));
        var previousPage = service.get(new MessageHistoryQuery(
                channelId, "member", newestPage.nextCursor(), 2));

        assertEquals(List.of("m2", "m3"), newestPage.messages().stream().map(Message::content).toList());
        assertEquals(List.of("m1"), previousPage.messages().stream().map(Message::content).toList());
        assertEquals(null, previousPage.nextCursor());
    }

    @Test
    void rejectsPageLimitAboveMaximum() {
        var service = new GetMessageHistoryService(new InMemoryChatRepository(true));

        assertThrows(InvalidMessageRequestException.class,
                () -> service.get(new MessageHistoryQuery(channelId, "member", null, 101)));
    }

    private final class InMemoryChatRepository implements ChatRepository {
        private final boolean member;
        private final List<Message> messages = new ArrayList<>();

        private InMemoryChatRepository(boolean member) {
            this.member = member;
        }

        @Override
        public Optional<UUID> findWorkspaceIdForMember(UUID requestedChannel, String userSubject) {
            return member && channelId.equals(requestedChannel) && "member".equals(userSubject)
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
            var ordered = messages.stream()
                    .filter(message -> message.channelId().equals(requestedChannel))
                    .filter(message -> before == null
                            || message.createdAt().isBefore(before.createdAt())
                            || (message.createdAt().equals(before.createdAt())
                            && message.id().compareTo(before.messageId()) < 0))
                    .sorted(Comparator.comparing(Message::createdAt)
                            .thenComparing(Message::id).reversed())
                    .limit(limit)
                    .toList();
            return ordered;
        }
    }
}
