package ru.unit_techno.qr_entry_control_impl.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.unit.techno.device.registration.api.dto.DeviceSourceTargetDto;
import ru.unit_techno.qr_entry_control_impl.exception.CardServiceException;
import ru.unit_techno.qr_entry_control_impl.service.HttpClientQr;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Slf4j
@Component
@Profile("!test")
public class HttpClientProdImpl implements HttpClientQr {
    @Override
    public String requestToGiveCard(DeviceSourceTargetDto group) {
        try {
            log.info("start give card barrier, deviceId: {}", group.getDeviceId());
            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .connectTimeout(Duration.ofSeconds(15))
                    .build()
                    .send(HttpRequest.newBuilder()
                            .GET()
                            .uri(new URI("http://" + group.getAddress() + "/api/squd-core/qr/giveCard/" + group.getDeviceId()))
                            .build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException("trouble with giving card");
            }
            log.info("response is {}", response);
            return response.body();
        } catch (Exception e) {
            throw new CardServiceException("failed proccess giving card");
        }

    }
}
