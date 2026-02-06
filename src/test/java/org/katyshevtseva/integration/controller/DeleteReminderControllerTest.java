package org.katyshevtseva.integration.controller;

import com.katyshevtseva.dto.ReminderRequestDto;
import org.junit.jupiter.api.Test;
import org.katyshevtseva.entity.Reminder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteReminderControllerTest extends BaseReminderControllerTest {

    @Test
    void shouldDeleteSavedReminder() throws Exception {
        long idOfCreatedReminder = createReminderAndGetId();

        mockMvc.perform(delete(CREATE_REMINDER_URL)
                        .with(user())
                        .param(ID_PARAM, String.valueOf(idOfCreatedReminder)))
                .andExpect(status().isOk());

        List<Reminder> remindersAfterDeletion = reminderRepository.findAll();
        assertThat(remindersAfterDeletion).hasSize(0);
    }

    @Test
    void shouldReturnUnauthorizedWithoutUser() throws Exception {
        long idOfCreatedReminder = createReminderAndGetId();

        mockMvc.perform(delete(CREATE_REMINDER_URL)
                        .with(user())
                        .param(ID_PARAM, String.valueOf(idOfCreatedReminder)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400ForBlankId() throws Exception {
        mockMvc.perform(delete(CREATE_REMINDER_URL)
                        .with(user())
                        .param(ID_PARAM, BLANK_STRING))
                .andExpect(status().isBadRequest());
    }

    private long createReminderAndGetId() throws Exception {
        ReminderRequestDto dto = new ReminderRequestDto(TITLE, DATE_TIME);
        createReminderWithUser(dto);

        List<Reminder> remindersAfterCreation = reminderRepository.findAll();
        assertThat(remindersAfterCreation).hasSize(1);
        Reminder saved = remindersAfterCreation.get(0);

        return saved.getId();
    }
}
