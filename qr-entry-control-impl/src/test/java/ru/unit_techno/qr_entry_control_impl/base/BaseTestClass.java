
package ru.unit_techno.qr_entry_control_impl.base;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.unit_techno.qr_entry_control_impl.mapper.EntryDeviceToReqRespMapper;
import ru.unit_techno.qr_entry_control_impl.repository.CardRepository;
import ru.unit_techno.qr_entry_control_impl.repository.QrDeliveryEntityRepository;
import ru.unit_techno.qr_entry_control_impl.repository.QrRepository;

import java.time.Duration;

@Slf4j
@IntegrationTest
public class BaseTestClass {

    @Autowired
    protected TestUtils testUtils;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected QrDeliveryEntityRepository qrDeliveryEntityRepository;

    @Autowired
    protected EntryDeviceToReqRespMapper reqRespMapper;

    @Autowired
    protected CardRepository cardRepository;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    protected QrRepository qrRepository;

    private static final String DB_NAME = "unit_techno";
    public static String DB_URL;

    private static final PostgreSQLContainer postgresDB = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName(DB_NAME)
            .withUsername("postgres")
            .withPassword("postgres")
            .withClasspathResourceMapping("init.sql", "/docker-entrypoint-initdb.d/init.sql", BindMode.READ_ONLY);

    static {
        postgresDB.setWaitStrategy(Wait.defaultWaitStrategy().withStartupTimeout(Duration.ofSeconds(30)));
        postgresDB.start();
        DB_URL = String.format("jdbc:postgresql://%s:%d/unit_techno?currentSchema=qr_entry_control",
                postgresDB.getContainerIpAddress(),
                postgresDB.getFirstMappedPort());
    }

    @DynamicPropertySource
    static void dynamicSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> DB_URL);
    }

    public void destroy() {
    }

    @AfterEach
    private void clearRepo() {
        cardRepository.deleteAll();
        qrDeliveryEntityRepository.deleteAll();
        qrRepository.deleteAll();
    }
}