
package ru.unit_techno.qr_entry_control_imp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.unit_techno.qr_entry_control_imp.entity.QrCodeEntity;

public interface QrRepository extends JpaRepository<QrCodeEntity, Long> {

    void deleteAllByExpireTrue();

    @Modifying
    @Query(value = "update qr_code set expire = true where (entering_date - now()) < (- interval '1 hour')",
           nativeQuery = true)
    void expireOldQrCodes();

    QrCodeEntity findByQrPicture(String qrPicture);
}