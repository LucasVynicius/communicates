package com.comunica.api.chat.adapters.out.persistence;

import com.comunica.api.chat.application.ChatAccessDeniedException;
import com.comunica.api.chat.application.IdempotencyConflictException;
import com.comunica.api.chat.application.port.out.ChatRepository;
import com.comunica.api.chat.domain.Message;
import com.comunica.api.chat.domain.MessageCursor;
import com.comunica.api.chat.domain.MessagePublication;
import com.comunica.api.chat.domain.OutboxEvent;
import com.comunica.api.servidores.adapters.out.persistence.WorkspaceChannelEntity;
import com.comunica.api.servidores.adapters.out.persistence.WorkspaceMembershipEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaChatRepository implements ChatRepository {
    private final EntityManager entityManager;

    public JpaChatRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UUID> findWorkspaceIdForMember(UUID channelId, String userSubject) {
        return entityManager.createQuery("""
                        select channel.workspaceId
                        from WorkspaceChannelEntity channel, WorkspaceMembershipEntity membership
                        where channel.workspaceId = membership.workspaceId
                          and channel.id = :channelId
                          and membership.userSubject = :userSubject
                        """, UUID.class)
                .setParameter("channelId", channelId)
                .setParameter("userSubject", userSubject)
                .getResultStream()
                .findFirst();
    }

    @Override
    @Transactional
    public MessagePublication saveIdempotentlyIfMember(Message message, OutboxEvent event) {
        var workspaceId = findWorkspaceIdForMember(message.channelId(), message.authorSubject())
                .filter(message.workspaceId()::equals)
                .orElseThrow(ChatAccessDeniedException::new);

        var inserted = entityManager.createNativeQuery("""
                        INSERT INTO chat_messages
                            (id, workspace_id, channel_id, author_subject, body, idempotency_key, created_at)
                        VALUES
                            (:id, :workspaceId, :channelId, :authorSubject, :body, :idempotencyKey, :createdAt)
                        ON CONFLICT (channel_id, author_subject, idempotency_key) DO NOTHING
                        """)
                .setParameter("id", message.id())
                .setParameter("workspaceId", workspaceId)
                .setParameter("channelId", message.channelId())
                .setParameter("authorSubject", message.authorSubject())
                .setParameter("body", message.content())
                .setParameter("idempotencyKey", message.idempotencyKey())
                .setParameter("createdAt", message.createdAt())
                .executeUpdate();

        if (inserted == 0) {
            var existing = findByIdempotencyKey(message.channelId(), message.authorSubject(), message.idempotencyKey());
            if (!existing.content().equals(message.content())) {
                throw new IdempotencyConflictException();
            }
            return new MessagePublication(existing, false);
        }

        entityManager.persist(new OutboxEventEntity(event));
        return new MessagePublication(message, true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> findPreviousMessagesIfMember(
            UUID channelId, String userSubject, MessageCursor before, int limit) {
        findWorkspaceIdForMember(channelId, userSubject)
                .orElseThrow(ChatAccessDeniedException::new);

        String jpql = "select message from ChatMessageEntity message where message.channelId = :channelId";
        if (before != null) {
            jpql += " and (message.createdAt < :createdAt or "
                    + "(message.createdAt = :createdAt and message.id < :messageId))";
        }
        jpql += " order by message.createdAt desc, message.id desc";

        TypedQuery<ChatMessageEntity> query = entityManager.createQuery(jpql, ChatMessageEntity.class)
                .setParameter("channelId", channelId)
                .setMaxResults(limit);
        if (before != null) {
            query.setParameter("createdAt", before.createdAt());
            query.setParameter("messageId", before.messageId());
        }
        return query.getResultList().stream().map(ChatMessageEntity::toDomain).toList();
    }

    private Message findByIdempotencyKey(UUID channelId, String authorSubject, String key) {
        return entityManager.createQuery("""
                        select message from ChatMessageEntity message
                        where message.channelId = :channelId
                          and message.authorSubject = :authorSubject
                          and message.idempotencyKey = :idempotencyKey
                        """, ChatMessageEntity.class)
                .setParameter("channelId", channelId)
                .setParameter("authorSubject", authorSubject)
                .setParameter("idempotencyKey", key)
                .getSingleResult()
                .toDomain();
    }
}
