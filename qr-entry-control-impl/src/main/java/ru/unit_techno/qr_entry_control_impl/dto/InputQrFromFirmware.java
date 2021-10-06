
package ru.unit_techno.qr_entry_control_impl.dto;

import lombok.Data;
import ru.unit_techno.qr_entry_control_impl.dto.service.CardInfo;

@Data
public class InputQrFromFirmware {
    private String UUID;
    private String governmentNumber;
    private CardInfo cardInfo;
}