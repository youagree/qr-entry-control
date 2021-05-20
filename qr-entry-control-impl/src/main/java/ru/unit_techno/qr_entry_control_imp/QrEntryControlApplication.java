
package ru.unit_techno.qr_entry_control_imp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = ThymeleafAutoConfiguration.class)
@EnableScheduling
public class QrEntryControlApplication {
    public static void main(String[] args) {
        SpringApplication.run(QrEntryControlApplication.class, args);
    }
}