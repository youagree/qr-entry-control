package ru.unit_techno.qr_entry_control_impl.validation;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import ru.unit.techno.ariss.barrier.api.BarrierFeignClient;
import ru.unit.techno.ariss.barrier.api.dto.BarrierRequestDto;
import ru.unit.techno.ariss.barrier.api.dto.BarrierResponseDto;
import ru.unit.techno.device.registration.api.DeviceResource;
import ru.unit.techno.device.registration.api.dto.DeviceResponseDto;
import ru.unit.techno.device.registration.api.enums.DeviceType;
import ru.unit_techno.qr_entry_control_impl.dto.service.QrObjectTemplateDto;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_impl.entity.QrDeliveryEntity;
import ru.unit_techno.qr_entry_control_impl.entity.enums.DeliveryStatus;
import ru.unit_techno.qr_entry_control_impl.base.BaseTestClass;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class QrValidationTest extends BaseTestClass {



    @MockBean
    private DeviceResource deviceResource;

    @MockBean
    private BarrierFeignClient barrierFeignClient;

    public static final String BASE_URL = "/ui/qr";

    @SneakyThrows
    @Test
    @DisplayName("Скан QR кода и валидация. Позитивный кейс")
    public void receiveQrMessageTest() {
        UUID uuid = UUID.randomUUID();
        OngoingStubbing<DeviceResponseDto> deviceResponseDtoOngoingStubbing = Mockito.when(deviceResource.getGroupDevices(7765L, DeviceType.QR))
                .thenReturn(new DeviceResponseDto().setEntryAddress("unknow").setDeviceId(1239L).setType("ENTRY"));

        BarrierRequestDto barrierRequestDto = reqRespMapper.entryDeviceToRequest(new DeviceResponseDto().setEntryAddress("unknow").setDeviceId(1239L).setType("ENTRY"));
        Mockito.when(barrierFeignClient.openBarrier(barrierRequestDto)).thenReturn(new BarrierResponseDto().setBarrierId(1239L).setBarrierResponseStatus(null));

        qrRepository.save(new QrCodeEntity()
                .setExpire(false)
                .setName("Ignat")
                .setSurname("Zalupin")
                .setGovernmentNumber("А777АА 77")
                .setCreationDate(Timestamp.valueOf(LocalDateTime.now()))
                .setEmail("azaza@gmail.com")
                .setEnteringDate(Timestamp.valueOf(LocalDateTime.of(2021, 9, 24, 16, 0)))
                .setQrPicture("Picture")
                .setUuid(uuid)
                .setQrDeliveryEntity(new QrDeliveryEntity()
                        .setId(3L)
                        .setDeliveryStatus(DeliveryStatus.DELIVERED)));

        QrObjectTemplateDto qrObjectTemplateDto = new QrObjectTemplateDto()
                .setUuid(String.valueOf(uuid))
                .setGovernmentNumber("А777АА 77")
                .setSurname("Zalupin")
                .setName("Ignat");

        String resultUrl = BASE_URL + "/receiveQrCode/7765";

        testUtils.invokePostApi(Void.class, resultUrl, HttpStatus.OK, qrObjectTemplateDto);
        /// TODO: 24.09.2021 Добавить ассерты на эвенты, и может еще что-то проверить, доделать тест
    }
}
