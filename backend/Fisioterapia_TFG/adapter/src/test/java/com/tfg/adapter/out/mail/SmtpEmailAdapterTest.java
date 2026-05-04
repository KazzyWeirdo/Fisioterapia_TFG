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
    String resetBaseUrl = "https://midominio.com/reset-password";

    SmtpEmailAdapter smtpEmailAdapter = new SmtpEmailAdapter(mailSenderMock, resetBaseUrl);

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
