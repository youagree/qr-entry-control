package ru.unit_techno.qr_entry_control_impl.scheduler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import ru.unit_techno.qr_entry_control_impl.base.BaseTestClass;
import ru.unit_techno.qr_entry_control_impl.entity.CardEntity;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_impl.entity.QrDeliveryEntity;
import ru.unit_techno.qr_entry_control_impl.entity.enums.CardStatus;
import ru.unit_techno.qr_entry_control_impl.entity.enums.DeliveryStatus;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class QrSchedulerTest extends BaseTestClass {

    @Test
    @Transactional
    @DisplayName("Удаление QR кодов с помощью метода шедулера")
    public void qrDeletingMethodTest() {
        UUID uuid = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        UUID uuid3 = UUID.randomUUID();

        CardEntity firstCard = new CardEntity()
                .setCardValue("тупорылый")
                .setCardStatus(CardStatus.ISSUED);

        CardEntity secondCard = new CardEntity()
                .setCardValue("тупорылый")
                .setCardStatus(CardStatus.ISSUED);

        QrCodeEntity normQr = new QrCodeEntity()
                .setExpire(false)
                .setFullName("Ignat")
                .setGovernmentNumber("А777АА 77")
                .setCreationDate(Timestamp.valueOf(LocalDateTime.now()))
                .setEmail("azaza@gmail.com")
                .setEnteringDate(LocalDate.of(2021, 9, 24))
                .setQrPicture("Picture")
                .setUuid(uuid)
                .setQrDeliveryEntity(new QrDeliveryEntity()
                        .setDeliveryStatus(DeliveryStatus.DELIVERED));

        QrCodeEntity qrWithoutCard = new QrCodeEntity()
                .setExpire(true)
                .setFullName("Ignat")
                .setGovernmentNumber("А777АА 77")
                .setCreationDate(Timestamp.valueOf(LocalDateTime.now()))
                .setEmail("azaza@gmail.com")
                .setEnteringDate(LocalDate.of(2021, 9, 24))
                .setQrPicture("Picture")
                .setUuid(uuid2)
                .setQrDeliveryEntity(new QrDeliveryEntity()
                        .setDeliveryStatus(DeliveryStatus.DELIVERED));

        QrCodeEntity expiredQr = new QrCodeEntity()
                .setExpire(true)
                .setFullName("Ignat")
                .setGovernmentNumber("А777АА 77")
                .setCreationDate(Timestamp.valueOf(LocalDateTime.now()))
                .setEmail("azaza@gmail.com")
                .setEnteringDate(LocalDate.of(2021, 9, 24))
                .setQrPicture("Picture")
                .setUuid(uuid3)
                .setQrDeliveryEntity(new QrDeliveryEntity()
                        .setDeliveryStatus(DeliveryStatus.DELIVERED));

        normQr.addCard(firstCard);
        expiredQr.addCard(secondCard);

        qrRepository.save(normQr);
        qrRepository.save(qrWithoutCard);
        qrRepository.save(expiredQr);

        qrRepository.deleteAllByExpireTrue();


        Assertions.assertEquals(qrRepository.findAll().size(), 2);
    }
}
