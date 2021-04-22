
package ru.unit_techno.qr_entry_control_imp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QrEmailService {

    private final JavaMailSender javaMailSender;

    public void sendQrToMail(String qr){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("aslg@yandex.ru");
            message.setTo("pavsoldatov96@gmail.com");
            message.setSubject("test_subject");
            message.setText("test_text");
            javaMailSender.send(message);
        } catch (Exception e) {

        }
    }
}