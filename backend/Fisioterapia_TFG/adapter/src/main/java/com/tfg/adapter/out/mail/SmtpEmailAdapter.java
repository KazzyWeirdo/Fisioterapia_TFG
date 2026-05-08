package com.tfg.adapter.out.mail;

import com.tfg.port.out.mail.EmailSenderPort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmtpEmailAdapter implements EmailSenderPort {

    private final String resetBaseUrl;
    private final JavaMailSender mailSender;

    public SmtpEmailAdapter(JavaMailSender mailSender,
                            @Value("${application.reset.base-url}") String resetBaseUrl) {
        this.mailSender = mailSender;
        this.resetBaseUrl = resetBaseUrl;
    }

    @Override
    public void sendPasswordResetLink(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        String resetUrl = resetBaseUrl + "?token=" + token;
        message.setTo(email);
        message.setSubject("Recuperación de contraseña");
        message.setText("""
            Has solicitado restablecer tu contraseña.
            
            Haz clic en el siguiente enlace (válido durante 1 hora):
            
            %s
            
            Si no has solicitado este cambio, ignora este mensaje.
            """.formatted(resetUrl));
        mailSender.send(message);
    }
}
