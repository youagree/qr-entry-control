
package ru.unit_techno.qr_entry_control_impl.service;

import ru.unit_techno.qr_entry_control_impl.dto.service.QrPictureObject;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailService {
    boolean sendMessageUsingThymeleafTemplate(String to,
                                              String subject,
                                              QrPictureObject qrPictureObject)
            throws IOException, MessagingException;
}