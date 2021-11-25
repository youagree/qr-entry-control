
package ru.unit_techno.qr_entry_control_impl.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.unit_techno.qr_entry_control_impl.entity.QrCodeEntity;

@UtilityClass
public class SpecUtils {
    public Specification<QrCodeEntity> expireFalse(Specification<QrCodeEntity> qrCodeEntitySpecification) {
        if (qrCodeEntitySpecification == null) {
            return buildSpecExpireFalse();
        }
        return qrCodeEntitySpecification.and(buildSpecExpireFalse());
    }

    private Specification<QrCodeEntity> buildSpecExpireFalse() {
        return (r, cq, cb) -> cb.equal(r.get("expire"), false);
    }
}