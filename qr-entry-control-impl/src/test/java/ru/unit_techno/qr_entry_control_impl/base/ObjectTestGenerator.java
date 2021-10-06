
package ru.unit_techno.qr_entry_control_impl.base;

import lombok.experimental.UtilityClass;
import ru.unit_techno.qr_entry_control_impl.entity.CardEntity;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_impl.entity.QrDeliveryEntity;
import ru.unit_techno.qr_entry_control_impl.entity.enums.CardStatus;
import ru.unit_techno.qr_entry_control_impl.entity.enums.DeliveryStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public class ObjectTestGenerator {

    public QrCodeEntity getTestQr() {
        QrCodeEntity qrCodeEntity = new QrCodeEntity()
                .setQrPicture("testPicture")
                .setCreationDate(new Timestamp(System.currentTimeMillis()))
                .setGovernmentNumber("F123FF 102")
                .setName("Ivan")
                .setSurname("Gamaz")
                .setEmail("as@ya.ru")
                .setExpire(false)
                .setUuid(UUID.randomUUID())
                .setEnteringDate(LocalDateTime.now().plusHours(1L))
                .setQrDeliveryEntity(getQrDeliveryEntity());
        qrCodeEntity.addCard(getCard());
        return qrCodeEntity;
    }

    public CardEntity getCard() {
        return new CardEntity()
                .setCardValue("123456")
                .setCardStatus(CardStatus.ISSUED);
    }

    public QrDeliveryEntity getQrDeliveryEntity() {
        return new QrDeliveryEntity()
                .setDeliveryStatus(DeliveryStatus.DELIVERED);
    }
}