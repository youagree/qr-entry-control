package ru.unit_techno.qr_entry_control_impl.validation;

import org.junit.jupiter.api.Assertions;
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
import ru.unit_techno.qr_entry_control_impl.base.BaseTestClass;
import ru.unit_techno.qr_entry_control_impl.entity.CardEntity;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_impl.entity.QrDeliveryEntity;
import ru.unit_techno.qr_entry_control_impl.entity.enums.CardStatus;
import ru.unit_techno.qr_entry_control_impl.entity.enums.DeliveryStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class CardReturningValidation extends BaseTestClass {

    @MockBean
    private DeviceResource deviceResource;

    @MockBean
    private BarrierFeignClient barrierFeignClient;

    public static final String BASE_URL = "/api/card";

    @Test
    @DisplayName("Сдача карты, позитивный кейс")
    public void cardReturningPos() {
        UUID uuid = UUID.randomUUID();

        OngoingStubbing<DeviceResponseDto> deviceResponseDtoOngoingStubbing = Mockito.when(deviceResource.getGroupDevices(9999L, DeviceType.CARD))
                .thenReturn(new DeviceResponseDto().setEntryAddress("unknown").setDeviceId(1239L).setType("ENTRY"));

        BarrierRequestDto barrierRequestDto = reqRespMapper.entryDeviceToRequest(new DeviceResponseDto()
                .setEntryAddress("unknown")
                .setDeviceId(1239L)
                .setType("ENTRY"));

        Mockito.when(barrierFeignClient.openBarrier(barrierRequestDto))
                .thenReturn(new BarrierResponseDto()
                        .setBarrierId(1239L)
                        .setBarrierResponseStatus(null));

        QrDeliveryEntity save = qrDeliveryEntityRepository.save(new QrDeliveryEntity()
                .setId(3L)
                .setDeliveryStatus(DeliveryStatus.DELIVERED));

        CardEntity cardSave = cardRepository.save(new CardEntity().setId(2L)
                .setCardValue("тупорылый").setCardStatus(CardStatus.ISSUED));

        QrCodeEntity qrCode = qrRepository.save(new QrCodeEntity()
                .setExpire(false)
                .setName("Ignat")
                .setSurname("Zalupin")
                .setGovernmentNumber("А777АА 77")
                .setCreationDate(Timestamp.valueOf(LocalDateTime.now()))
                .setEmail("azaza@gmail.com")
                .setEnteringDate(Timestamp.valueOf(LocalDateTime.of(2021, 9, 24, 16, 0)))
                .setQrPicture("Picture")
                .setUuid(uuid)
                .setQrDeliveryEntity(save)
                .setCard(cardSave));

        String resultUrl = BASE_URL + "/return/" + "тупорылый?deviceId=9999";

        testUtils.invokePostApi(Void.class, resultUrl, HttpStatus.OK, null);

        Optional<QrCodeEntity> byId = qrRepository.findById(qrCode.getQrId());
        Optional<CardEntity> byId1 = cardRepository.findById(cardSave.getId());

        Assertions.assertNull(byId.get().getCard());
        Assertions.assertEquals(byId1.get().getCardStatus(), CardStatus.RETURNED);
    }
}
