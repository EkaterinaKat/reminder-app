package org.katyshevtseva.integration.controller.profile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteProfileControllerTest extends BaseProfileControllerTest {

    @Test
    void shouldDeleteSavedReminder() throws Exception {
        updateProfileWithValidValues();

        assertEquals(1, profileRepository.count());

        mockMvc.perform(delete(PROFILE_URL)
                        .with(user()))
                .andExpect(status().isOk());

        assertEquals(0, profileRepository.count());
    }

    @Test
    void shouldReturnUnauthorizedWithoutUser() throws Exception {
        mockMvc.perform(delete(PROFILE_URL))
                .andExpect(status().isUnauthorized());
    }
}
