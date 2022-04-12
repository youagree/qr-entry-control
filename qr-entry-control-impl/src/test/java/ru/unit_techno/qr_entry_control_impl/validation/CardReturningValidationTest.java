package ru.unit_techno.qr_entry_control_impl.validation;

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
import ru.unit_techno.qr_entry_control_impl.entity.CardEntity;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;
import ru.unit_techno.qr_entry_control_impl.entity.QrDeliveryEntity;
import ru.unit_techno.qr_entry_control_impl.entity.enums.CardStatus;
import ru.unit_techno.qr_entry_control_impl.entity.enums.DeliveryStatus;
import ru.unit_techno.qr_entry_control_impl.service.CardService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class CardReturningValidationTest extends BaseTestClass {

    @MockBean
    private DeviceResource deviceResource;

    @MockBean
    private BarrierFeignClient barrierFeignClient;

    @Autowired
    private CardService cardService;

    public static final String BASE_URL_QR = "/api/qr/";
    public static final String BASE_URL_CARD = "/api/card/";

    @Test
    @DisplayName("Сдача карты, позитивный кейс")
    public void cardReturningPos() {
        UUID uuid = UUID.randomUUID();

        Mockito.when(deviceResource.getGroupDevices(9999L, DeviceType.CARD))
                .thenReturn(new DeviceResponseDto().setEntryAddress("unknown").setDeviceId(1239L).setType("ENTRY"));

        BarrierRequestDto barrierRequestDto = reqRespMapper.entryDeviceToRequest(new DeviceResponseDto()
                .setEntryAddress("unknown")
                .setDeviceId(1239L)
                .setType("ENTRY"));

        Mockito.when(barrierFeignClient.openBarrier(barrierRequestDto))
                .thenReturn(new BarrierResponseDto()
                        .setBarrierId(1239L)
                        .setBarrierResponseStatus(null));

        CardEntity j = new CardEntity()
                .setCardValue("тупорылый")
                .setCardStatus(CardStatus.ISSUED);

        QrCodeEntity qrCodeEntity = new QrCodeEntity()
                .setExpire(false)
                .setFullName("Ignat")
                .setGovernmentNumber("А777АА 77")
                .setCreationDate(Timestamp.valueOf(LocalDateTime.now()))
                .setEmail("azaza@gmail.com")
                .setEnteringDate(LocalDate.now())
                .setQrPicture("Picture")
                .setUuid(uuid)
                .setQrDeliveryEntity(new QrDeliveryEntity()
                        .setDeliveryStatus(DeliveryStatus.DELIVERED));

        qrCodeEntity.addCard(j);

        QrCodeEntity qrCode = qrRepository.save(qrCodeEntity);

        String resultUrl = BASE_URL_CARD + "/return/" + "тупорылый?deviceId=9999";

        testUtils.invokePostApi(Void.class, resultUrl, HttpStatus.OK, null);

        Optional<QrCodeEntity> existQrCodeEntity = qrRepository.findById(qrCode.getQrId());
        Optional<CardEntity> deletedCard = cardRepository.findById(qrCodeEntity.getCard().getId());

        Assertions.assertNull(existQrCodeEntity.get().getCard());
        Assertions.assertTrue(deletedCard.isEmpty());
    }

    @Test
    @DisplayName("Сдача карты, негативный кейс с неправильной датой въезда/выезда")
    public void cardReturningNegativeDate() {
        UUID uuid = UUID.randomUUID();

        Mockito.when(deviceResource.getGroupDevices(9999L, DeviceType.CARD))
                .thenReturn(new DeviceResponseDto().setEntryAddress("unknown").setDeviceId(1239L).setType("ENTRY"));

        BarrierRequestDto barrierRequestDto = reqRespMapper.entryDeviceToRequest(new DeviceResponseDto()
                .setEntryAddress("unknown")
                .setDeviceId(1239L)
                .setType("ENTRY"));

        Mockito.when(barrierFeignClient.openBarrier(barrierRequestDto))
                .thenReturn(new BarrierResponseDto()
                        .setBarrierId(1239L)
                        .setBarrierResponseStatus(null));

        CardEntity j = new CardEntity()
                .setCardValue("тупорылый")
                .setCardStatus(CardStatus.ISSUED);

        QrCodeEntity qrCodeEntity = new QrCodeEntity()
                .setExpire(false)
                .setFullName("Ignat")
                .setGovernmentNumber("А777АА 77")
                .setCreationDate(Timestamp.valueOf(LocalDateTime.now()))
                .setEmail("azaza@gmail.com")
                .setEnteringDate(LocalDate.of(2021, 9, 24))
                .setQrPicture("Picture")
                .setUuid(uuid)
                .setQrDeliveryEntity(new QrDeliveryEntity()
                        .setDeliveryStatus(DeliveryStatus.DELIVERED));

        qrCodeEntity.addCard(j);

        QrCodeEntity qrCode = qrRepository.save(qrCodeEntity);

        Assertions.assertThrows(RuntimeException.class, () -> {
            cardService.returnCard("тупорылый", 9999L);
        });
    }
}
