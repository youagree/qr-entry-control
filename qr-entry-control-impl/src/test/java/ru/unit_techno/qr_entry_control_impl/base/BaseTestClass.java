
package ru.unit_techno.qr_entry_control_impl.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;

@Slf4j
public class BaseTestClass {

    @Autowired
    protected TestUtils testUtils;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

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
        DB_URL = String.format("jdbc:p6spy:postgresql://%s:%d/unit_techno?currentSchema=qr_entry_control",
                postgresDB.getContainerIpAddress(),
                postgresDB.getFirstMappedPort());
    }

    @DynamicPropertySource
    static void dynamicSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> DB_URL);
    }

    public void destroy() {
    }
}