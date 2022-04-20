
package ru.unit_techno.qr_entry_control_impl.util;

import lombok.experimental.UtilityClass;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_impl.exception.QrExpireException;

import java.time.LocalDate;

@UtilityClass
public class DateValidator {
    public void checkQrEnteringDate(QrCodeEntity qrCodeEnt) throws RuntimeException {
        LocalDate currentDate = LocalDate.now();
        if (qrCodeEnt.getEnteringDate().getYear() != currentDate.getYear() ||
                !qrCodeEnt.getEnteringDate().getMonth().equals(currentDate.getMonth()) ||
                qrCodeEnt.getEnteringDate().getDayOfMonth() != currentDate.getDayOfMonth()) {
            throw new QrExpireException("Entering date is not today!");
        }
    }
}