package org.katyshevtseva.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.katyshevtseva.dto.ReminderRequestDto;
import org.katyshevtseva.entity.Reminder;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
    protected static final String DELETE_REMINDER_URL = "/reminder";
    protected static final String UPDATE_REMINDER_URL = "/reminder";

    protected final String BLANK_STRING = "   ";
    protected final String ID_PARAM = "id";
    protected final String NOT_SENT_STATUS = "NOT_SENT";
    protected final int RANDOM_INT = 5;

    protected final String TITLE = "Reminder title";
    protected final String DESCRIPTION = "Reminder description";
    protected final LocalDateTime DATE_TIME = LocalDateTime.now().withNano(0);

    protected final String TITLE_2 = "Reminder title 2";
    protected final String DESCRIPTION_2 = "Reminder description 2";
    protected final LocalDateTime DATE_TIME_2 = LocalDateTime.now().withNano(0).plusDays(1);

    private static final String USER_ID = "1";
    private static final String CLAIM_SUB = "sub";

    protected RequestPostProcessor user() {
        return jwt().jwt(jwt -> jwt.claim(CLAIM_SUB, USER_ID));
    }


    protected long createReminderAndGetId() throws Exception {
        return createReminderAndGetId(TITLE, null, DATE_TIME);
    }

    protected long createReminderAndGetId(String title, String desc, LocalDateTime dateTime) throws Exception {
        ReminderRequestDto dto = new ReminderRequestDto(title, dateTime);
        dto.setDescription(desc);

        createReminderWithUser(dto);

        List<Reminder> remindersAfterCreation = reminderRepository.findAll();
        assertThat(remindersAfterCreation).hasSize(1);
        Reminder saved = remindersAfterCreation.get(0);

        return saved.getId();
    }

    protected ResultActions createReminderWithUser(ReminderRequestDto dto) throws Exception {
        return mockMvc.perform(post(CREATE_REMINDER_URL)
                .with(user())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
    }
}
