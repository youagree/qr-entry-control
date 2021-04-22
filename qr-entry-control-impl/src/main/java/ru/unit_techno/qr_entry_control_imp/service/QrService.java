
package ru.unit_techno.qr_entry_control_imp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.unit_techno.qr_entry_control_imp.dto.QrCodeDto;
import ru.unit_techno.qr_entry_control_imp.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_imp.mapper.QrMapper;
import ru.unit_techno.qr_entry_control_imp.repository.QrRepository;

@Service
@RequiredArgsConstructor
public class QrService {

    private final QrMapper qrMapper;
    private final QrRepository qrRepository;
    private final QrEmailService qrEmailService;

    public long createAndSendQrToEmail(QrCodeDto qrCodeDto) {
        QrCodeEntity save = qrRepository.save(qrMapper.toDomain(qrCodeDto));
        qrEmailService.sendQrToMail(save.getQrPicture());
        return save.getQrId();
    }

}