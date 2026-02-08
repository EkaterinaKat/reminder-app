package org.katyshevtseva.integration.controller.reminder;

import com.katyshevtseva.dto.ReminderRequestDto;
import org.junit.jupiter.api.Test;
import org.katyshevtseva.domain.ReminderEmailStatus;
import org.katyshevtseva.domain.ReminderTelegramStatus;
import org.katyshevtseva.entity.Reminder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateReminderControllerTest extends BaseReminderControllerTest {

    @Test
    void shouldUpdateReminderAndReturnFullDto() throws Exception {
        long idOfCreatedReminder = createReminderAndGetId(TITLE, DESCRIPTION, DATE_TIME);

        ReminderRequestDto updateDto = new ReminderRequestDto(TITLE_2, DATE_TIME_2);
        updateDto.setDescription(DESCRIPTION_2);

        updateReminderWithUser(idOfCreatedReminder, updateDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idOfCreatedReminder))
                .andExpect(jsonPath("$.title").value(TITLE_2))
                .andExpect(jsonPath("$.description").value(DESCRIPTION_2))
                .andExpect(jsonPath("$.remind").value(DATE_TIME_2.toString()))
                .andExpect(jsonPath("$.telegramStatus").value(NOT_SENT_STATUS))
                .andExpect(jsonPath("$.emailStatus").value(NOT_SENT_STATUS));

        List<Reminder> reminders = reminderRepository.findAll();
        assertThat(reminders).hasSize(1);
        Reminder saved = reminders.get(0);
        assertThat(saved.getId()).isEqualTo(idOfCreatedReminder);
        assertThat(saved.getTitle()).isEqualTo(TITLE_2);
        assertThat(saved.getDescription()).isEqualTo(DESCRIPTION_2);
        assertThat(saved.getRemind()).isEqualTo(DATE_TIME_2);
        assertThat(saved.getTelegramStatus()).isEqualTo(ReminderTelegramStatus.NOT_SENT);
        assertThat(saved.getEmailStatus()).isEqualTo(ReminderEmailStatus.NOT_SENT);
    }

    @Test
    void shouldReturnUnauthorizedWithoutUser() throws Exception {
        long idOfCreatedReminder = createReminderAndGetId(TITLE, DESCRIPTION, DATE_TIME);

        ReminderRequestDto updateDto = new ReminderRequestDto(TITLE_2, DATE_TIME_2);

        mockMvc.perform(put(UPDATE_REMINDER_URL)
                        .param(ID_PARAM, String.valueOf(idOfCreatedReminder))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldDeleteDescriptionIfItNull() throws Exception {
        long idOfCreatedReminder = createReminderAndGetId(TITLE, DESCRIPTION, DATE_TIME);

        ReminderRequestDto updateDto = new ReminderRequestDto(TITLE_2, DATE_TIME_2);

        updateReminderWithUser(idOfCreatedReminder, updateDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").isEmpty());

        List<Reminder> reminders = reminderRepository.findAll();
        assertThat(reminders).hasSize(1);
        Reminder saved = reminders.get(0);
        assertThat(saved.getDescription()).isNull();
    }

    @Test
    void shouldReturn400ForNullTitle() throws Exception {
        long idOfCreatedReminder = createReminderAndGetId(TITLE, DESCRIPTION, DATE_TIME);

        ReminderRequestDto updateDto = new ReminderRequestDto(null, DATE_TIME_2);

        updateReminderWithUser(idOfCreatedReminder, updateDto).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForBlankTitle() throws Exception {
        long idOfCreatedReminder = createReminderAndGetId(TITLE, DESCRIPTION, DATE_TIME);

        ReminderRequestDto updateDto = new ReminderRequestDto(BLANK_STRING, DATE_TIME_2);

        updateReminderWithUser(idOfCreatedReminder, updateDto).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForBlankRemind() throws Exception {
        long idOfCreatedReminder = createReminderAndGetId(TITLE, DESCRIPTION, DATE_TIME);

        ReminderRequestDto updateDto = new ReminderRequestDto(TITLE_2, null);

        updateReminderWithUser(idOfCreatedReminder, updateDto).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForNonExistentId() throws Exception {
        ReminderRequestDto updateDto = new ReminderRequestDto(TITLE_2, DATE_TIME_2);

        updateReminderWithUser(RANDOM_INT, updateDto).andExpect(status().isBadRequest());
    }

    private ResultActions updateReminderWithUser(long id, ReminderRequestDto dto) throws Exception {
        return mockMvc.perform(put(UPDATE_REMINDER_URL)
                .with(user())
                .param(ID_PARAM, String.valueOf(id))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
    }
}
