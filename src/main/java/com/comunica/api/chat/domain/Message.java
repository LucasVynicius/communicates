package com.comunica.api.chat.domain;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

public record Message(
        UUID id,
        UUID channelId,
        UUID workspaceId,
        String authorSubject,
        String content,
        String idempotencyKey,
        Instant createdAt) {

    public Message {
        Objects.requireNonNull(id, "message id is required");
        Objects.requireNonNull(channelId, "channel id is required");
        Objects.requireNonNull(workspaceId, "workspace id is required");
        Objects.requireNonNull(createdAt, "creation time is required");
        if (authorSubject == null || authorSubject.isBlank()) {
            throw new IllegalArgumentException("message author is required");
        }
        if (content == null || content.isBlank() || content.length() > 4_000) {
            throw new IllegalArgumentException("message content must contain 1 to 4000 characters");
        }
        if (idempotencyKey == null || idempotencyKey.isBlank() || idempotencyKey.length() > 128) {
            throw new IllegalArgumentException("idempotency key must contain 1 to 128 characters");
        }
        authorSubject = authorSubject.trim();
        createdAt = createdAt.truncatedTo(ChronoUnit.MICROS);
    }

    public static Message create(
            UUID channelId,
            UUID workspaceId,
            String authorSubject,
            String content,
            String idempotencyKey) {
        return new Message(
                UUID.randomUUID(), channelId, workspaceId, authorSubject, content,
                idempotencyKey, Instant.now().truncatedTo(ChronoUnit.MICROS));
    }
}
