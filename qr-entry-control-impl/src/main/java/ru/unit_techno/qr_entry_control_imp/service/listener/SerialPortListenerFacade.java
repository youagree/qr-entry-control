package ru.unit_techno.qr_entry_control_imp.service.listener;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.unit_techno.qr_entry_control_imp.config.SerialPortTemplate;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Slf4j
@Data
@Component
public class SerialPortListenerFacade {


    private final SerialPortTemplate serialPort;
    private final SerialPortListener listener;

    @SneakyThrows
    public void enableEventListener() {
        serialPort.getSerialPort().addEventListener(listener);
        log.info("Listener has restored");
    }

    @PostConstruct
    @SneakyThrows
    public void initCom() {
        serialPort.init();
        serialPort.getSerialPort().addEventListener(listener);
    }
}
