
package ru.unit_techno.qr_entry_control_impl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.unit_techno.qr_entry_control_impl.dto.InputQrFromFirmware;
import ru.unit_techno.qr_entry_control_impl.dto.QrCodeDto;
import ru.unit_techno.qr_entry_control_impl.service.QrService;
import ru.unit_techno.qr_entry_control_impl.service.QrValidationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/ui/qr")
@RequiredArgsConstructor
public class QrController {

    private final QrService qrService;
    private final QrValidationService qrValidationService;

    @PostMapping("/createAndSend")
    public Long createQrAndSend(@Valid @RequestBody QrCodeDto qrCodeDto) {
        return qrService.createAndSendQrToEmail(qrCodeDto);
    }

    @PostMapping("/receiveQrCode/{deviceId}")
    public void receiveQrCode(@RequestBody InputQrFromFirmware inputQrFromFirmware, @PathVariable Long deviceId) {
        qrValidationService.parseQrCodeMessage(inputQrFromFirmware, deviceId);
    }
}