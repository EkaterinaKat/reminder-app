package org.katyshevtseva.integration.controller.profile;

import com.katyshevtseva.dto.UpdateProfileRequestDto;
import org.katyshevtseva.integration.controller.BaseTest;
import org.katyshevtseva.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


public class BaseProfileControllerTest extends BaseTest {

    @Autowired
    protected UserProfileRepository profileRepository;

    protected final String PROFILE_URL = "/profile";

    protected static final String EMAIL = "test@mail.ru";
    protected static final String TELEGRAM = "username";

    protected ResultActions updateProfileWithValidValues() throws Exception {
        return updateProfile(EMAIL, TELEGRAM);
    }

    protected ResultActions updateProfile(String email, String telegram) throws Exception {
        UpdateProfileRequestDto dto = new UpdateProfileRequestDto().email(email).telegram(telegram);

        return mockMvc.perform(put(PROFILE_URL)
                .with(user())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
    }
}
