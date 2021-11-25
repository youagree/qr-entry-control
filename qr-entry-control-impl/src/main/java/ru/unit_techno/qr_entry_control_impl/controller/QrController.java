
package ru.unit_techno.qr_entry_control_impl.controller;

import lombok.RequiredArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.Between;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.unit_techno.qr_entry_control_impl.dto.InputQrFromFirmware;
import ru.unit_techno.qr_entry_control_impl.dto.QrCodeDto;
import ru.unit_techno.qr_entry_control_impl.dto.QrInfoDto;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;
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

    @PostMapping("/allQrCodesInfo")
    public Page<QrInfoDto> getQrCodesInfo(
            @And({@Spec(path = "qrId", params = "qrId", spec = Equal.class),
                    @Spec(path = "governmentNumber", params = "governmentNumber", spec = Equal.class),
                    @Spec(path = "enteringDate", params = "enteringDate", spec = Equal.class, config = "yyyy-MM-dd"),
                    @Spec(path = "fullName", params = "fullName", spec = Like.class)
            }) Specification<QrCodeEntity> specificationPageable, Pageable pageable) {
        return qrService.getAllQrCodesInfo(specificationPageable, pageable);
    }
}