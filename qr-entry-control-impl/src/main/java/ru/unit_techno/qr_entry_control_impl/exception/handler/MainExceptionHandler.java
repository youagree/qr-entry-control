
package ru.unit_techno.qr_entry_control_impl.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.unit_techno.qr_entry_control_impl.exception.DeliverySendException;

@Slf4j
@RestControllerAdvice
public class MainExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DeliverySendException.class)
    public void handleRfidAccessDeniedException() {
        log.error("failed when in process delivery");
    }
}