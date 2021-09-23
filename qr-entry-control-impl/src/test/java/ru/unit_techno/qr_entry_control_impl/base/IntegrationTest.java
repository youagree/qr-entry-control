
package ru.unit_techno.qr_entry_control_impl.base;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.unit_techno.qr_entry_control_imp.QrEntryControlApplication;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(classes = QrEntryControlApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public @interface IntegrationTest {
}