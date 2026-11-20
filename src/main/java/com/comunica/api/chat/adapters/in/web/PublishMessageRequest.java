package com.comunica.api.chat.adapters.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublishMessageRequest(@NotBlank @Size(max = 4_000) String content) {
}
