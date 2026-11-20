package com.comunica.api.chat.application.service;

import com.comunica.api.chat.application.ChatAccessDeniedException;
import com.comunica.api.chat.application.InvalidMessageRequestException;
import com.comunica.api.chat.application.port.in.PublishMessageUseCase;
import com.comunica.api.chat.application.port.out.ChatRepository;
import com.comunica.api.chat.domain.Message;
import com.comunica.api.chat.domain.OutboxEvent;

public class PublishMessageService implements PublishMessageUseCase {
    private final ChatRepository chatRepository;

    public PublishMessageService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public com.comunica.api.chat.domain.MessagePublication publish(PublishMessageCommand command) {
        if (command.idempotencyKey() == null || command.idempotencyKey().isBlank()
                || command.idempotencyKey().length() > 128) {
            throw new InvalidMessageRequestException();
        }
        var workspaceId = chatRepository.findWorkspaceIdForMember(
                        command.channelId(), command.authorSubject())
                .orElseThrow(ChatAccessDeniedException::new);
        final Message message;
        try {
            message = Message.create(
                    command.channelId(), workspaceId, command.authorSubject(),
                    command.content(), command.idempotencyKey());
        } catch (IllegalArgumentException exception) {
            throw new InvalidMessageRequestException();
        }
        return chatRepository.saveIdempotentlyIfMember(message, OutboxEvent.messageCreated(message));
    }
}
