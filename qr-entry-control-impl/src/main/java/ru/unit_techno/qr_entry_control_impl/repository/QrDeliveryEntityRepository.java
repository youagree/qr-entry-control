
package ru.unit_techno.qr_entry_control_impl.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.unit_techno.qr_entry_control_impl.entity.QrDeliveryEntity;
import ru.unit_techno.qr_entry_control_impl.entity.enums.DeliveryStatus;

import java.util.List;

public interface QrDeliveryEntityRepository extends JpaRepository<QrDeliveryEntity, Long> {

    @Modifying
    @Query("update QrDeliveryEntity set deliveryStatus = :deliveryStatus where id = :id")
    void updateStatus(@Param("id") Long id, @Param("deliveryStatus") DeliveryStatus deliveryStatus);

    @Modifying
    @Query("update QrDeliveryEntity set deliveryStatus = :deliveryStatus where id in (:id)")
    void updateStatuses(@Param("id") List<Long> id, @Param("deliveryStatus") DeliveryStatus deliveryStatus);
}