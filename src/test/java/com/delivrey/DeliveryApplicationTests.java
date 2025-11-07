package com.delivrey;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=update",
    "spring.jpa.show-sql=true",
    "spring.main.allow-bean-definition-overriding=true"
})
public class DeliveryApplicationTests {

    @Test
    public void contextLoads() {
        // Vérifie simplement que le contexte de l'application se charge correctement
    }
}
