
package ru.unit_techno.qr_entry_control_imp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;

@SpringBootApplication(exclude = ThymeleafAutoConfiguration.class)
public class QrEntryControlApplication {
    public static void main(String[] args) {
        SpringApplication.run(QrEntryControlApplication.class, args);
    }
}