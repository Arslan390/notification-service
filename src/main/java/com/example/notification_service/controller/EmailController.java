package com.example.notification_service.controller;

import com.example.notification_service.dto.EmailRequest;
import com.example.notification_service.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@Tag(name = "Email Notifications", description = "API для отправки email‑уведомлений")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    @Operation(
            summary = "Отправить email",
            description = "Отправляет произвольное email‑сообщение на указанный адрес",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Email отправлен успешно",
                            content = @Content(schema = @Schema(implementation = EntityModel.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные в запросе (например, пустой email)"),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ошибка отправки email (SMTP, сеть и т.п.)")
            }
    )
    public EntityModel<String> sendEmail(@RequestBody EmailRequest request) {
        emailService.sendCustomEmail(request);
        return EntityModel.of("Email отправлен успешно");
    }
}