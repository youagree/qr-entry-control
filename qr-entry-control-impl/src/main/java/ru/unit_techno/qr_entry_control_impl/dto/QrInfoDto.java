package ru.unit_techno.qr_entry_control_impl.dto;

import lombok.Data;
import ru.unit_techno.qr_entry_control_impl.entity.enums.DeliveryStatus;

import java.time.LocalDate;

@Data
public class QrInfoDto {

    private Long qrId;
    private LocalDate enteringDate;
    private String governmentNumber;
    private String fullName;
    private String email;
    private DeliveryStatus deliveryStatus;
}
