package com.comunica.api.chat.adapters.out.persistence;

import com.comunica.api.chat.domain.OutboxEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
public class OutboxEventEntity {
    @Id
    private UUID id;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;

    @Column(name = "workspace_id", nullable = false)
    private UUID workspaceId;

    @Column(name = "channel_id", nullable = false)
    private UUID channelId;

    @Column(nullable = false)
    private int version;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name = "published_at")
    private Instant publishedAt;

    protected OutboxEventEntity() {
    }

    public OutboxEventEntity(OutboxEvent event) {
        this.id = event.id();
        this.eventType = event.type();
        this.aggregateId = event.aggregateId();
        this.workspaceId = event.workspaceId();
        this.channelId = event.channelId();
        this.version = event.version();
        this.occurredAt = event.occurredAt();
    }
}
