package com.comunica.api.chat.adapters.out.persistence;

import com.comunica.api.chat.domain.Message;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "chat_messages")
public class ChatMessageEntity {
    @Id
    private UUID id;

    @Column(name = "workspace_id", nullable = false)
    private UUID workspaceId;

    @Column(name = "channel_id", nullable = false)
    private UUID channelId;

    @Column(name = "author_subject", nullable = false, length = 255)
    private String authorSubject;

    @Column(nullable = false, length = 4_000)
    private String body;

    @Column(name = "idempotency_key", nullable = false, length = 128)
    private String idempotencyKey;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected ChatMessageEntity() {
    }

    public Message toDomain() {
        return new Message(id, channelId, workspaceId, authorSubject, body, idempotencyKey, createdAt);
    }
}
