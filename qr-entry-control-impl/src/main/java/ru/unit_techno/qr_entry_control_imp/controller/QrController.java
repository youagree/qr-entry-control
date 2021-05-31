
package ru.unit_techno.qr_entry_control_imp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.unit_techno.qr_entry_control_imp.dto.QrCodeDto;
import ru.unit_techno.qr_entry_control_imp.service.QrService;

@RestController
@RequestMapping("ui/qr/")
@RequiredArgsConstructor
public class QrController {

    private final QrService qrService;

    @PostMapping("/createAndSend")
    public Long createQrAndSend(@RequestBody QrCodeDto qrCodeDto){
       return qrService.createAndSendQrToEmail(qrCodeDto);
    }
}