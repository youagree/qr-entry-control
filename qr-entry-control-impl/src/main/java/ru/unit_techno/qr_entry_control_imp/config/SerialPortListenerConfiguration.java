package ru.unit_techno.qr_entry_control_imp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.unit_techno.qr_entry_control_imp.service.listener.SerialEventListenerAspectEnabler;

@Configuration
public class SerialPortListenerConfiguration {

    private SerialPortTemplate serialPorTemplate;

    @Autowired
    public SerialPortListenerConfiguration (SerialPortTemplate template) {
        this.serialPorTemplate = template;
    }


    @Bean
    public SerialEventListenerAspectEnabler createEventListener () {
        return new SerialEventListenerAspectEnabler(serialPorTemplate);
    }
}
