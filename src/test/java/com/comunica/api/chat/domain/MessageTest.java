package com.comunica.api.chat.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageTest {
    @Test
    void createsMessageWithTimestampAtPostgresPrecision() {
        var createdAt = Instant.parse("2026-11-20T12:30:00.123456789Z");

        var message = new Message(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                "user-1", "hello", "key-1", createdAt);

        assertEquals(Instant.parse("2026-11-20T12:30:00.123456Z"), message.createdAt());
    }

    @Test
    void rejectsBlankContent() {
        assertThrows(IllegalArgumentException.class,
                () -> new Message(
                        UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                        "user-1", "  ", "key-1", Instant.now()));
    }

    @Test
    void rejectsContentAboveMaximumLength() {
        assertThrows(IllegalArgumentException.class,
                () -> new Message(
                        UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                        "user-1", "x".repeat(4_001), "key-1", Instant.now()));
    }

    @Test
    void cursorRoundTripsTimestampAndId() {
        var original = new MessageCursor(
                Instant.parse("2026-11-20T12:30:00.123456Z"), UUID.randomUUID());

        assertEquals(original, MessageCursor.decode(original.encode()));
    }

    @Test
    void rejectsMalformedCursor() {
        assertThrows(InvalidMessageCursorException.class, () -> MessageCursor.decode("bad-cursor"));
    }
}
