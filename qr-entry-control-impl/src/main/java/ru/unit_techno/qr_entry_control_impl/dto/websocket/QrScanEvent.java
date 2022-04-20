package ru.unit_techno.qr_entry_control_impl.dto.websocket;

import lombok.Data;

@Data
public class QrScanEvent {
    private String governmentNumber;
    private String message;
    private Long deviceId = null;
}
