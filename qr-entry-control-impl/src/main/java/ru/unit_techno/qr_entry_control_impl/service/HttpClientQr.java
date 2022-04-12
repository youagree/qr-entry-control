package ru.unit_techno.qr_entry_control_impl.service;

import ru.unit.techno.device.registration.api.dto.DeviceResponseDto;

public interface HttpClientQr {
    String requestToGiveCard(DeviceResponseDto group);
}
