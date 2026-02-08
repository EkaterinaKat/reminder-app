package org.katyshevtseva.integration.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.katyshevtseva.dto.ReminderDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetFilteredReminderControllerTest extends BaseReminderControllerTest {

    private final String DATE_PARAM = "date";
    private final String TIME_PARAM = "time";

    private final String INVALID_DATE = "2000-01-040";
    private final String INVALID_TIME = "00:03:000";

    private static final LocalDateTime DATE_1 = LocalDateTime.of(2000, 1, 1, 0, 1);
    private static final LocalDateTime DATE_2 = LocalDateTime.of(2000, 1, 2, 0, 2);
    private static final LocalDateTime DATE_3 = LocalDateTime.of(2000, 1, 3, 0, 3);
    private static final LocalDateTime DATE_4 = LocalDateTime.of(2000, 1, 4, 0, 3);
    private static final LocalDateTime DATE_5 = LocalDateTime.of(2000, 1, 4, 0, 4);

    private static final List<LocalDateTime> dateTimeList = Arrays.asList(DATE_1, DATE_2, DATE_3, DATE_4, DATE_5);

    static Stream<Arguments> provideFilterParamsAndResult() {
        return Stream.of(
                Arguments.of("2000-01-04", null, Arrays.asList(DATE_4, DATE_5)),
                Arguments.of("2000-01-01", null, List.of(DATE_1)),
                Arguments.of(null, "00:03:00", Arrays.asList(DATE_3, DATE_4)),
                Arguments.of(null, "00:02:00", List.of(DATE_2)),
                Arguments.of("2000-01-04", "00:03:00", List.of(DATE_4)),
                Arguments.of("2000-01-04", "00:01:00", List.of()),
                Arguments.of(null, null, dateTimeList)
        );
    }

    @BeforeEach
    public void createReminders() throws Exception {
        for (LocalDateTime dateTime : dateTimeList) {
            createReminderAndGetId(TITLE, DESCRIPTION, dateTime);
        }
    }

    @ParameterizedTest
    @MethodSource("provideFilterParamsAndResult")
    void shouldFilterCorrectly(String date, String time, List<LocalDateTime> expected) throws Exception {
        MvcResult result = mockMvc.perform(get(GET_FILTERED_REMINDER_LIST_URL)
                        .param(DATE_PARAM, date)
                        .param(TIME_PARAM, time)
                        .with(user()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        List<ReminderDto> dtos = objectMapper.readValue(responseBody, new TypeReference<>() {
        });
        List<LocalDateTime> actual = dtos.stream().map(ReminderDto::getRemind).toList();

        boolean expectedAndActualAreEqual = expected.size() == actual.size() &&
                expected.containsAll(actual) &&
                actual.containsAll(expected);
        assertThat(expectedAndActualAreEqual).isEqualTo(true);
    }

    @Test
    void shouldReturn400ForInvalidDate() throws Exception {
        mockMvc.perform(get(GET_FILTERED_REMINDER_LIST_URL)
                        .param(DATE_PARAM, INVALID_DATE)
                        .with(user()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForInvalidTime() throws Exception {
        mockMvc.perform(get(GET_FILTERED_REMINDER_LIST_URL)
                        .param(TIME_PARAM, INVALID_TIME)
                        .with(user()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUnauthorizedWithoutUser() throws Exception {
        mockMvc.perform(get(GET_FILTERED_REMINDER_LIST_URL))
                .andExpect(status().isUnauthorized());
    }
}
