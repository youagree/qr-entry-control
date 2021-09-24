
package ru.unit_techno.qr_entry_control_impl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EntityScan(basePackages =
        {"ru.unit.techno.ariss.log.action.lib.entity",
                "ru.unit_techno.qr_entry_control_impl.entity"})
@EnableJpaRepositories(basePackages = {"ru.unit_techno.qr_entry_control_impl.repository",
        "ru.unit.techno.ariss.log.action.lib.repository"})
@EnableFeignClients(basePackages =
        {"ru.unit.techno.device.registration.api",
                "ru.unit.techno.ariss.barrier.api"})
@EnableDiscoveryClient
@SpringBootApplication(exclude = ThymeleafAutoConfiguration.class)
@EnableScheduling
public class QrEntryControlApplication {
    public static void main(String[] args) {
        SpringApplication.run(QrEntryControlApplication.class, args);
    }
}