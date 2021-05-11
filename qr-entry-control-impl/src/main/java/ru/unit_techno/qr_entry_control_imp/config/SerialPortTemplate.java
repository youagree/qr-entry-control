package ru.unit_techno.qr_entry_control_imp.config;

import jssc.SerialPort;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.unit_techno.qr_entry_control_imp.service.listener.SerialPortListener;

import javax.annotation.PostConstruct;

@Component
@Data
public class SerialPortTemplate {
    @Value("${qr-entry-control.comport-name}")
    private String comName;
    private SerialPort serialPort;
    private SerialPortListener serialPortListener;

    @Autowired
    public SerialPortTemplate(SerialPortListener serialPortListener) {
        this.serialPortListener = serialPortListener;
    }

    @SneakyThrows
    @PostConstruct
    public void init() {
        serialPort = new SerialPort(comName);
        serialPort.openPort();
        serialPort.setParams(115200, 8, 1, SerialPort.PARITY_NONE);
        serialPort.addEventListener(serialPortListener);
    }
}
