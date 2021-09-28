package ru.unit_techno.qr_entry_control_impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.unit_techno.qr_entry_control_impl.entity.CardEntity;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {

    Optional<CardEntity> findByCardValue(String cardValue);
}
