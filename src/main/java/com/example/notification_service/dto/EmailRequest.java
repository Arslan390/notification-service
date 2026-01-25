package com.example.notification_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на отправку email")
public class EmailRequest {

    @Schema(description = "Адрес получателя", example = "user@example.com")
    private String to;

    @Schema(description = "Тема письма", example = "Приветствие")
    private String subject;

    @Schema(description = "Текст письма", example = "Здравствуйте, это тестовое сообщение.")
    private String text;
}