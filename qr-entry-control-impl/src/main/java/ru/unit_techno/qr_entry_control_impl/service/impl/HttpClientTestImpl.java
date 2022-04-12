package ru.unit_techno.qr_entry_control_impl.service.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.unit.techno.device.registration.api.dto.DeviceResponseDto;
import ru.unit_techno.qr_entry_control_impl.service.HttpClientQr;

import java.util.UUID;

@Component
@Profile("test")
public class HttpClientTestImpl implements HttpClientQr {
    @Override
    public String requestToGiveCard(DeviceResponseDto group) {
        return UUID.randomUUID().toString();
    }
}
