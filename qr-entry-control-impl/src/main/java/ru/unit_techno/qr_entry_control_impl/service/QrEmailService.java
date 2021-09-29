
package ru.unit_techno.qr_entry_control_impl.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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
import java.io.FileNotFoundException;

@Slf4j
@Service
@Setter
@RequiredArgsConstructor
public class QrEmailService implements EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final MessageStorageService messageStorageService;
    private final QrDeliveryEntityRepository qrDeliveryEntityRepository;
    @Value("classpath:/mail-logo.png")
    private Resource resourceFile;

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
            deleteSuccessSendingQr(qrPictureObject.getFilePath());
            qrDeliveryEntityRepository.updateStatus(qrPictureObject.getDeliveryEntityId(), DeliveryStatus.DELIVERED);
            return true;
        } catch (Exception e) {
            messageStorageService.putNotDeliveryMessage(to, qrPictureObject);
            qrDeliveryEntityRepository.updateStatus(qrPictureObject.getDeliveryEntityId(), DeliveryStatus.NOT_DELIVERED);
            return false;
        }
    }

    private void deleteSuccessSendingQr(String pathToDelete) {
        try {
            boolean delete = new File("temp\\" + pathToDelete).delete();
            if (delete) {
                log.info("file {} success delete from temp", pathToDelete);
                return;
            }
            log.info("delete file {} not success", pathToDelete);
        } catch (Exception e) {
            log.debug("exception {}", e.getMessage());
        }
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody, String pathToQr) throws MessagingException, FileNotFoundException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("crime-it-tech-user@yandex.ru");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        //add attach
        helper.addAttachment("qr-code.png", new File("temp\\" + pathToQr));

        //add image inside message
        helper.addInline("qr-code-for-send.png", new FileSystemResource(new File("temp\\" + pathToQr)), "image/png");

        javaMailSender.send(message);
    }
}