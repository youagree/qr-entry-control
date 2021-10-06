
package ru.unit_techno.qr_entry_control_impl.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import ru.unit_techno.qr_entry_control_impl.base.BaseTestClass;
import ru.unit_techno.qr_entry_control_impl.dto.QrCodeDto;

import java.time.LocalDate;

@DirtiesContext
public class QrControllerTest extends BaseTestClass {

    public static final String BASE_URL = "/ui/qr";

    @Test
    @DisplayName("валидация гос-номера")
    public void validGosNumberTest() {
        testUtils.invokePostApi(Void.class, BASE_URL + "/createAndSend", HttpStatus.BAD_REQUEST,
                new QrCodeDto()
                        .setEmail("a@ya.ru")
                        .setFullName("test_name")
                        .setGovernmentNumber("not_valid_number")
                        .setEnteringDate(LocalDate.now().plusYears(1L)));
    }

    @Test
    @DisplayName("валидация даты, что она не в прошлом")
    public void validEnteringDate() {
        testUtils.invokePostApi(Void.class, BASE_URL + "/createAndSend", HttpStatus.BAD_REQUEST,
                new QrCodeDto()
                        .setEmail("a@ya.ru")
                        .setFullName("test_name")
                        .setGovernmentNumber("А123АА 199")
                        .setEnteringDate(LocalDate.now().minusYears(1L)));
    }
}