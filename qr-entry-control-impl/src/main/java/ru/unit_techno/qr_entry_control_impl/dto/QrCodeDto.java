
package ru.unit_techno.qr_entry_control_impl.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ru.unit_techno.qr_entry_control_impl.serializer.TimestampDateSerializer;

import java.sql.Timestamp;

@Data
public class QrCodeDto {
    private String email;
    private String governmentNumber;
    private String name;
    private String surname;
    @JsonSerialize(using = TimestampDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Timestamp enteringDate;
    //TODO добавить планируемую дату вьезда
}