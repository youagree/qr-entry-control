
package ru.unit_techno.qr_entry_control_impl.entity;

import lombok.Data;
import ru.unit_techno.qr_entry_control_impl.entity.enums.DeliveryStatus;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "qr_delivery_process", schema = "qr_entry_control")
@SequenceGenerator(name = "qr_delivery_process_seq", sequenceName = "qr_delivery_process_seq")
public class QrDeliveryEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qr_delivery_process_seq")
    private Long id;

    @Basic
    @Column(name = "message_tag")
    private UUID messageTag;

    @Basic
    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;
}