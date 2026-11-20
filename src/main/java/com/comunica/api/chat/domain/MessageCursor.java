package com.comunica.api.chat.domain;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

public record MessageCursor(Instant createdAt, UUID messageId) {
    public String encode() {
        var value = createdAt.toString() + "|" + messageId;
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    public static MessageCursor decode(String encoded) {
        try {
            var decoded = new String(Base64.getUrlDecoder().decode(encoded), StandardCharsets.UTF_8);
            var parts = decoded.split("\\|", -1);
            if (parts.length != 2) {
                throw new IllegalArgumentException("invalid cursor fields");
            }
            return new MessageCursor(Instant.parse(parts[0]), UUID.fromString(parts[1]));
        } catch (RuntimeException exception) {
            throw new InvalidMessageCursorException();
        }
    }
}
