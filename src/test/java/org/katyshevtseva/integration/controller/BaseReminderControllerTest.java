package org.katyshevtseva.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.katyshevtseva.dto.ReminderRequestDto;
import org.katyshevtseva.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

    protected static final String CREATE_REMINDER_URL = "/reminder";

    protected final String BLANK_STRING = "   ";
    protected final String TITLE = "Reminder title";
    protected final LocalDateTime DATE_TIME = LocalDateTime.now().withNano(0);
    protected final String ID_PARAM = "id";

    private static final String USER_ID = "1";
    private static final String CLAIM_SUB = "sub";

    protected RequestPostProcessor user() {
        return jwt().jwt(jwt -> jwt.claim(CLAIM_SUB, USER_ID));
    }

    protected ResultActions createReminderWithUser(ReminderRequestDto dto) throws Exception {
        return mockMvc.perform(post(CREATE_REMINDER_URL)
                .with(user())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
    }
}
