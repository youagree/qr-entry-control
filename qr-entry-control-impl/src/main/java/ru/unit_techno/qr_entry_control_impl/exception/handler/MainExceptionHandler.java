
package ru.unit_techno.qr_entry_control_impl.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.unit_techno.qr_entry_control_impl.dto.ExceptionHandleDto;
import ru.unit_techno.qr_entry_control_impl.exception.DeliverySendException;

@Slf4j
@RestControllerAdvice
public class MainExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DeliverySendException.class)
    public void handleRfidAccessDeniedException() {
        log.error("failed when in process delivery");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionHandleDto handleIllegalArgumentException(Exception ex) {
        log.error("invalid input argument {}", ex.getMessage());
        return new ExceptionHandleDto().setStatusCode(400).setMessage(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleAllException(Exception ex) {
        log.error("failed when in process delivery");
        ex.printStackTrace();
        return ex.getMessage();
    }
}