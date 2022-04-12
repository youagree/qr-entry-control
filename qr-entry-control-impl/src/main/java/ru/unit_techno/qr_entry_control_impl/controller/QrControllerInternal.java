package ru.unit_techno.qr_entry_control_impl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.unit_techno.qr_entry_control_impl.dto.InputQrFromFirmware;
import ru.unit_techno.qr_entry_control_impl.service.QrValidationService;

@RestController
@RequestMapping("/api/qr")
@RequiredArgsConstructor
public class QrControllerInternal {

    private final QrValidationService qrValidationService;

    @PostMapping("/receiveQrCode/{deviceId}")
    public void receiveQrCode(@RequestBody InputQrFromFirmware inputQrFromFirmware, @PathVariable Long deviceId) {
        qrValidationService.parseQrCodeMessage(inputQrFromFirmware, deviceId);
    }
}
