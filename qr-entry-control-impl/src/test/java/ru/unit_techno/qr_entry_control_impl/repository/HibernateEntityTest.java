
package ru.unit_techno.qr_entry_control_impl.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;
import ru.unit_techno.qr_entry_control_impl.base.BaseTestClass;
import ru.unit_techno.qr_entry_control_impl.base.ObjectTestGenerator;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;

import javax.transaction.Transactional;
import java.util.Optional;

public class HibernateEntityTest extends BaseTestClass {

    @Autowired
    protected TransactionTemplate txTemplate;

    @Test
    public void parentEntityNotDelete() {
        QrCodeEntity testQr = ObjectTestGenerator.getTestQr();
        qrRepository.save(testQr);

        deleteCard(testQr.getCard().getId());
        Optional<QrCodeEntity> byUuid = qrRepository.findByUuid(testQr.getUuid());
        Assertions.assertFalse(byUuid.isEmpty());
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }
}