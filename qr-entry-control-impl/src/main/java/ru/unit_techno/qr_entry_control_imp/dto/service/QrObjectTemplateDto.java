package ru.unit_techno.qr_entry_control_imp.dto.service;

import lombok.Data;

@Data
public class QrObjectTemplateDto {
    private Long id;
    private String governmentNumber;
    private String name;
    private String surname;
    private Boolean expire;
}
