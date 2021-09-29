package ru.unit_techno.qr_entry_control_impl.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.unit_techno.qr_entry_control_impl.entity.enums.CardStatus;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cards", schema = "qr_entry_control")
@SequenceGenerator(name = "card_id_seq", sequenceName = "card_id_seq")
public class CardEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_id_seq")
    private Long id;

    @Column(name = "card_value")
    private String cardValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_status")
    private CardStatus cardStatus;

    @OneToOne(mappedBy = "card")
    private QrCodeEntity qrCodeEntity;
}
