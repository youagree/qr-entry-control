package ru.unit_techno.qr_entry_control_imp.service.listener;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.unit_techno.qr_entry_control_imp.config.SerialPortTemplate;
import ru.unit_techno.qr_entry_control_imp.service.QrValidationService;

import static java.lang.Thread.sleep;

@Component
@Slf4j
public class SerialPortListener implements SerialPortEventListener {

    private SerialPortTemplate serialPort;
    private QrValidationService qrValidationService;
    static StringBuilder sb = new StringBuilder();

    @SneakyThrows
    @Autowired
    public SerialPortListener(SerialPortTemplate serialPort, QrValidationService qrValidationService) {
        this.serialPort = serialPort;
        this.qrValidationService = qrValidationService;
    }

    @SneakyThrows
    public void serialEvent(SerialPortEvent event) {
        /*
         * Объект типа SerialPortEvent несёт в себе информацию о том какое событие
         * произошло и значение. Так например если пришли данные то метод
         * event.getEventValue() вернёт нам количество байт во входном буфере.
         */
        try {
            if (event.isRXCHAR()) {
                if (event.getEventValue() > 0) {
                    sleep(500);
                    byte[] buffer = serialPort.getSerialPort().readBytes();
                    if (buffer != null) {
                        sb.append(new String(buffer, "UTF-8"));
                        sleep(200);
                        buffer = serialPort.getSerialPort().readBytes();
                        if (buffer == null) {
                            qrValidationService.parseQrCodeMessage(sb.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.info("An error occurred while reading the QR code, message: {}", e.getMessage());
        }
    }
}
