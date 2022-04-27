package ru.unit_techno.qr_entry_control_impl.service;

import ru.unit.techno.device.registration.api.dto.DeviceSourceTargetDto;

public interface HttpClientQr {
    String requestToGiveCard(DeviceSourceTargetDto group);
}
