package com.comunica.api.chat.application.service;

import com.comunica.api.chat.application.ChatAccessDeniedException;
import com.comunica.api.chat.application.InvalidMessageRequestException;
import com.comunica.api.chat.application.port.in.GetMessageHistoryUseCase;
import com.comunica.api.chat.application.port.out.ChatRepository;
import com.comunica.api.chat.domain.MessageCursor;

import java.util.ArrayList;
import java.util.List;

public class GetMessageHistoryService implements GetMessageHistoryUseCase {
    private static final int MAX_LIMIT = 100;

    private final ChatRepository chatRepository;

    public GetMessageHistoryService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public MessageHistory get(MessageHistoryQuery query) {
        if (query.limit() < 1 || query.limit() > MAX_LIMIT) {
            throw new InvalidMessageRequestException();
        }
        if (chatRepository.findWorkspaceIdForMember(query.channelId(), query.readerSubject()).isEmpty()) {
            throw new ChatAccessDeniedException();
        }

        MessageCursor cursor = null;
        if (query.cursor() != null) {
            cursor = MessageCursor.decode(query.cursor());
        }

        var descending = new ArrayList<>(chatRepository.findPreviousMessagesIfMember(
                query.channelId(), query.readerSubject(), cursor, query.limit() + 1));
        var hasMore = descending.size() > query.limit();
        if (hasMore) {
            descending.removeLast();
        }
        java.util.Collections.reverse(descending);
        var nextCursor = hasMore && !descending.isEmpty()
                ? new MessageCursor(
                        descending.getFirst().createdAt(), descending.getFirst().id()).encode()
                : null;
        return new MessageHistory(List.copyOf(descending), nextCursor);
    }

}
