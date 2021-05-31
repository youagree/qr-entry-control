package ru.unit_techno.qr_entry_control_imp.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.unit_techno.qr_entry_control_imp.repository.QrRepository;

@Component
@Slf4j
public class QrMaintenanceScheduler {

    private QrRepository qrRepository;

    @Autowired
    public QrMaintenanceScheduler(QrRepository qrRepository) {
        this.qrRepository = qrRepository;
    }

    @Scheduled(cron = "${qr-entry-control.cron.delete-expired-qr-job}", zone = "${qr-entry-control.cron.time-zone}")
    @Transactional
    public void deleteExpiredQr() {
        log.info("Start deleting expired QR codes");
        qrRepository.deleteAllByExpireTrue();
        log.info("Deleting successful");
    }

    @Scheduled(cron = "${qr-entry-control.cron.expiring-old-qr-job}", zone = "${qr-entry-control.cron.time-zone}")
    @Transactional
    public void expireOldQrCodes() {
        log.info("Start expiring old QR codes");
        qrRepository.expireOldQrCodes();
        log.info("QR has expired");
    }
}
