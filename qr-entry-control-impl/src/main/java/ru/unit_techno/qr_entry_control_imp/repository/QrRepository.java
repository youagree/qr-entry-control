
package ru.unit_techno.qr_entry_control_imp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.unit_techno.qr_entry_control_imp.entity.QrCodeEntity;

public interface QrRepository extends JpaRepository<QrCodeEntity, Long> {
}