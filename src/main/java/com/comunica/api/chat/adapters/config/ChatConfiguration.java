package com.comunica.api.chat.adapters.config;

import com.comunica.api.chat.application.port.in.GetMessageHistoryUseCase;
import com.comunica.api.chat.application.port.in.PublishMessageUseCase;
import com.comunica.api.chat.application.port.out.ChatRepository;
import com.comunica.api.chat.application.service.GetMessageHistoryService;
import com.comunica.api.chat.application.service.PublishMessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfiguration {
    @Bean
    PublishMessageUseCase publishMessageUseCase(ChatRepository chatRepository) {
        return new PublishMessageService(chatRepository);
    }

    @Bean
    GetMessageHistoryUseCase getMessageHistoryUseCase(ChatRepository chatRepository) {
        return new GetMessageHistoryService(chatRepository);
    }
}
