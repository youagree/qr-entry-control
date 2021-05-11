package ru.unit_techno.qr_entry_control_imp.service.listener;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.unit_techno.qr_entry_control_imp.config.SerialPortTemplate;
import ru.unit_techno.qr_entry_control_imp.service.QrValidationService;

import static java.lang.Thread.sleep;

@Component
public class SerialPortListener implements SerialPortEventListener {

    SerialPortTemplate serialPort;
    private QrValidationService qrValidationService;
    private SerialPortListener listener;
    static StringBuilder sb = new StringBuilder();

    @SneakyThrows
    @Autowired
    public SerialPortListener(@Lazy SerialPortTemplate serialPort, QrValidationService qrValidationService, @Lazy SerialPortListener listener) {
        this.serialPort = serialPort;
        this.qrValidationService = qrValidationService;
        this.listener = listener;
    }

    @SneakyThrows
    public void serialEvent(SerialPortEvent event) {
        /*
         * Объект типа SerialPortEvent несёт в себе информацию о том какое событие
         * произошло и значение. Так например если пришли данные то метод
         * event.getEventValue() вернёт нам количество байт во входном буфере.
         */
        //todo решить проблему с убийством треда лиссенера при любой Runtime ошибке
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
    }
}
