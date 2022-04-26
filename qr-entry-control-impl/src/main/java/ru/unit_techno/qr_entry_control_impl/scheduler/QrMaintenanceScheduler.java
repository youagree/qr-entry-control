package ru.unit_techno.qr_entry_control_impl.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import ru.unit_techno.qr_entry_control_impl.repository.QrRepository;

//@Component
@Slf4j
@RequiredArgsConstructor
public class QrMaintenanceScheduler {

    private final QrRepository qrRepository;

    @Scheduled(cron = "${qr-entry-control.cron.delete-expired-qr-job}", zone = "${qr-entry-control.cron.time-zone}")
    @Transactional
    public void deleteExpiredQr() {
        //todo не забыть проверку, что карточка не null;
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
