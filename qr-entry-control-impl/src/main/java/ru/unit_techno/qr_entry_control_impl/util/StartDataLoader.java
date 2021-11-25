package ru.unit_techno.qr_entry_control_impl.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.unit.techno.ariss.log.action.lib.repository.EventRepository;
import ru.unit_techno.qr_entry_control_impl.repository.QrDeliveryEntityRepository;
import ru.unit_techno.qr_entry_control_impl.repository.QrRepository;

import java.util.Objects;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class StartDataLoader implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;
    private final EventRepository repository;
    private final QrRepository qrRepository;
    private final QrDeliveryEntityRepository qrDeliveryEntityRepository;
    private final EventRepository eventRepository;

    @Override
    public void run(String... args) {
        eventRepository.deleteAll();
        repository.flush();
        qrDeliveryEntityRepository.deleteAll();
        qrDeliveryEntityRepository.flush();
        qrRepository.deleteAll();
        qrRepository.flush();

        Resource resource = new ClassPathResource("test-data-events.sql");
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(resource);
        populator.setSqlScriptEncoding("UTF-8");
        populator.execute(Objects.requireNonNull(jdbcTemplate.getDataSource()));

        Resource resource1 = new ClassPathResource("test-data-delivery.sql");
        ResourceDatabasePopulator populator1 = new ResourceDatabasePopulator(resource1);
        populator1.setSqlScriptEncoding("UTF-8");
        populator1.execute(Objects.requireNonNull(jdbcTemplate.getDataSource()));

        Resource resource2 = new ClassPathResource("test-data-qr.sql");
        ResourceDatabasePopulator populator2 = new ResourceDatabasePopulator(resource2);
        populator2.setSqlScriptEncoding("UTF-8");
        populator2.execute(Objects.requireNonNull(jdbcTemplate.getDataSource()));
        log.info("Start data uploaded!");
    }
}
