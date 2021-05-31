
package ru.unit_techno.qr_entry_control_imp.entity.enums;

import lombok.Getter;
import lombok.Setter;

public enum DeliveryStatus {
    DELIVERED("DELIVERED"),
    NOT_DELIVERED("NOT_DELIVERED");

    @Getter
    @Setter
    private String value;

    DeliveryStatus(String value) {
        this.value = value;
    }
}