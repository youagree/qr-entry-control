
package ru.unit_techno.qr_entry_control_imp.service;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.unit_techno.qr_entry_control_imp.dto.service.QrPictureObject;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessageStorageService {
    @Getter
    private final ConcurrentHashMap<String, QrPictureObject> notDeliveryQr = new ConcurrentHashMap<>();

    public void putNotDeliveryMessage(String to, QrPictureObject qrPictureObject) {
        notDeliveryQr.putIfAbsent(to, qrPictureObject);
    }

    public void deleteSuccessDeliveryMessage(String keyOfMessage) {
        notDeliveryQr.remove(keyOfMessage);
    }
}