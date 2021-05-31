
package ru.unit_techno.qr_entry_control_imp.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.unit_techno.qr_entry_control_imp.dto.QrCodeDto;
import ru.unit_techno.qr_entry_control_imp.dto.service.QrPictureObject;
import ru.unit_techno.qr_entry_control_imp.entity.QrCodeEntity;
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
    public long createAndSendQrToEmail(QrCodeDto qrCodeDto) {
        QrCodeEntity save = qrRepository.save(qrMapper.toDomain(qrCodeDto));
        QrPictureObject qrPictureObject = qrGeneratorService.generateQrPictureObject(qrMapper.toTempQrObject(qrCodeDto)
        .setId(save.getQrId()));

        save.setQrPicture(qrPictureObject.getQrImageInBase64());
        save.setCreationDate(new Timestamp(System.currentTimeMillis()));
        save.setExpire(false);
        save.setEnteringDate(qrCodeDto.getEnteringDate());
        qrRepository.save(save);

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