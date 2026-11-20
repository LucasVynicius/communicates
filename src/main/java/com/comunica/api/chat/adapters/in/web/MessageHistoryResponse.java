package com.comunica.api.chat.adapters.in.web;

import com.comunica.api.chat.application.port.in.GetMessageHistoryUseCase.MessageHistory;

import java.util.List;

public record MessageHistoryResponse(List<MessageResponse> messages, String nextCursor) {
    public static MessageHistoryResponse from(MessageHistory history) {
        return new MessageHistoryResponse(
                history.messages().stream().map(MessageResponse::from).toList(), history.nextCursor());
    }
}
