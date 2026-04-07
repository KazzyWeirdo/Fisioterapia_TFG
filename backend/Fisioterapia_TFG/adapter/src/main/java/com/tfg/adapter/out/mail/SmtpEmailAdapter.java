package com.tfg.adapter.out.mail;

import com.tfg.port.out.mail.EmailSenderPort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmtpEmailAdapter implements EmailSenderPort {

    private final String formBaseUrl;
    private final JavaMailSender mailSender;

    public SmtpEmailAdapter(JavaMailSender mailSender,
        @Value("${application.form.base-url}") String formBaseUrl){
        this.mailSender = mailSender;
        this.formBaseUrl = formBaseUrl;
    }

    @Override
    public void sendFormLink(String email, int id) {
        SimpleMailMessage message = new SimpleMailMessage();
        String formUrl = formBaseUrl + "/" + id;
        message.setTo(email);
        message.setSubject("Registro de sesiones de entrenamiento");
        message.setText("%s".formatted(formUrl));
        mailSender.send(message);
    }
}
