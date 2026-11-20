package com.comunica.api.chat.domain;

import java.util.UUID;

public record OutboxEvent(
        UUID id,
        String type,
        UUID aggregateId,
        UUID workspaceId,
        UUID channelId,
        int version,
        java.time.Instant occurredAt) {

    public static OutboxEvent messageCreated(Message message) {
        return new OutboxEvent(
                UUID.randomUUID(), "message.created", message.id(), message.workspaceId(),
                message.channelId(), 1, message.createdAt());
    }
}
