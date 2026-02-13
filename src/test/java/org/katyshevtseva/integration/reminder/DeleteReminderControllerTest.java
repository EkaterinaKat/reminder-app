package org.katyshevtseva.integration.reminder;

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

        mockMvc.perform(delete(DELETE_REMINDER_URL)
                        .with(user())
                        .param(ID_PARAM, String.valueOf(idOfCreatedReminder)))
                .andExpect(status().isOk());

        List<Reminder> remindersAfterDeletion = reminderRepository.findAll();
        assertThat(remindersAfterDeletion).hasSize(0);
    }

    @Test
    void shouldReturnUnauthorizedWithoutUser() throws Exception {
        long idOfCreatedReminder = createReminderAndGetId();

        mockMvc.perform(delete(DELETE_REMINDER_URL)
                        .param(ID_PARAM, String.valueOf(idOfCreatedReminder)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn400ForBlankId() throws Exception {
        mockMvc.perform(delete(DELETE_REMINDER_URL)
                        .with(user())
                        .param(ID_PARAM, BLANK_STRING))
                .andExpect(status().isBadRequest());
    }
}
