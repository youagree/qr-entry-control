package ru.unit_techno.qr_entry_control_impl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExceptionHandleDto {

    @JsonProperty("status_code")
    private Integer statusCode;
    private String message;
}
