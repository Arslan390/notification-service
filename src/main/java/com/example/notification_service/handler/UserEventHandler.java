package com.example.notification_service.handler;

import com.example.notification_service.kafka.UserEvent;
import com.example.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics = "user-events-topic")
public class UserEventHandler {

    private final EmailService emailService;

    @Transactional
    @KafkaHandler
    public void handle(UserEvent userEvent) {
        if (userEvent == null) {
            log.warn("Получено пустое событие из Kafka");
            return;
        }

        log.info("Обрабатывается событие: title={}, email={}",
                userEvent.getTitle(), userEvent.getEmail());

        try {
            switch (userEvent.getTitle()) {
                case "CREATED" -> emailService.sendWelcomeEmail(userEvent.getEmail());
                case "DELETED" -> emailService.sendGoodbyeEmail(userEvent.getEmail());
                default -> log.warn("Неизвестная операция в событии: {}", userEvent.getTitle());
            }

        } catch (IllegalArgumentException e) {
            log.warn("Недопустимый тип события {}", userEvent.getTitle(), e);
        } catch (Exception e) {
            log.error("Ошибка при обработке события {} для email: {}",
                    userEvent.getTitle(), userEvent.getEmail(), e);
        }
    }
}
