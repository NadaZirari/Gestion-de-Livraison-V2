package com.delivrey;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(
    classes = {DeliveryApplication.class, TestConfig.class},
    properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=update",
        "spring.jpa.show-sql=true",
        "spring.main.allow-bean-definition-overriding=true",
        "spring.ai.openai.api-key=sk-dummy-key" // Dummy key for testing
    }
)
@ContextConfiguration(classes = {DeliveryApplication.class, TestConfig.class})
public class DeliveryApplicationTests {

    @Test
    public void contextLoads() {
        // Just verifies that the application context loads successfully
    }
}
