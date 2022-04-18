
package ru.unit_techno.qr_entry_control_impl.dto;

import static ru.unit_techno.qr_entry_control_impl.util.Constant.REGEX;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class QrCodeDto {
    @Email
    @NotNull
    private String email;
    @NotNull
    @Pattern(regexp = REGEX,
            message = "неправильный формат гос-номера")
    private String governmentNumber;
    private String fullName;
    @NotNull
    @FutureOrPresent(message = "время не может быть передано в прошлом времени")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime enteringDate;
}