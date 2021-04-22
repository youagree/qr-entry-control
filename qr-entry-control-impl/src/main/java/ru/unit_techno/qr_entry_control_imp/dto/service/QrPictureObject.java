package ru.unit_techno.qr_entry_control_imp.dto.service;

import lombok.Data;

@Data
public class QrPictureObject {
    private String filePath;
    private String qrImageInBase64;
    private DeliveryStatus status;

    private enum DeliveryStatus {
        DELIVERED ("QR код доставлен"),
        NOT_DELIVERED ("QR код не доставлен");

        private final String status;

        DeliveryStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }
}
