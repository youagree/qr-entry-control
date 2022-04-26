
package ru.unit_techno.qr_entry_control_impl.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

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
    @Column(name = "full_name")
    private String fullName;
    @Basic
    @Column
    private String email;
    @Basic
    @Column
    //todo constraint not null
    private Boolean expire;
    @Basic
    @Column
    //todo constraint not null
    private UUID uuid;
    @Basic
    @Column(name = "entering_date")
    private LocalDate enteringDate;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "qr_delivery_id", referencedColumnName = "id")
    private QrDeliveryEntity qrDeliveryEntity;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "card", referencedColumnName = "id")
    private CardEntity card;

    public void addCard(CardEntity cardEntity) {
        this.setCard(cardEntity);
        cardEntity.setQrCodeEntity(this);
    }
}