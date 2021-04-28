
package ru.unit_techno.qr_entry_control_imp.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;

@Service
@Setter
@RequiredArgsConstructor
public class QrEmailService implements EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;

    @Override
    @SneakyThrows
    public void sendMessageUsingThymeleafTemplate(String to,
                                                  String subject,
                                                  Map<String, Object> templateModel,
                                                  String pathToQr) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("template-thymeleaf.html", thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody, pathToQr);
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody, String pathToQr) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("crime-it-tech-user@yandex.ru");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.addAttachment("qr-code.png", new File("temp\\" + pathToQr));
        helper.setText(htmlBody, true);
        javaMailSender.send(message);
    }
}