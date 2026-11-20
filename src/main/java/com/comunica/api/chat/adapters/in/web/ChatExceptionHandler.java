package com.comunica.api.chat.adapters.in.web;

import com.comunica.api.chat.application.ChatAccessDeniedException;
import com.comunica.api.chat.application.IdempotencyConflictException;
import com.comunica.api.chat.application.InvalidMessageRequestException;
import com.comunica.api.chat.domain.InvalidMessageCursorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ChatExceptionHandler {
    @ExceptionHandler(ChatUnauthenticatedRequestException.class)
    public ResponseEntity<ApiError> unauthenticated() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError("unauthorized", "Authentication is required."));
    }

    @ExceptionHandler(ChatAccessDeniedException.class)
    public ResponseEntity<ApiError> forbidden() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiError("forbidden", "The user is not a member of the workspace."));
    }

    @ExceptionHandler(IdempotencyConflictException.class)
    public ResponseEntity<ApiError> idempotencyConflict() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("idempotency_conflict", "The key was already used with different content."));
    }

    @ExceptionHandler({
            InvalidMessageRequestException.class,
            InvalidMessageCursorException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            MissingRequestHeaderException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ApiError> invalidRequest(Exception exception) {
        return ResponseEntity.badRequest()
                .body(new ApiError("invalid_request", "The message request is invalid."));
    }

    public record ApiError(String code, String message) {
    }
}
