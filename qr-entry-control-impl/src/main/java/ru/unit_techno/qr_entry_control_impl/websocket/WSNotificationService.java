package ru.unit_techno.qr_entry_control_impl.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.unit_techno.qr_entry_control_impl.dto.websocket.CardReturnEvent;
import ru.unit_techno.qr_entry_control_impl.dto.websocket.QrScanEvent;

@Service
@RequiredArgsConstructor
public class WSNotificationService {

    private final SimpMessagingTemplate brokerMessagingTemplate;

    @Value("${ws.topics.name}")
    private String eventsTopic;

    public void sendCardNotReturned(String governmentNumber) {
        brokerMessagingTemplate.convertAndSend(
                eventsTopic,
                new CardReturnEvent()
                        .setMessage("Не удалось зафиксировать возврат карточки")
                        .setGovernmentNumber("Номер автомобиля: " + governmentNumber)
        );
    }

    public void sendQrErrorScan(String governmentNumber) {
        brokerMessagingTemplate.convertAndSend(
                eventsTopic,
                new QrScanEvent()
                        .setMessage("Данный QR код не найден в системе")
                        .setGovernmentNumber("Номер автомобиля: " + governmentNumber)
        );
    }
}
