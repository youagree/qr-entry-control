package ru.unit_techno.qr_entry_control_imp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.unit_techno.qr_entry_control_imp.service.listener.SerialEventListenerAspectEnabler;
import ru.unit_techno.qr_entry_control_imp.service.listener.SerialPortListenerFacade;

@Configuration
public class SerialPortListenerConfiguration {

    private SerialPortListenerFacade facade;

    @Autowired
    public SerialPortListenerConfiguration (SerialPortListenerFacade facade) {
        this.facade = facade;
    }


    @Bean
    public SerialEventListenerAspectEnabler createEventListener () {
        return new SerialEventListenerAspectEnabler(facade);
    }
}
