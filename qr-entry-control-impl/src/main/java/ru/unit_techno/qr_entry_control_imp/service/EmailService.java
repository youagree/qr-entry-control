
package ru.unit_techno.qr_entry_control_imp.service;

import ru.unit_techno.qr_entry_control_imp.dto.service.QrPictureObject;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailService {
    boolean sendMessageUsingThymeleafTemplate(String to,
                                              String subject,
                                              QrPictureObject qrPictureObject)
            throws IOException, MessagingException;
}