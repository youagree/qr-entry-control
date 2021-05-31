package ru.unit_techno.qr_entry_control_imp.dto.service;

import lombok.Data;

import java.util.HashMap;
import java.util.UUID;

@Data
public class QrPictureObject {
    private UUID messageTag;
    private String filePath;
    private String qrImageInBase64;
    private HashMap<String, Object> metadataForSendMessage;
}
