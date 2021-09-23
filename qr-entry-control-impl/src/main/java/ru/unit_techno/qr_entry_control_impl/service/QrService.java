
package ru.unit_techno.qr_entry_control_impl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.unit.techno.ariss.barrier.api.BarrierFeignClient;
import ru.unit.techno.device.registration.api.DeviceResource;
import ru.unit_techno.qr_entry_control_impl.dto.QrCodeDto;
import ru.unit_techno.qr_entry_control_impl.dto.service.QrObjectTemplateDto;
import ru.unit_techno.qr_entry_control_impl.dto.service.QrPictureObject;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_impl.entity.QrDeliveryEntity;
import ru.unit_techno.qr_entry_control_impl.entity.enums.DeliveryStatus;
import ru.unit_techno.qr_entry_control_impl.exception.DeliverySendException;
import ru.unit_techno.qr_entry_control_impl.mapper.EntryDeviceToReqRespMapper;
import ru.unit_techno.qr_entry_control_impl.mapper.QrMapper;
import ru.unit_techno.qr_entry_control_impl.repository.QrRepository;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrService {

    private final QrMapper qrMapper;
    private final QrRepository qrRepository;
    private final QrEmailService qrEmailService;
    private final QrGeneratorService qrGeneratorService;
    private final ObjectMapper mapper;
    private final DeviceResource deviceResource;
    private final BarrierFeignClient barrierFeignClient;
    private final EntryDeviceToReqRespMapper reqRespMapper;

    @SneakyThrows
    @Transactional
    public Long createAndSendQrToEmail(QrCodeDto qrCodeDto) {

        QrObjectTemplateDto qrObjectTemplateDto = qrMapper.toTempQrObject(qrCodeDto);
        qrObjectTemplateDto.setUuid(String.valueOf(UUID.randomUUID()));
        QrPictureObject qrPictureObject = qrGeneratorService.generateQrPictureObject(qrObjectTemplateDto);

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
                .setDeliveryStatus(DeliveryStatus.NOT_DELIVERED)
        );
        QrCodeEntity save = qrRepository.save(qrCodeForSave);

        Long deliveryId = save.getQrDeliveryEntity().getId();
        qrPictureObject.setDeliveryEntityId(deliveryId);

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