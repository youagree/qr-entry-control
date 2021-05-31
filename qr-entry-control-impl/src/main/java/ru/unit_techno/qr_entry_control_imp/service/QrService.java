
package ru.unit_techno.qr_entry_control_imp.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.unit_techno.qr_entry_control_imp.dto.QrCodeDto;
import ru.unit_techno.qr_entry_control_imp.dto.service.QrPictureObject;
import ru.unit_techno.qr_entry_control_imp.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_imp.entity.QrDeliveryEntity;
import ru.unit_techno.qr_entry_control_imp.entity.enums.DeliveryStatus;
import ru.unit_techno.qr_entry_control_imp.exception.DeliverySendException;
import ru.unit_techno.qr_entry_control_imp.mapper.QrMapper;
import ru.unit_techno.qr_entry_control_imp.repository.QrRepository;

import java.sql.Timestamp;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class QrService {

    private final QrMapper qrMapper;
    private final QrRepository qrRepository;
    private final QrEmailService qrEmailService;
    private final QrGeneratorService qrGeneratorService;

    @SneakyThrows
    @Transactional
    public Long createAndSendQrToEmail(QrCodeDto qrCodeDto) {

        QrPictureObject qrPictureObject = qrGeneratorService.generateQrPictureObject(qrMapper.toTempQrObject(qrCodeDto));

        QrCodeEntity qrCodeForSave = qrMapper.toDomain(qrCodeDto);
        QrCodeEntity byQrPicture = qrRepository.findByQrPicture(qrPictureObject.getQrImageInBase64());
        //TODO проверять по планируемой дате въезда вместо этого ГОВНА
        if (byQrPicture != null) {
            if (byQrPicture.getQrDeliveryEntity().getDeliveryStatus().getValue().equals(DeliveryStatus.NOT_DELIVERED.getValue())) {
                return null;
            }
        }
        qrCodeForSave.setQrPicture(qrPictureObject.getQrImageInBase64());
        qrCodeForSave.setCreationDate(new Timestamp(System.currentTimeMillis()));
        qrCodeForSave.setQrDeliveryEntity(new QrDeliveryEntity()
                .setMessageTag(qrPictureObject.getMessageTag())
                .setDeliveryStatus(DeliveryStatus.NOT_DELIVERED)
        );
        qrRepository.save(qrCodeForSave);

        HashMap<String, Object> map = new HashMap<>() {{
            put("surname", qrCodeForSave.getSurname() + " " + qrCodeForSave.getName());
            //TODO дооработать, передавать дату вьезда
            put("date", "27.04.2021");
            put("senderName", "Технический пользователь, ");
        }};

        qrPictureObject.setMetadataForSendMessage(map);

        boolean deliveryResult = qrEmailService.sendMessageUsingThymeleafTemplate(
                qrCodeDto.getEmail(),
                "Проезд на территорию предприятия",
                qrPictureObject
        );
        if (!deliveryResult) {
            throw new DeliverySendException("message not delivery");
        }

        return qrCodeForSave.getQrId();
    }

}