
package ru.unit_techno.qr_entry_control_imp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.unit_techno.qr_entry_control_imp.entity.QrDeliveryEntity;
import ru.unit_techno.qr_entry_control_imp.entity.enums.DeliveryStatus;

import java.util.List;
import java.util.UUID;

public interface QrDeliveryEntityRepository extends JpaRepository<QrDeliveryEntity, Long> {

    @Modifying
    @Query("update QrDeliveryEntity set deliveryStatus = :deliveryStatus where messageTag = :uuid")
    void updateStatus(@Param("uuid") UUID uuid, @Param("deliveryStatus") DeliveryStatus deliveryStatus);

    @Modifying
    @Query("update QrDeliveryEntity set deliveryStatus = :deliveryStatus where messageTag in (:uuid)")
    void updateStatuses(@Param("uuid") List<UUID> uuid, @Param("deliveryStatus") DeliveryStatus deliveryStatus);
}