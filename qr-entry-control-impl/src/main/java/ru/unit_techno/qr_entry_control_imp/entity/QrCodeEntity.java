
package ru.unit_techno.qr_entry_control_imp.entity;

import lombok.Data;
import ru.unit_techno.qr_entry_control_imp.entity.enums.DeliveryStatus;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "qr_code", schema = "qr_entry_control")
@SequenceGenerator(name = "qr_code_id_seq", sequenceName = "qr_code_id_seq")
public class QrCodeEntity {
    @Id
    @Column(name = "qr_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qr_code_id_seq")
    private Long qrId;
    @Basic
    @Column(name = "qr_picture")
    private String qrPicture;
    @Basic
    @Column(name = "creation_date")
    private Timestamp creationDate;
    @Basic
    @Column(name = "government_number")
    private String governmentNumber;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "surname")
    private String surname;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "expire")
    private Boolean expire;
    @Basic
    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;
    @Basic
    @Column(name = "entering_date")
    private Timestamp enteringDate;
}