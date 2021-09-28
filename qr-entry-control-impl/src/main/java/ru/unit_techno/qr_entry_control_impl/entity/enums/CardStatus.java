package ru.unit_techno.qr_entry_control_impl.entity.enums;

import lombok.Getter;

public enum CardStatus {
    ISSUED("ISSUED"),
    RETURNED("RETURNED");

    @Getter
    private String value;

    CardStatus(String value) {
        this.value = value;
    }
}
