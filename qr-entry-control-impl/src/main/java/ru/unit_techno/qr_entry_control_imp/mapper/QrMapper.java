
package ru.unit_techno.qr_entry_control_imp.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.unit_techno.qr_entry_control_imp.dto.QrCodeDto;
import ru.unit_techno.qr_entry_control_imp.dto.service.QrObjectTemplateDto;
import ru.unit_techno.qr_entry_control_imp.entity.QrCodeEntity;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface QrMapper {

    @Mapping(target = "qrId", ignore = true)
    QrCodeEntity toDomain(QrCodeDto qrCodeDto);

    QrCodeDto toDto(QrCodeEntity qrCodeEntity);

    QrObjectTemplateDto toTempQrObject(QrCodeDto qrCodeDto);

    default Boolean setExpire(Boolean expire){
        return false;
    }



}