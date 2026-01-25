package com.example.notification_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())

                .info(new Info()
                        .title("Notification Service API")
                        .description(
                                "REST API для отправки email-уведомлений. " +
                                        "Поддерживает отправку приветственных писем, уведомлений об удалении аккаунта " +
                                        "и произвольных сообщений через Kafka и SMTP."
                        )
                        .version("1.0.0")

                        .contact(new Contact()
                                .name("Техническая поддержка")
                                .email("support@notification-service.com")
                                .url("https://notification-service.com/contact")
                        )

                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")
                        )
                );
    }
}
