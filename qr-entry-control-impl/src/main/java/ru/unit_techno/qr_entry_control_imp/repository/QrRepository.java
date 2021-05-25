
package ru.unit_techno.qr_entry_control_imp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.unit_techno.qr_entry_control_imp.entity.QrCodeEntity;

import java.util.List;

public interface QrRepository extends JpaRepository<QrCodeEntity, Long> {

    void deleteAllByExpireTrue();
}