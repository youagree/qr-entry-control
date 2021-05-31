package ru.unit_techno.qr_entry_control_imp.service.listener;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

@Aspect
@Slf4j
public class SerialEventListenerAspectEnabler {

    private SerialPortListenerFacade facade;

    @Autowired
    public SerialEventListenerAspectEnabler(SerialPortListenerFacade facade) {
        this.facade = facade;
    }

    @AfterThrowing(value = "execution(public * ru.unit_techno.qr_entry_control_imp.service.*.*(..)) && @annotation(EventListenerCanCrash)", throwing = "e")
    private void activateSerialPortListener(JoinPoint joinPoint, Throwable e) {
        log.info("SerialPort exception occurred: {} \r\n Add new listener on port", e.getMessage());
        facade.enableEventListener();
    }
}
