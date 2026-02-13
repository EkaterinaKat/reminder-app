package org.katyshevtseva.integration.reminder;

import com.katyshevtseva.dto.ReminderRequestDto;
import org.junit.jupiter.api.Test;
import org.katyshevtseva.domain.ReminderEmailStatus;
import org.katyshevtseva.domain.ReminderTelegramStatus;
import org.katyshevtseva.entity.Reminder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateReminderControllerTest extends BaseReminderControllerTest {

    @Test
    void shouldSaveReminderAndReturnFullDto() throws Exception {
        ReminderRequestDto dto = new ReminderRequestDto(TITLE, DATE_TIME);
        dto.setDescription(DESCRIPTION);

        createReminderWithUser(dto)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value(TITLE))
                .andExpect(jsonPath("$.description").value(DESCRIPTION))
                .andExpect(jsonPath("$.remind").value(DATE_TIME.toString()))
                .andExpect(jsonPath("$.telegramStatus").value(NOT_SENT_STATUS))
                .andExpect(jsonPath("$.emailStatus").value(NOT_SENT_STATUS));

        List<Reminder> reminders = reminderRepository.findAll();
        assertThat(reminders).hasSize(1);
        Reminder saved = reminders.get(0);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo(TITLE);
        assertThat(saved.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(saved.getRemind()).isEqualTo(DATE_TIME);
        assertThat(saved.getTelegramStatus()).isEqualTo(ReminderTelegramStatus.NOT_SENT);
        assertThat(saved.getEmailStatus()).isEqualTo(ReminderEmailStatus.NOT_SENT);
    }

    @Test
    void shouldReturnUnauthorizedWithoutUser() throws Exception {
        ReminderRequestDto dto = new ReminderRequestDto(TITLE, DATE_TIME);

        mockMvc.perform(post(CREATE_REMINDER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldCreateReminderWithoutDescription() throws Exception {
        var dto = new ReminderRequestDto(TITLE, DATE_TIME);

        createReminderWithUser(dto).andExpect(status().isCreated());
    }

    @Test
    void shouldReturn400ForNullTitle() throws Exception {
        var dto = new ReminderRequestDto(null, DATE_TIME);
        createReminderWithUser(dto).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForBlankTitle() throws Exception {
        var dto = new ReminderRequestDto(BLANK_STRING, DATE_TIME);
        createReminderWithUser(dto).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForBlankRemind() throws Exception {
        var dto = new ReminderRequestDto(TITLE, null);
        createReminderWithUser(dto).andExpect(status().isBadRequest());
    }

    private ResultActions createReminderWithUser(ReminderRequestDto dto) throws Exception {
        return mockMvc.perform(post(CREATE_REMINDER_URL)
                .with(user())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
    }
}
