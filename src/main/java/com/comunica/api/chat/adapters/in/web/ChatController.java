package com.comunica.api.chat.adapters.in.web;

import com.comunica.api.chat.application.port.in.GetMessageHistoryUseCase;
import com.comunica.api.chat.application.port.in.GetMessageHistoryUseCase.MessageHistoryQuery;
import com.comunica.api.chat.application.port.in.PublishMessageUseCase;
import com.comunica.api.chat.application.port.in.PublishMessageUseCase.PublishMessageCommand;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/channels/{channelId}/messages")
public class ChatController {
    private final PublishMessageUseCase publishMessage;
    private final GetMessageHistoryUseCase getMessageHistory;

    public ChatController(
            PublishMessageUseCase publishMessage,
            GetMessageHistoryUseCase getMessageHistory) {
        this.publishMessage = publishMessage;
        this.getMessageHistory = getMessageHistory;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> publish(
            @PathVariable UUID channelId,
            @RequestHeader(name = "Idempotency-Key", required = false) String idempotencyKey,
            @Valid @RequestBody PublishMessageRequest request,
            Principal principal) {
        var actor = requirePrincipal(principal);
        var publication = publishMessage.publish(new PublishMessageCommand(
                channelId, actor, request.content(), idempotencyKey));
        return ResponseEntity.status(publication.created() ? HttpStatus.CREATED : HttpStatus.OK)
                .body(MessageResponse.from(publication.message()));
    }

    @GetMapping
    public MessageHistoryResponse history(
            @PathVariable UUID channelId,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "50") int limit,
            Principal principal) {
        var actor = requirePrincipal(principal);
        return MessageHistoryResponse.from(getMessageHistory.get(
                new MessageHistoryQuery(channelId, actor, cursor, limit)));
    }

    private String requirePrincipal(Principal principal) {
        if (principal == null || principal.getName() == null || principal.getName().isBlank()) {
            throw new ChatUnauthenticatedRequestException();
        }
        return principal.getName();
    }
}
