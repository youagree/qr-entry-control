package ru.unit_techno.qr_entry_control_impl.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.unit.techno.ariss.barrier.api.BarrierFeignClient;
import ru.unit.techno.ariss.barrier.api.dto.BarrierRequestDto;
import ru.unit.techno.ariss.log.action.lib.api.LogActionBuilder;
import ru.unit.techno.ariss.log.action.lib.config.DeviceEventConfig;
import ru.unit.techno.ariss.log.action.lib.entity.Description;
import ru.unit.techno.ariss.log.action.lib.model.ActionStatus;
import ru.unit.techno.ariss.log.action.lib.model.MetaObject;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_impl.websocket.WSNotificationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class BarrierFeignService {

    private final BarrierFeignClient barrierFeignClient;
    private final WSNotificationService notificationService;
    private final LogActionBuilder logActionBuilder;
    private final QrEventService qrEventService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void openBarrier(BarrierRequestDto barrierRequestDto, QrCodeEntity qrCodeEntity) {
        try {
            barrierFeignClient.openBarrier(barrierRequestDto);
            logActionBuilder.buildActionObjectAndLogAction(barrierRequestDto.getBarrierId(),
                    qrCodeEntity.getQrId(),
                    qrCodeEntity.getGovernmentNumber(),
                    ActionStatus.UNKNOWN);
        } catch (FeignException e) {
            // todo switch to qrEventService
            notificationService.openBarrierError(qrCodeEntity.getGovernmentNumber(), barrierRequestDto.getBarrierId());
            log.error("Service not available", e);
            logActionBuilder.buildActionObjectAndLogAction(barrierRequestDto.getBarrierId(),
                    qrCodeEntity.getQrId(),
                    qrCodeEntity.getGovernmentNumber(),
                    ActionStatus.UNKNOWN,
                    true,
                    new Description().setStatusCode(String.valueOf(e.status()))
                            .setMessage(e.getMessage()).setErroredServiceName(e.request().url()));

        }
    }
}
