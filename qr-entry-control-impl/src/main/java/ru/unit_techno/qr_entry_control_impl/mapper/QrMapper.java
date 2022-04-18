
package ru.unit_techno.qr_entry_control_impl.mapper;

import jdk.jfr.Name;
import org.mapstruct.*;
import ru.unit_techno.qr_entry_control_impl.dto.QrCodeDto;
import ru.unit_techno.qr_entry_control_impl.dto.QrInfoDto;
import ru.unit_techno.qr_entry_control_impl.dto.service.QrObjectTemplateDto;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;

import java.time.*;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface QrMapper {

    @Mapping(target = "qrId", ignore = true)
    QrCodeEntity toDomain(QrCodeDto qrCodeDto);

    QrCodeDto toDto(QrCodeEntity qrCodeEntity);

    QrObjectTemplateDto toTempQrObject(QrCodeDto qrCodeDto);

    @Mappings({
            @Mapping(source = "qrDeliveryEntity.deliveryStatus", target = "deliveryStatus"),
            @Mapping(source = "enteringDate", target = "enteringDate", qualifiedByName = "mapTimeWithZone")
    })
    QrInfoDto entityToInfo(QrCodeEntity qrCodeEntity);

    @Named("mapTimeWithZone")
    default LocalDateTime mapTimeWithZone(LocalDateTime date) {
        Instant seconds = date.toInstant(ZoneOffset.UTC);
        ZoneId moscowTimeZone = ZoneId.of("Europe/Moscow");
        ZonedDateTime timeWithMoscowZone = seconds.atZone(moscowTimeZone);
        return LocalDateTime.from(timeWithMoscowZone);
    }
}