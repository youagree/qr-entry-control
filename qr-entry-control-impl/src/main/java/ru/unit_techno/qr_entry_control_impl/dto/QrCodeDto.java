
package ru.unit_techno.qr_entry_control_impl.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class QrCodeDto {
    @Email
    @NotNull
    private String email;
    @NotNull
    @Pattern(regexp = "^[АВЕКМНОРСТУХ]\\d{3}[АВЕКМНОРСТУХ]{2} \\d{2,3}$", message = "неправильный формат гос-номера")
    private String governmentNumber;
    private String fullName;
    @NotNull
    @FutureOrPresent(message = "время не может быть передано в прошлом времени")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate enteringDate;
}