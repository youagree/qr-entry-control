
package ru.unit_techno.qr_entry_control_imp.dto;

import lombok.Data;

@Data
public class QrCodeDto {
    private Long qrId;
    //base 64
    private String email;
    private String governmentNumber;
    private String name;
    private String surname;
    private Boolean expire;

    //TODO добавить планируемую дату вьезда

}