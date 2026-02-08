package org.katyshevtseva.integration.controller.reminder;

import com.fasterxml.jackson.databind.JsonNode;
import com.katyshevtseva.dto.ReminderRequestDto;
import org.katyshevtseva.integration.controller.BaseTest;
import org.katyshevtseva.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class BaseReminderControllerTest extends BaseTest {

    @Autowired
    protected ReminderRepository reminderRepository;

    protected static final String CREATE_REMINDER_URL = "/reminder";
    protected static final String DELETE_REMINDER_URL = "/reminder";
    protected static final String UPDATE_REMINDER_URL = "/reminder";
    protected static final String GET_REMINDER_LIST_URL = "/reminder";
    protected static final String GET_SORTED_REMINDER_LIST_URL = "/reminder/sort";
    protected static final String GET_FILTERED_REMINDER_LIST_URL = "/reminder/filter";

    protected final String ID_PARAM = "id";
    protected final String NOT_SENT_STATUS = "NOT_SENT";
    protected final int RANDOM_INT = 5;

    protected final String TITLE = "Reminder title";
    protected final String DESCRIPTION = "Reminder description";
    protected final LocalDateTime DATE_TIME = LocalDateTime.now().withNano(0);

    protected final String TITLE_2 = "Reminder title 2";
    protected final String DESCRIPTION_2 = "Reminder description 2";
    protected final LocalDateTime DATE_TIME_2 = LocalDateTime.now().withNano(0).plusDays(1);

    protected long createReminderAndGetId() throws Exception {
        return createReminderAndGetId(TITLE, null, DATE_TIME);
    }

    protected long createReminderAndGetId(String title, String desc, LocalDateTime dateTime) throws Exception {
        return createReminderAndGetId(title, desc, dateTime, user());
    }

    protected long createReminderAndGetId(
            String title,
            String desc,
            LocalDateTime dateTime,
            RequestPostProcessor user
    ) throws Exception {

        ReminderRequestDto dto = new ReminderRequestDto(title, dateTime);
        dto.setDescription(desc);

        MvcResult result = mockMvc.perform(post(CREATE_REMINDER_URL)
                        .with(user)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get(ID_PARAM).asLong();
    }
}
