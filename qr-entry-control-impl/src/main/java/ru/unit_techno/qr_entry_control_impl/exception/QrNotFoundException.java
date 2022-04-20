package ru.unit_techno.qr_entry_control_impl.exception;

import javax.persistence.EntityNotFoundException;

public class QrNotFoundException extends EntityNotFoundException {
    public QrNotFoundException() {
        super();
    }

    public QrNotFoundException(String message) {
        super(message);
    }
}
