
package ru.unit_techno.qr_entry_control_impl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.unit_techno.qr_entry_control_impl.dto.QrCodeDto;
import ru.unit_techno.qr_entry_control_impl.dto.service.QrObjectTemplateDto;
import ru.unit_techno.qr_entry_control_impl.service.QrService;
import ru.unit_techno.qr_entry_control_impl.service.QrValidationService;

@RestController
@RequestMapping("/ui/qr")
@RequiredArgsConstructor
public class QrController {

    private final QrService qrService;
    private final QrValidationService qrValidationService;

    @PostMapping("/createAndSend")
    public Long createQrAndSend(@RequestBody QrCodeDto qrCodeDto) {
       return qrService.createAndSendQrToEmail(qrCodeDto);
    }

    @PostMapping("/receiveQrCode/{deviceId}")
    public void receiveQrCode(@RequestBody QrObjectTemplateDto qrCodeMessage, @PathVariable Long deviceId) {
        qrValidationService.parseQrCodeMessage(qrCodeMessage, deviceId);
    }
}