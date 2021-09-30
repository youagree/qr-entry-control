
package ru.unit_techno.qr_entry_control_impl.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.unit_techno.qr_entry_control_impl.dto.QrCodeDto;
import ru.unit_techno.qr_entry_control_impl.dto.service.QrObjectTemplateDto;
import ru.unit_techno.qr_entry_control_impl.dto.service.QrPictureObject;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_impl.entity.QrDeliveryEntity;
import ru.unit_techno.qr_entry_control_impl.entity.enums.DeliveryStatus;
import ru.unit_techno.qr_entry_control_impl.exception.DeliverySendException;
import ru.unit_techno.qr_entry_control_impl.mapper.QrMapper;
import ru.unit_techno.qr_entry_control_impl.repository.QrRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

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

        QrObjectTemplateDto qrObjectTemplateDto = qrMapper.toTempQrObject(qrCodeDto);
        qrObjectTemplateDto.setUuid(String.valueOf(UUID.randomUUID()));
        QrPictureObject qrPictureObject = qrGeneratorService.generateQrPictureObject(qrObjectTemplateDto);

        QrCodeEntity qrCodeForSave = qrMapper.toDomain(qrCodeDto);
        QrCodeEntity byQrPicture = qrRepository.findByQrPicture(qrPictureObject.getQrImageInBase64());
        qrCodeForSave.setQrPicture(qrPictureObject.getQrImageInBase64());
        qrCodeForSave.setCreationDate(new Timestamp(System.currentTimeMillis()));
        qrCodeForSave.setQrDeliveryEntity(new QrDeliveryEntity()
                .setDeliveryStatus(DeliveryStatus.NOT_DELIVERED)
        );
        QrCodeEntity save = qrRepository.save(qrCodeForSave);

        Long deliveryId = save.getQrDeliveryEntity().getId();
        qrPictureObject.setDeliveryEntityId(deliveryId);
        qrPictureObject.setMetadataForSendMessage(buildMetadataForMessage(qrPictureObject, qrCodeForSave));

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

    private HashMap<String, Object> buildMetadataForMessage(QrPictureObject qrPictureObject, QrCodeEntity qrCodeForSave) {
        LocalDateTime enteringDate = qrCodeForSave.getEnteringDate();
        String resultDate =
                DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                        .withLocale(new Locale("ru"))
                        .format(LocalDate.of(enteringDate.getYear(), enteringDate.getMonth(), enteringDate.getDayOfMonth()));
        String withCorrectYear = resultDate.replaceAll("г\\.", "года");
        return new HashMap<>() {{
            put("surname", qrCodeForSave.getName() + " " + qrCodeForSave.getSurname() + "!");
            put("date", withCorrectYear);
            put("pathToQr", "temp/" + qrPictureObject.getFilePath());
        }};
    }
}