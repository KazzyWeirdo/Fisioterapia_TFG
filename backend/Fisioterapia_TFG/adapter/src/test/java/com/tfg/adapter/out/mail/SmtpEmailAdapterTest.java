package com.tfg.adapter.out.mail;

import org.springframework.mail.javamail.JavaMailSender;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SmtpEmailAdapterTest {
    JavaMailSender mailSenderMock = mock(JavaMailSender.class);
    String formBaseUrl = "https://midominio.com/form";
    String resetBaseUrl = "https://midominio.com/reset-password";

    SmtpEmailAdapter smtpEmailAdapter = new SmtpEmailAdapter(mailSenderMock, formBaseUrl, resetBaseUrl);

    @Test
    public void givenEmailAndId_whenSendFormLink_thenMailIsSentWithCorrectData() {
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

    @Test
    public void givenEmailAndToken_whenSendPasswordResetLink_thenMailIsSentWithCorrectData() {
        String targetEmail = "fisio@clinica.com";
        String resetToken = "uuid-token-9876";

        smtpEmailAdapter.sendPasswordResetLink(targetEmail, resetToken);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSenderMock).send(messageCaptor.capture());

        SimpleMailMessage capturedMessage = messageCaptor.getValue();

        assertEquals(targetEmail, java.util.Objects.requireNonNull(capturedMessage.getTo())[0]);
        assertEquals("Recuperación de contraseña", capturedMessage.getSubject());

        String expectedUrl = resetBaseUrl + "?token=" + resetToken;
        String actualText = capturedMessage.getText();

        assertNotNull(actualText, "Body cannot be null");

        assertTrue(actualText.contains(expectedUrl),
                "Mail must have the exact link of recovery");

        assertTrue(actualText.contains("Has solicitado restablecer tu contraseña"),
                "Inital text is missing");
        assertTrue(actualText.contains("válido durante 1 hora"),
                "Expired date missing");
    }
}
