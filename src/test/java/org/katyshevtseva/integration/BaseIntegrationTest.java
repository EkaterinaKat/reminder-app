package org.katyshevtseva.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@Transactional
@Rollback
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected final String BLANK_STRING = "   ";

    private static final String USER_ID_1 = "1";
    private static final String USER_ID_2 = "2";
    private static final String CLAIM_SUB = "sub";

    protected RequestPostProcessor user() {
        return jwt().jwt(jwt -> jwt.claim(CLAIM_SUB, USER_ID_1));
    }

    protected RequestPostProcessor otherUser() {
        return jwt().jwt(jwt -> jwt.claim(CLAIM_SUB, USER_ID_2));
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:test_db");
        registry.add("spring.datasource.driver-class-name", () -> "org.h2.Driver");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.H2Dialect");
        registry.add("spring.jpa.hibername.ddl-auto", () -> "create-drop");
    }
}
