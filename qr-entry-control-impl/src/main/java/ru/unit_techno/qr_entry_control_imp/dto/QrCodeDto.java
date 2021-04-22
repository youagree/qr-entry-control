
package ru.unit_techno.qr_entry_control_imp.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class QrCodeDto {
    private Long qrId;
    //base 64
    private Timestamp creationDate;
    private String governmentNumber;
    private String name;
    private String surname;
    private Boolean expire;
}