package ru.unit_techno.qr_entry_control_impl.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.unit.techno.ariss.barrier.api.BarrierFeignClient;
import ru.unit.techno.ariss.barrier.api.dto.BarrierRequestDto;
import ru.unit.techno.ariss.barrier.api.dto.BarrierResponseDto;
import ru.unit.techno.ariss.log.action.lib.api.LogActionBuilder;
import ru.unit.techno.ariss.log.action.lib.entity.Description;
import ru.unit.techno.ariss.log.action.lib.model.ActionStatus;
import ru.unit.techno.device.registration.api.DeviceResource;
import ru.unit.techno.device.registration.api.dto.DeviceResponseDto;
import ru.unit.techno.device.registration.api.enums.DeviceType;
import ru.unit_techno.qr_entry_control_impl.dto.InputQrFromFirmware;
import ru.unit_techno.qr_entry_control_impl.entity.CardEntity;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_impl.entity.enums.CardStatus;
import ru.unit_techno.qr_entry_control_impl.mapper.EntryDeviceToReqRespMapper;
import ru.unit_techno.qr_entry_control_impl.repository.QrRepository;
import ru.unit_techno.qr_entry_control_impl.util.DateValidator;
import ru.unit_techno.qr_entry_control_impl.websocket.WSNotificationService;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class QrValidationService {

    private final QrRepository repository;
    private final DeviceResource deviceResource;
    private final BarrierFeignClient barrierFeignClient;
    private final EntryDeviceToReqRespMapper reqRespMapper;
    private final LogActionBuilder logActionBuilder;
    private final WSNotificationService notificationService;

    @SneakyThrows
    @Transactional
    public void parseQrCodeMessage(InputQrFromFirmware inputQrFromFirmware, Long deviceId) {
        Optional<QrCodeEntity> qrObj = repository.findByUuid(UUID.fromString(inputQrFromFirmware.getUUID()));

        //TODO проверять дату въезда, если вдруг человек заказал на завтра,приехал сегодня
        if (qrObj.isPresent()) {
            QrCodeEntity qrCodeEnt = qrObj.get();
            if (qrCodeEnt.getExpire()) {
                throw new Exception("QR code has expired! Try generate new QR code and use it! Expire date: " + qrCodeEnt.getExpire().toString());
            }

            DateValidator.checkQrEnteringDate(qrCodeEnt);

            //todo выдача карточки КАК В АЭРОПОРТУ и открытие шлагбаума(вызов прошивки) и получение номера карты
            /// TODO: 07.10.2021 Добавить функционал запроса прошивки

            qrCodeEnt.addCard(
                    new CardEntity()
                            .setCardValue("return value from firmware")
                            .setCardStatus(CardStatus.ISSUED)
            );
            qrCodeEnt.setExpire(true);

            DeviceResponseDto entryDevice = deviceResource.getGroupDevices(deviceId, DeviceType.QR);
            BarrierRequestDto barrierRequest = reqRespMapper.entryDeviceToRequest(entryDevice);
            BarrierResponseDto barrierResponse = barrierFeignClient.openBarrier(barrierRequest);

            logActionBuilder.buildActionObjectAndLogAction(barrierResponse.getBarrierId(),
                    qrCodeEnt.getQrId(),
                    qrCodeEnt.getGovernmentNumber(),
                    ActionStatus.UNKNOWN);

            repository.save(qrCodeEnt);
            log.info("qr codie is {}", qrCodeEnt);
        } else {
            notificationService.sendQrErrorScan(inputQrFromFirmware.getGovernmentNumber());
            logActionBuilder.buildActionObjectAndLogAction(deviceId,
                    //fixme есть риск записать не то, либо npe
                    qrObj.get().getQrId(),
                    inputQrFromFirmware.getGovernmentNumber(),
                    null,
                    true,
                    new Description()
                            .setErroredServiceName("QR")
                            .setMessage("We are no have this QR code in database. Try generate QR code on our website!"));
            throw new Exception("We are no have this QR code in database. Try generate QR code on our website!");
        }
    }
}
