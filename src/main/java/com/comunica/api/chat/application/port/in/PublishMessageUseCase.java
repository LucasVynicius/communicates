package com.comunica.api.chat.application.port.in;

import com.comunica.api.chat.domain.MessagePublication;

import java.util.UUID;

public interface PublishMessageUseCase {
    MessagePublication publish(PublishMessageCommand command);

    record PublishMessageCommand(
            UUID channelId,
            String authorSubject,
            String content,
            String idempotencyKey) {
    }
}
