
package ru.unit_techno.qr_entry_control_impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.unit_techno.qr_entry_control_impl.entity.CardEntity;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;

import java.util.Optional;
import java.util.UUID;

public interface QrRepository extends JpaRepository<QrCodeEntity, Long> {

    void deleteAllByExpireTrue();

    @Modifying
    @Query(value = "update qr_code set expire = true where (entering_date - now()) < (- interval '1 hour')",
           nativeQuery = true)
    void expireOldQrCodes();

    QrCodeEntity findByQrPicture(String qrPicture);

    Optional<QrCodeEntity> findByUuid(UUID uuid);

    @Modifying
    @Query("update QrCodeEntity q set q.card = null where q.card = :id")
    void returnCard(@Param("id") CardEntity id);

    QrCodeEntity findByCardId(Long cardId);
}