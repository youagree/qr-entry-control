package ru.unit_techno.qr_entry_control_impl.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
import ru.unit_techno.qr_entry_control_impl.entity.CardEntity;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_impl.entity.enums.CardStatus;
import ru.unit_techno.qr_entry_control_impl.mapper.EntryDeviceToReqRespMapper;
import ru.unit_techno.qr_entry_control_impl.repository.CardRepository;
import ru.unit_techno.qr_entry_control_impl.repository.QrRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final QrRepository qrRepository;
    private final CardRepository cardRepository;
    private final DeviceResource deviceResource;
    private final BarrierFeignClient barrierFeignClient;
    private final EntryDeviceToReqRespMapper reqRespMapper;
    private final LogActionBuilder logActionBuilder;

    @SneakyThrows
    @Transactional
    public void returnCard(String cardValue, Long deviceId) {
        Optional<CardEntity> byId = cardRepository.findByCardValue(cardValue);
        CardEntity card = null;

        if (byId.isPresent()) {
            card = byId.get();
        }

        if (card != null && card.getCardStatus() == CardStatus.ISSUED) {
            card.setCardStatus(CardStatus.RETURNED);
        } else {
            throw new EntityNotFoundException("Данная карта не найдена");
        }

        CardEntity save = cardRepository.save(card);
        QrCodeEntity qrCodeEntity = qrRepository.findByCardId(save.getId());

        try {
            qrRepository.returnCard(save);

            DeviceResponseDto entryDevice = deviceResource.getGroupDevices(deviceId, DeviceType.CARD);
            BarrierRequestDto barrierRequest = reqRespMapper.entryDeviceToRequest(entryDevice);
            BarrierResponseDto barrierResponse = barrierFeignClient.openBarrier(barrierRequest);

            logActionBuilder.buildActionObjectAndLogAction(barrierResponse.getBarrierId(),
                    qrCodeEntity.getQrId(),
                    qrCodeEntity.getGovernmentNumber(),
                    ActionStatus.UNKNOWN);
        } catch (Exception e) {
            /// TODO: 27.09.2021 Добавить оповещение в сокет
            /// TODO: 27.09.2021 Продумать возможные кейсы ошибок и эксепшенов, сделать обработки
            logActionBuilder.buildActionObjectAndLogAction(deviceId,
                    qrCodeEntity.getQrId(),
                    qrCodeEntity.getGovernmentNumber(),
                    ActionStatus.UNKNOWN,
                    true,
                    new Description()
                            .setErroredServiceName("QR")
                            .setMessage("Some problems while returning card in column. Error message: " + e.getMessage()));
        }

    }
}
