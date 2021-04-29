
package ru.unit_techno.qr_entry_control_imp.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.unit_techno.qr_entry_control_imp.dto.QrCodeDto;
import ru.unit_techno.qr_entry_control_imp.dto.service.QrPictureObject;
import ru.unit_techno.qr_entry_control_imp.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_imp.mapper.QrMapper;
import ru.unit_techno.qr_entry_control_imp.repository.QrRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QrService {

    private final QrMapper qrMapper;
    private final QrRepository qrRepository;
    private final QrEmailService qrEmailService;
    private final QrGeneratorService qrGeneratorService;

    @SneakyThrows
    public long createAndSendQrToEmail(QrCodeDto qrCodeDto) {
        QrPictureObject qrPictureObject = qrGeneratorService.generateQrPictureObject(qrMapper.toTempQrObject(qrCodeDto));

        //собираем обьект для сейва, в случае если не удалось отправить, чтобы была возможность повторной отправки
        QrCodeEntity qrCodeEntity = qrMapper.toDomain(qrCodeDto);
        qrCodeEntity.setQrPicture(qrPictureObject.getQrImageInBase64());

        QrCodeEntity save = qrRepository.save(qrCodeEntity);

        HashMap<String, Object> map = new HashMap<String, Object>() {{
            put("surname", save.getSurname() + " " + save.getName());
            //TODO дооработать, передавать дату вьезда
            put("date", "27.04.2021");
            put("senderName", "Технический пользователь, ");
        }};

        qrEmailService.sendMessageUsingThymeleafTemplate(
                qrCodeDto.getEmail(), "Проезд на территорию предприятия",
                map,
                qrPictureObject.getFilePath()
        );
        return save.getQrId();
    }

}