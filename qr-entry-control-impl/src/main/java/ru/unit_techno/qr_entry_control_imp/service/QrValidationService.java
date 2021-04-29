package ru.unit_techno.qr_entry_control_imp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.unit_techno.qr_entry_control_imp.dto.service.QrObjectTemplateDto;
import ru.unit_techno.qr_entry_control_imp.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_imp.repository.QrRepository;

import java.util.Optional;

@Service
public class QrValidationService {

    private QrRepository repository;
    private ObjectMapper mapper;

    @Autowired
    public QrValidationService(QrRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @SneakyThrows
    public void parseQrCodeMessage(String qrMessage) {
        QrObjectTemplateDto qrCodeEntity = mapper.readValue(qrMessage, QrObjectTemplateDto.class);
        Optional<QrCodeEntity> qrObj = repository.findById(qrCodeEntity.getId());

        if (qrObj.isPresent()) {
            QrCodeEntity qrCodeEnt = qrObj.get();
            //todo выдача карточки КАК В АЭРОПОРТУ и открытие шлагбаума
            qrCodeEnt.setExpire(true);
            repository.save(qrCodeEnt);
        } else {
            throw new Exception("Въезд запрещен! Проваливай!");
        }
    }
}
