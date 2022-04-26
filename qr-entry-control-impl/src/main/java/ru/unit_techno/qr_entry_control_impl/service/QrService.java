
package ru.unit_techno.qr_entry_control_impl.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.unit_techno.qr_entry_control_impl.dto.QrCodeDto;
import ru.unit_techno.qr_entry_control_impl.dto.QrInfoDto;
import ru.unit_techno.qr_entry_control_impl.dto.service.QrObjectTemplateDto;
import ru.unit_techno.qr_entry_control_impl.dto.service.QrPictureObject;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_impl.entity.QrDeliveryEntity;
import ru.unit_techno.qr_entry_control_impl.entity.enums.DeliveryStatus;
import ru.unit_techno.qr_entry_control_impl.exception.DeliverySendException;
import ru.unit_techno.qr_entry_control_impl.mapper.QrMapper;
import ru.unit_techno.qr_entry_control_impl.repository.QrRepository;
import ru.unit_techno.qr_entry_control_impl.util.Constant;
import ru.unit_techno.qr_entry_control_impl.util.SpecUtils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

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
        qrCodeForSave.setQrPicture(qrPictureObject.getQrImageInBase64());
        qrCodeForSave.setCreationDate(new Timestamp(System.currentTimeMillis()));
        qrCodeForSave.setEnteringDate(qrCodeDto.getEnteringDate());
        qrCodeForSave.setQrDeliveryEntity(new QrDeliveryEntity()
                .setDeliveryStatus(DeliveryStatus.NOT_DELIVERED)
        );
        qrCodeForSave.setUuid(UUID.fromString(qrObjectTemplateDto.getUuid()));
        qrCodeForSave.setExpire(false);
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

    public Page<QrInfoDto> getAllQrCodesInfo(Specification<QrCodeEntity> specification, Pageable pageable) {
        SpecUtils.expireFalse(specification);
        Page<QrCodeEntity> allQrCodes = qrRepository.findAll(specification, pageable);
        return new PageImpl<>(allQrCodes.stream()
                .map(qrMapper::entityToInfo)
                .collect(Collectors.toList()), pageable, allQrCodes.getTotalElements());
    }

    private HashMap<String, Object> buildMetadataForMessage(QrPictureObject qrPictureObject, QrCodeEntity qrCodeForSave) {
        LocalDate enteringDate = qrCodeForSave.getEnteringDate();
        String resultDate =
                DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                        .withLocale(new Locale("ru"))
                        .format(LocalDate.of(enteringDate.getYear(), enteringDate.getMonth(), enteringDate.getDayOfMonth()));
        String withCorrectYear = resultDate.replaceAll("г\\.", "года");
        return new HashMap<>() {{
            put("surname", qrCodeForSave.getFullName() + "!");
            put("date", withCorrectYear);
            put("pathToQr", Constant.PATH_TO_QRS + qrPictureObject.getFilePath());
        }};
    }
}