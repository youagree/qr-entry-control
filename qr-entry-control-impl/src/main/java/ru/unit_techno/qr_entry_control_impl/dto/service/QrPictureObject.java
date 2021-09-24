package ru.unit_techno.qr_entry_control_impl.dto.service;

import lombok.Data;

import java.util.HashMap;

@Data
public class QrPictureObject {
    private Long deliveryEntityId;
    private String filePath;
    private String qrImageInBase64;
    private HashMap<String, Object> metadataForSendMessage;
}
