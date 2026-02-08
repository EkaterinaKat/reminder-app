package org.katyshevtseva.integration.controller.profile;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetProfileControllerTest extends BaseProfileControllerTest {

    @Test
    void shouldReturnCorrectVales() throws Exception {
        updateProfile(EMAIL, TELEGRAM);

        mockMvc.perform(get(PROFILE_URL)
                        .with(user()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.telegram").value(TELEGRAM));
    }

    @Test
    void shouldReturnUnauthorizedWithoutUser() throws Exception {
        mockMvc.perform(get(PROFILE_URL))
                .andExpect(status().isUnauthorized());
    }
}
