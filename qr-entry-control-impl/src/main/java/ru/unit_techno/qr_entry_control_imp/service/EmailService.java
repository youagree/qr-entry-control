
package ru.unit_techno.qr_entry_control_imp.service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

public interface EmailService {
        void sendMessageUsingThymeleafTemplate(String to,
                                               String subject,
                                               Map<String, Object> templateModel)
                throws IOException, MessagingException;
}