
package ru.unit_techno.qr_entry_control_imp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.unit_techno.qr_entry_control_imp.serializer.TimestampDateSerializer;

import java.sql.Timestamp;

@Data
public class QrCodeDto {
    private String email;
    private String governmentNumber;
    private String name;
    private String surname;
    private Boolean expire;
    @JsonSerialize(using = TimestampDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Timestamp enteringDate;
    //TODO добавить планируемую дату вьезда
}