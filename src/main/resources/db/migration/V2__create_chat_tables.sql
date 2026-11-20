ALTER TABLE workspace_channels
    ADD CONSTRAINT uq_workspace_channels_id_workspace UNIQUE (id, workspace_id);

CREATE TABLE chat_messages (
    id UUID PRIMARY KEY,
    workspace_id UUID NOT NULL REFERENCES workspaces (id) ON DELETE CASCADE,
    channel_id UUID NOT NULL,
    author_subject VARCHAR(255) NOT NULL,
    body VARCHAR(4000) NOT NULL,
    idempotency_key VARCHAR(128) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_chat_message_channel_workspace
        FOREIGN KEY (channel_id, workspace_id)
        REFERENCES workspace_channels (id, workspace_id) ON DELETE CASCADE,
    CONSTRAINT uq_chat_message_idempotency
        UNIQUE (channel_id, author_subject, idempotency_key)
);

CREATE INDEX ix_chat_messages_history
    ON chat_messages (channel_id, created_at DESC, id DESC);

CREATE TABLE outbox_events (
    id UUID PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    aggregate_id UUID NOT NULL REFERENCES chat_messages (id) ON DELETE CASCADE,
    workspace_id UUID NOT NULL REFERENCES workspaces (id) ON DELETE CASCADE,
    channel_id UUID NOT NULL,
    version INTEGER NOT NULL,
    occurred_at TIMESTAMPTZ NOT NULL,
    published_at TIMESTAMPTZ,
    CONSTRAINT fk_outbox_channel_workspace
        FOREIGN KEY (channel_id, workspace_id)
        REFERENCES workspace_channels (id, workspace_id) ON DELETE CASCADE
);

CREATE INDEX ix_outbox_events_pending
    ON outbox_events (occurred_at)
    WHERE published_at IS NULL;
