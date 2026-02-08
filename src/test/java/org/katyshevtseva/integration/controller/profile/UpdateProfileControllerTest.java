package org.katyshevtseva.integration.controller.profile;

import com.katyshevtseva.dto.UpdateProfileRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.katyshevtseva.entity.UserProfile;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateProfileControllerTest extends BaseProfileControllerTest {

    private static final String INVALID_EMAIL = "test@";

    @Test
    void shouldSaveProfileDataAndReturnFullDto() throws Exception {
        updateProfileWithValidValues()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.telegram").value(TELEGRAM));

        List<UserProfile> profiles = profileRepository.findAll();
        assertThat(profiles).hasSize(1);
        UserProfile profile = profiles.get(0);
        assertThat(profile.getId()).isNotNull();
        assertThat(profile.getEmail()).isEqualTo(EMAIL);
        assertThat(profile.getTelegram()).isEqualTo(TELEGRAM);
    }

    @Test
    void shouldReturnUnauthorizedWithoutUser() throws Exception {
        UpdateProfileRequestDto dto = new UpdateProfileRequestDto().email(EMAIL).telegram(TELEGRAM);

        mockMvc.perform(put(PROFILE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("provideEmailAndTelegramNullCombinations")
    void shouldReturn200IfSomeParamsAreNull(String email, String telegram) throws Exception {
        updateProfile(email, telegram)
                .andExpect(status().isOk());
    }

    static Stream<Arguments> provideEmailAndTelegramNullCombinations() {
        return Stream.of(
                Arguments.of(EMAIL, null),
                Arguments.of(null, TELEGRAM),
                Arguments.of(null, null)
        );
    }

    @Test
    void shouldReturn400ForInvalidEmail() throws Exception {
        updateProfile(INVALID_EMAIL, TELEGRAM)
                .andExpect(status().isBadRequest());
    }
}
