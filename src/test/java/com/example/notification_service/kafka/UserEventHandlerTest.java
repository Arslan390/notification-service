package com.example.notification_service.kafka;

import com.example.notification_service.handler.UserEventHandler;
import com.example.notification_service.service.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@EmbeddedKafka()
@SpringBootTest(properties = "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}")
public  class UserEventHandlerTest {

    @MockitoBean
    EmailService emailService;

    @Autowired
    KafkaTemplate<String, UserEvent> kafkaTemplate;

    @MockitoSpyBean
    UserEventHandler userEventHandler;

    @Test
    void shouldSendWelcomeEmail_whenEventTitleIsCreated() throws ExecutionException, InterruptedException {
    UserEvent event = UserEvent.builder().title("CREATED").email("test@mail.ru").build();
    String messageKey = event.getEmail();

    kafkaTemplate.send("user-events-topic", messageKey, event).get();

    ArgumentCaptor<UserEvent> captor = ArgumentCaptor.forClass(UserEvent.class);
    verify(userEventHandler, timeout(5000).times(1)).handle(captor.capture());
    assertEquals(event.getTitle(), captor.getValue().getTitle());
    verify(emailService, timeout(5000).times(1)).sendWelcomeEmail(captor.getValue().getEmail());
    }

    @Test
    void shouldSendGoodbyeEmail_whenEventTitleIsDeleted() throws ExecutionException, InterruptedException {
        UserEvent event = UserEvent.builder().title("DELETED").email("test@mail.ru").build();
        String messageKey = event.getEmail();

        kafkaTemplate.send("user-events-topic", messageKey, event).get();

        ArgumentCaptor<UserEvent> captor = ArgumentCaptor.forClass(UserEvent.class);
        verify(userEventHandler, timeout(5000).times(1)).handle(captor.capture());
        assertEquals("DELETED", captor.getValue().getTitle());
        verify(emailService, timeout(5000).times(1)).sendGoodbyeEmail(captor.getValue().getEmail());
    }

    @Test
    void shouldLogWarning_whenUnknownEventTitle() throws ExecutionException, InterruptedException {
        UserEvent event = UserEvent.builder().title("UNKNOWN_OPERATION").email("test@mail.ru").build();
        String messageKey = event.getEmail();

        kafkaTemplate.send("user-events-topic", messageKey, event).get();

        ArgumentCaptor<UserEvent> captor = ArgumentCaptor.forClass(UserEvent.class);
        verify(userEventHandler, timeout(5000).times(1)).handle(captor.capture());
        assertEquals("UNKNOWN_OPERATION", captor.getValue().getTitle());
        verify(emailService, never()).sendWelcomeEmail(anyString());
        verify(emailService, never()).sendGoodbyeEmail(anyString());
    }
}