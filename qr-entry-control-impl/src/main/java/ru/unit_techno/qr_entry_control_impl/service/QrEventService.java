package ru.unit_techno.qr_entry_control_impl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.unit.techno.ariss.log.action.lib.api.LogActionBuilder;
import ru.unit.techno.ariss.log.action.lib.entity.Description;
import ru.unit.techno.ariss.log.action.lib.model.ActionStatus;
import ru.unit_techno.qr_entry_control_impl.dto.InputQrFromFirmware;
import ru.unit_techno.qr_entry_control_impl.websocket.WSNotificationService;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class QrEventService {
    private final LogActionBuilder logActionBuilder;
    private final WSNotificationService notificationService;

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void logEventAndSendNotification(InputQrFromFirmware inputQrFromFirmware,
                                            Long deviceId,
                                            Long commonId) {
        notificationService.sendQrErrorScan(inputQrFromFirmware.getGovernmentNumber(), deviceId);
        logActionBuilder.buildActionObjectAndLogAction(deviceId,
                commonId,
                inputQrFromFirmware.getGovernmentNumber(),
                ActionStatus.UNKNOWN,
                true,
                new Description()
                        .setErroredServiceName("qr-entry-control")
                        //set message from input args method
                        .setMessage("We are no have this QR code in database with uuid: " + inputQrFromFirmware.getUUID() +
                                ". Try generate QR code on our website!"));
    }
}
