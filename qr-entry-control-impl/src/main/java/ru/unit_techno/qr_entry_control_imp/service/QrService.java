
package ru.unit_techno.qr_entry_control_imp.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.unit_techno.qr_entry_control_imp.dto.QrCodeDto;
import ru.unit_techno.qr_entry_control_imp.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_imp.mapper.QrMapper;
import ru.unit_techno.qr_entry_control_imp.repository.QrRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class QrService {

    private final QrMapper qrMapper;
    private final QrRepository qrRepository;
    private final QrEmailService qrEmailService;

    @SneakyThrows
    public long createAndSendQrToEmail(QrCodeDto qrCodeDto) {
        QrCodeEntity save = qrRepository.save(qrMapper.toDomain(qrCodeDto));
        qrEmailService.sendMessageUsingThymeleafTemplate(
                qrCodeDto.getEmail(), "Проезд на территорию предприятия",
                Map.of("surname", save.getSurname() + " " + save.getName(),
                        //TODO дооработать, передавать дату вьезда
                        "date", "27.04.2021",
                        "senderName", "Технический пользователь, "
                )
        );
        return save.getQrId();
    }

}