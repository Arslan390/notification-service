package com.example.notification_service.service;

import com.example.notification_service.dto.EmailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendWelcomeEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Здравствуйте!");
        message.setText("Ваш аккаунт на сайте был успешно создан.");
        sendEmail(message);
    }

    public void sendGoodbyeEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Здравствуйте!");
        message.setText("Ваш аккаунт был удалён. ");
        sendEmail(message);
    }

    private void sendEmail(SimpleMailMessage message) {
        try {
            mailSender.send(message);
            log.info("Email отправлен на адрес {}",  message.getTo());
        } catch (Exception e) {
            log.error("Ошибка отправки email на адрес: {}", message.getTo(), e);
        }
    }

    public void sendCustomEmail(EmailRequest emailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(emailRequest.getTo());
        message.setSubject(emailRequest.getSubject());
        message.setText(emailRequest.getText());
        sendEmail(message);
    }
}
