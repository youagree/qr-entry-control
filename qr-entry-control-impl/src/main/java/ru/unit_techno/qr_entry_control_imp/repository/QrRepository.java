
package ru.unit_techno.qr_entry_control_imp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.unit_techno.qr_entry_control_imp.entity.QrCodeEntity;

import java.util.List;

public interface QrRepository extends JpaRepository<QrCodeEntity, Long> {

    void deleteAllByExpireTrue();

    @Modifying
    @Query(value = "update qr_code set expire = true where (now() - creation_date) > 2 * interval '1 day'", nativeQuery = true)
    void expireOldQrCodes();
}