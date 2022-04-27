package ru.unit_techno.qr_entry_control_impl.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.unit.techno.ariss.barrier.api.dto.BarrierRequestDto;
import ru.unit.techno.device.registration.api.DeviceResource;
import ru.unit.techno.device.registration.api.dto.DeviceResponseDto;
import ru.unit.techno.device.registration.api.dto.DeviceSourceTargetDto;
import ru.unit.techno.device.registration.api.enums.DeviceType;
import ru.unit_techno.qr_entry_control_impl.dto.InputQrFromFirmware;
import ru.unit_techno.qr_entry_control_impl.entity.CardEntity;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_impl.entity.enums.CardStatus;
import ru.unit_techno.qr_entry_control_impl.exception.CardServiceException;
import ru.unit_techno.qr_entry_control_impl.exception.QrExpireException;
import ru.unit_techno.qr_entry_control_impl.exception.QrNotFoundException;
import ru.unit_techno.qr_entry_control_impl.mapper.EntryDeviceToReqRespMapper;
import ru.unit_techno.qr_entry_control_impl.repository.QrRepository;
import ru.unit_techno.qr_entry_control_impl.util.DateValidator;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class QrValidationService {

    private final QrRepository repository;
    private final DeviceResource deviceResource;
    private final BarrierFeignService barrierFeignService;
    private final EntryDeviceToReqRespMapper reqRespMapper;
    private final HttpClientQr httpClientQr;
    private final QrEventService qrEventService;

    @Transactional
    public void parseQrCodeMessage(InputQrFromFirmware inputQrFromFirmware, Long deviceId) {
        log.info("input params {}, deviceId {}", inputQrFromFirmware, deviceId);
        QrCodeEntity qrCodeEnt = null;
        try {
            qrCodeEnt = repository.findByUuid(UUID.fromString(inputQrFromFirmware.getUUID()))
                    .orElseThrow(() -> new QrNotFoundException("qr not found in database"));
                if (qrCodeEnt.getExpire()) {

                    throw new QrExpireException("QR code has expired! Try generate new QR code and use it! Expire date: "
                            + qrCodeEnt.getExpire().toString());
                }

                DateValidator.checkQrEnteringDate(qrCodeEnt);

                //todo сделать метод
            DeviceSourceTargetDto cardColumn = deviceResource.getTargetDevice(deviceId, DeviceType.QR, DeviceType.CARD);

                qrCodeEnt.addCard(
                        new CardEntity()
                                .setCardValue(httpClientQr.requestToGiveCard(cardColumn))
                                .setCardStatus(CardStatus.ISSUED)
                );
                qrCodeEnt.setExpire(true);

                DeviceResponseDto entryDevice = deviceResource.getGroupDevices(deviceId, DeviceType.QR);
                BarrierRequestDto barrierRequest = reqRespMapper.entryDeviceToRequest(entryDevice);
                barrierFeignService.openBarrier(barrierRequest, qrCodeEnt);

                repository.save(qrCodeEnt);
                log.info("qr codie is {}", qrCodeEnt);
        } catch (CardServiceException | QrExpireException | FeignException e) {
            log.error("error is: {}", e.getMessage());
            qrEventService.logEventAndSendNotification(inputQrFromFirmware, deviceId,
                    Optional.ofNullable(qrCodeEnt).map(QrCodeEntity::getQrId).orElse(null));
            throw e;
        } catch (QrNotFoundException e) {
            log.error("error is: {}", e.getMessage());
            qrEventService.logEventAndSendNotification(inputQrFromFirmware, deviceId, null);
            throw e;
        }
    }
}
