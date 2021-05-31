package ru.unit_techno.qr_entry_control_imp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.unit_techno.qr_entry_control_imp.dto.service.QrObjectTemplateDto;
import ru.unit_techno.qr_entry_control_imp.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_imp.repository.QrRepository;
import ru.unit_techno.qr_entry_control_imp.service.listener.EventListenerCanCrash;

import java.util.Optional;

@Service
@Slf4j
public class QrValidationService {

    private QrRepository repository;
    private ObjectMapper mapper;

    @Autowired
    public QrValidationService(QrRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @SneakyThrows
    @EventListenerCanCrash
    public void parseQrCodeMessage(String qrMessage) {

        if (!qrCodeValidation(qrMessage)) {
            throw new IllegalArgumentException("Wrong QR code format! Please use our QR code format!");
        }

        QrObjectTemplateDto qrCodeEntity = mapper.readValue(qrMessage, QrObjectTemplateDto.class);
        Optional<QrCodeEntity> qrObj = repository.findById(qrCodeEntity.getId());

        //TODO проверять дату въезда, если вдруг человек заказал на завтра,приехал сегодня
        if (qrObj.isPresent()) {
            QrCodeEntity qrCodeEnt = qrObj.get();
            if (qrCodeEnt.getExpire()) {
                throw new Exception("QR code has expired! Try generate new QR code and use it! Expire date: " + qrCodeEnt.getExpire().toString());
            }
            //todo выдача карточки КАК В АЭРОПОРТУ и открытие шлагбаума
            qrCodeEnt.setExpire(true);
            repository.save(qrCodeEnt);
            log.info(qrCodeEnt.toString());
        } else {
            throw new Exception("We are no have this QR code in database. Try generate QR code on our website!");
        }
    }

    public boolean qrCodeValidation(String qrCodeMessage) {
        return qrCodeMessage.contains("id")
                && qrCodeMessage.contains("governmentNumber")
                && qrCodeMessage.contains("name")
                && qrCodeMessage.contains("surname")
                && qrCodeMessage.contains("expire");
    }
}
