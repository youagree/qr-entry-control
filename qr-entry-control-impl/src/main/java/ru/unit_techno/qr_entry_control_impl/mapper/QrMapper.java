
package ru.unit_techno.qr_entry_control_impl.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.unit_techno.qr_entry_control_impl.dto.QrCodeDto;
import ru.unit_techno.qr_entry_control_impl.dto.QrInfoDto;
import ru.unit_techno.qr_entry_control_impl.dto.service.QrObjectTemplateDto;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface QrMapper {

    @Mapping(target = "qrId", ignore = true)
    QrCodeEntity toDomain(QrCodeDto qrCodeDto);

    QrCodeDto toDto(QrCodeEntity qrCodeEntity);

    QrObjectTemplateDto toTempQrObject(QrCodeDto qrCodeDto);

    @Mapping(source = "qrDeliveryEntity.deliveryStatus", target = "deliveryStatus")
    QrInfoDto entityToInfo(QrCodeEntity qrCodeEntity);
}