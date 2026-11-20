package com.comunica.api.chat.adapters.in.web;

import com.comunica.api.chat.domain.Message;

import java.time.Instant;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        UUID channelId,
        String authorSubject,
        String content,
        Instant createdAt) {

    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.id(), message.channelId(), message.authorSubject(),
                message.content(), message.createdAt());
    }
}
