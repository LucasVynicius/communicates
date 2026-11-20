package com.comunica.api.chat.application.port.out;

import com.comunica.api.chat.domain.Message;
import com.comunica.api.chat.domain.MessageCursor;
import com.comunica.api.chat.domain.MessagePublication;
import com.comunica.api.chat.domain.OutboxEvent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository {
    Optional<UUID> findWorkspaceIdForMember(UUID channelId, String userSubject);

    MessagePublication saveIdempotentlyIfMember(Message message, OutboxEvent event);

    List<Message> findPreviousMessagesIfMember(
            UUID channelId, String userSubject, MessageCursor before, int limit);
}
