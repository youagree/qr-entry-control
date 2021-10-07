package ru.unit_techno.qr_entry_control_impl.validation;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import ru.unit.techno.ariss.barrier.api.BarrierFeignClient;
import ru.unit.techno.ariss.barrier.api.dto.BarrierRequestDto;
import ru.unit.techno.ariss.barrier.api.dto.BarrierResponseDto;
import ru.unit.techno.device.registration.api.DeviceResource;
import ru.unit.techno.device.registration.api.dto.DeviceResponseDto;
import ru.unit.techno.device.registration.api.enums.DeviceType;
import ru.unit_techno.qr_entry_control_impl.base.BaseTestClass;
import ru.unit_techno.qr_entry_control_impl.dto.InputQrFromFirmware;
import ru.unit_techno.qr_entry_control_impl.entity.CardEntity;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_impl.entity.QrDeliveryEntity;
import ru.unit_techno.qr_entry_control_impl.entity.enums.CardStatus;
import ru.unit_techno.qr_entry_control_impl.entity.enums.DeliveryStatus;
import ru.unit_techno.qr_entry_control_impl.service.QrValidationService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class QrValidationTest extends BaseTestClass {

    @MockBean
    private DeviceResource deviceResource;

    @MockBean
    private BarrierFeignClient barrierFeignClient;

    @Autowired
    private QrValidationService qrValidationService;

    public static final String BASE_URL = "/ui/qr";

    @SneakyThrows
    @Test
    @DisplayName("Скан QR кода и валидация. Позитивный кейс")
    public void receiveQrMessageTest() {
        UUID uuid = UUID.randomUUID();
        Mockito.when(deviceResource.getGroupDevices(7765L, DeviceType.QR))
                .thenReturn(new DeviceResponseDto().setEntryAddress("unknown").setDeviceId(1239L).setType("ENTRY"));

        BarrierRequestDto barrierRequestDto = reqRespMapper.entryDeviceToRequest(new DeviceResponseDto()
                .setEntryAddress("unknown")
                .setDeviceId(1239L)
                .setType("ENTRY"));

        Mockito.when(barrierFeignClient.openBarrier(barrierRequestDto))
                .thenReturn(new BarrierResponseDto()
                        .setBarrierId(1239L)
                        .setBarrierResponseStatus(null));

        qrRepository.save(new QrCodeEntity()
                .setExpire(false)
                .setFullName("Ignat")
                .setGovernmentNumber("А777АА 77")
                .setCreationDate(Timestamp.valueOf(LocalDateTime.now()))
                .setEmail("azaza@gmail.com")
                .setEnteringDate(LocalDate.now())
                .setQrPicture("Picture")
                .setUuid(uuid)
                .setQrDeliveryEntity(new QrDeliveryEntity()
                        .setDeliveryStatus(DeliveryStatus.DELIVERED)));

        InputQrFromFirmware inputQrFromFirmware = new InputQrFromFirmware()
                .setUUID(String.valueOf(uuid))
                .setGovernmentNumber("А777АА 77");

        String resultUrl = BASE_URL + "/receiveQrCode/7765";

        testUtils.invokePostApi(Void.class, resultUrl, HttpStatus.OK, inputQrFromFirmware);

        List<CardEntity> all = cardRepository.findAll();
        Assertions.assertEquals(all.size(), 1);

        CardEntity cardEntity = all.get(0);
        Assertions.assertEquals(cardEntity.getCardValue(), "return value from firmware");
        Assertions.assertEquals(cardEntity.getCardStatus(), CardStatus.ISSUED);
        Assertions.assertNotNull(cardEntity.getQrCodeEntity());


        /// TODO: 24.09.2021 Добавить ассерты на эвенты
    }

    @SneakyThrows
    @Test
    @DisplayName("Скан QR кода и валидация. Негативный кейс с неправильной датой въезда/выезда")
    public void receiveQrMessageTestNegativeDate() {
        UUID uuid = UUID.randomUUID();
        Mockito.when(deviceResource.getGroupDevices(7765L, DeviceType.QR))
                .thenReturn(new DeviceResponseDto().setEntryAddress("unknown").setDeviceId(1239L).setType("ENTRY"));

        BarrierRequestDto barrierRequestDto = reqRespMapper.entryDeviceToRequest(new DeviceResponseDto()
                .setEntryAddress("unknown")
                .setDeviceId(1239L)
                .setType("ENTRY"));

        Mockito.when(barrierFeignClient.openBarrier(barrierRequestDto))
                .thenReturn(new BarrierResponseDto()
                        .setBarrierId(1239L)
                        .setBarrierResponseStatus(null));

        qrRepository.save(new QrCodeEntity()
                .setExpire(false)
                .setFullName("Ignat")
                .setGovernmentNumber("А777АА 77")
                .setCreationDate(Timestamp.valueOf(LocalDateTime.now()))
                .setEmail("azaza@gmail.com")
                .setEnteringDate(LocalDate.of(2021, 9, 24))
                .setQrPicture("Picture")
                .setUuid(uuid)
                .setQrDeliveryEntity(new QrDeliveryEntity()
                        .setDeliveryStatus(DeliveryStatus.DELIVERED)));

        InputQrFromFirmware inputQrFromFirmware = new InputQrFromFirmware()
                .setUUID(String.valueOf(uuid))
                .setGovernmentNumber("А777АА 77");

        Assertions.assertThrows(RuntimeException.class, () -> {
            qrValidationService.parseQrCodeMessage(inputQrFromFirmware, 7765L);
        });
    }
}
