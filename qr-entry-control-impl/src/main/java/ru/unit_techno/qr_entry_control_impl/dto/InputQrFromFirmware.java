
package ru.unit_techno.qr_entry_control_impl.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InputQrFromFirmware {
    private String UUID;
    private String governmentNumber;
}