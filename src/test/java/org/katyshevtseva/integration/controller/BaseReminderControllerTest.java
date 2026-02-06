package org.katyshevtseva.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.katyshevtseva.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@Transactional
@Rollback
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class BaseReminderControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ReminderRepository reminderRepository;

    private static final String USER_ID = "1";
    private static final String CLAIM_SUB = "sub";

    protected RequestPostProcessor user() {
        return jwt().jwt(jwt -> jwt.claim(CLAIM_SUB, USER_ID));
    }
}
