package ru.unit_techno.qr_entry_control_impl.exception;

public class QrExpireException extends RuntimeException{
    public QrExpireException() {
        super();
    }

    public QrExpireException(String message) {
        super(message);
    }
}
