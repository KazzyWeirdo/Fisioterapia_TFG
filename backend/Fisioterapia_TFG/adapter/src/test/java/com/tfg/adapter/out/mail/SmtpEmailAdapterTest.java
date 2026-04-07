package com.tfg.adapter.out.mail;

import org.springframework.mail.javamail.JavaMailSender;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SmtpEmailAdapterTest {
    @Test
    public void givenEmailAndId_whenSendFormLink_thenMailIsSentWithCorrectData() {
        JavaMailSender mailSenderMock = mock(JavaMailSender.class);
        String testBaseUrl = "https://midominio.com/form";

        SmtpEmailAdapter smtpEmailAdapter = new SmtpEmailAdapter(mailSenderMock, testBaseUrl);

        String targetEmail = "patient@gmail.com";
        int patientId = 42;

        smtpEmailAdapter.sendFormLink(targetEmail, patientId);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSenderMock).send(messageCaptor.capture());

        SimpleMailMessage capturedMessage = messageCaptor.getValue();

        assertEquals(targetEmail, Objects.requireNonNull(capturedMessage.getTo())[0]);
        assertEquals("Registro de sesiones de entrenamiento", capturedMessage.getSubject());
        assertEquals("https://midominio.com/form/42", capturedMessage.getText());
    }
}
