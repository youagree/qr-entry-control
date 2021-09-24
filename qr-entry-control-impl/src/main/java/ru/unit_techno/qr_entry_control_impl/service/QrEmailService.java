
package ru.unit_techno.qr_entry_control_impl.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import ru.unit_techno.qr_entry_control_impl.dto.service.QrPictureObject;
import ru.unit_techno.qr_entry_control_impl.entity.enums.DeliveryStatus;
import ru.unit_techno.qr_entry_control_impl.repository.QrDeliveryEntityRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
@Setter
@RequiredArgsConstructor
public class QrEmailService implements EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final MessageStorageService messageStorageService;
    private final QrDeliveryEntityRepository qrDeliveryEntityRepository;

    @Override
    @Transactional
    public boolean sendMessageUsingThymeleafTemplate(String to,
                                                     String subject,
                                                     QrPictureObject qrPictureObject) {
        try {
            Context thymeleafContext = new Context();
            thymeleafContext.setVariables(qrPictureObject.getMetadataForSendMessage());
            String htmlBody = thymeleafTemplateEngine.process("template-thymeleaf.html", thymeleafContext);
            sendHtmlMessage(to, subject, htmlBody, qrPictureObject.getFilePath());
            //TODO добавить удаление qr кода(картинки)
            qrDeliveryEntityRepository.updateStatus(qrPictureObject.getMessageTag(), DeliveryStatus.DELIVERED);
            return true;
        } catch (Exception e) {
            messageStorageService.putNotDeliveryMessage(to, qrPictureObject);
            qrDeliveryEntityRepository.updateStatus(qrPictureObject.getMessageTag(), DeliveryStatus.NOT_DELIVERED);
            return false;
        }
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