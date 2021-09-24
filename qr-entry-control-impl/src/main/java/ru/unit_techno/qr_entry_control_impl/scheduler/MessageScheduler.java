
package ru.unit_techno.qr_entry_control_impl.scheduler;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.unit_techno.qr_entry_control_impl.dto.service.QrPictureObject;
import ru.unit_techno.qr_entry_control_impl.entity.QrDeliveryEntity;
import ru.unit_techno.qr_entry_control_impl.entity.enums.DeliveryStatus;
import ru.unit_techno.qr_entry_control_impl.repository.QrDeliveryEntityRepository;
import ru.unit_techno.qr_entry_control_impl.service.MessageStorageService;
import ru.unit_techno.qr_entry_control_impl.service.QrEmailService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageScheduler {

    private final MessageStorageService messageStorageService;
    private final QrEmailService qrEmailService;
    private final QrDeliveryEntityRepository qrDeliveryEntityRepository;

    @Scheduled(cron = "0 */1 * * * *", zone = "")
    @Transactional
    public void resendMessage() {
        log.info("Start resending not delivered messages");
        ConcurrentHashMap<String, QrPictureObject> notDeliveryQr = messageStorageService.getNotDeliveryQr();

        List<QrDeliveryEntity> successDeliveryStatus = new ArrayList<>();
        notDeliveryQr.forEach((key, resendObject) -> {
            boolean deliveryResult = qrEmailService.sendMessageUsingThymeleafTemplate(
                    key,
                    "Проезд на территорию предприятия",
                    resendObject
            );
            log.info("Message sent, {}", resendObject);
            if (deliveryResult) {
                log.info("Message sent successful");
                messageStorageService.deleteSuccessDeliveryMessage(key);
                successDeliveryStatus.add(new QrDeliveryEntity().setMessageTag(
                        resendObject.getMessageTag())
                        .setDeliveryStatus(DeliveryStatus.DELIVERED));
            } else {
                log.info("message not delivery {}", resendObject);
            }
        });

        List<UUID> forUpdateStatuses = successDeliveryStatus.stream()
                .map(QrDeliveryEntity::getMessageTag)
                .collect(Collectors.toList());
        qrDeliveryEntityRepository.updateStatuses(forUpdateStatuses, DeliveryStatus.DELIVERED);
        log.info("Message has been resent and complete successful");
    }
}