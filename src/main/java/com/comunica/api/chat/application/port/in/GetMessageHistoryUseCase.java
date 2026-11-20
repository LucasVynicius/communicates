package com.comunica.api.chat.application.port.in;

import com.comunica.api.chat.domain.Message;

import java.util.List;
import java.util.UUID;

public interface GetMessageHistoryUseCase {
    MessageHistory get(MessageHistoryQuery query);

    record MessageHistoryQuery(UUID channelId, String readerSubject, String cursor, int limit) {
    }

    record MessageHistory(List<Message> messages, String nextCursor) {
    }
}
