package ru.unit_techno.qr_entry_control_impl.dto.service;

import lombok.Data;

import java.util.UUID;

@Data
public class QrObjectTemplateDto {
    private Long id;
    private String uuid;
    private String governmentNumber;
    private String name;
    private String surname;
}
