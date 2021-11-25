
package ru.unit_techno.qr_entry_control_impl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;

import java.util.Optional;
import java.util.UUID;

public interface QrRepository extends JpaRepository<QrCodeEntity, Long>, JpaSpecificationExecutor<QrCodeEntity> {

    @Query(value = "delete from QrCodeEntity q where q.expire = true and q.card is null")
    @Modifying
    void deleteAllByExpireTrue();

    @Modifying
    @Query(value = "update qr_code set expire = true where (entering_date - now()) < (- interval '1 hour')",
           nativeQuery = true)
    void expireOldQrCodes();

    Optional<QrCodeEntity> findByUuid(UUID uuid);

    QrCodeEntity findByCardId(Long cardId);

    Page<QrCodeEntity> findAllByExpireFalse(@Nullable Specification<QrCodeEntity> specification, Pageable pageable);
}