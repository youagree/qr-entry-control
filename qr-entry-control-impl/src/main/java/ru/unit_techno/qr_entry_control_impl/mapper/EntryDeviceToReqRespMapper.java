package ru.unit_techno.qr_entry_control_impl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.unit.techno.ariss.barrier.api.dto.BarrierRequestDto;
import ru.unit.techno.device.registration.api.dto.DeviceResponseDto;

@Mapper
public interface EntryDeviceToReqRespMapper {

    @Mappings({
            @Mapping(source = "deviceId", target = "barrierId"),
            @Mapping(source = "entryAddress", target = "barrierCoreAddress")
    })
    BarrierRequestDto entryDeviceToRequest(DeviceResponseDto responseDto);
}
