package org.katyshevtseva.integration.controller.reminder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.katyshevtseva.dto.ReminderDto;
import org.junit.jupiter.api.Test;
import org.katyshevtseva.domain.ReminderSortType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetSortedReminderControllerTest extends BaseReminderControllerTest {

    private final String SORT_TYPE_PARAM = "sortType";
    private final String TITLE_SORT = "title";
    private final String DATE_SORT = "date";
    private final String TIME_SORT = "time";

    @Test
    void shouldSortByTitle() throws Exception {
        List<String> orderedTitles = Arrays.asList("A", "B", "C", "D", "E");

        List<String> shuffled = new ArrayList<>(orderedTitles);
        Collections.shuffle(shuffled);

        for (String title : shuffled) {
            createReminderAndGetId(title, DESCRIPTION, DATE_TIME);
        }

        List<ReminderDto> reminders = getSortedList(TITLE_SORT);

        for (int i = 0; i < reminders.size(); i++) {
            assertThat(reminders.get(i).getTitle()).isEqualTo(orderedTitles.get(i));
        }
    }

    @Test
    void shouldSortByDate() throws Exception {
        List<LocalDateTime> dateTimeOrderedByDate = Arrays.asList(
                LocalDateTime.of(2000, 1, 1, 0, 0),
                LocalDateTime.of(2000, 1, 2, 0, 0),
                LocalDateTime.of(2000, 1, 3, 0, 0),
                LocalDateTime.of(2000, 1, 4, 0, 0),
                LocalDateTime.of(2000, 1, 5, 0, 0)
        );

        List<LocalDateTime> shuffled = new ArrayList<>(dateTimeOrderedByDate);
        Collections.shuffle(shuffled);

        for (LocalDateTime dateTime : shuffled) {
            createReminderAndGetId(TITLE, DESCRIPTION, dateTime);
        }

        List<ReminderDto> reminders = getSortedList(DATE_SORT);

        for (int i = 0; i < reminders.size(); i++) {
            assertThat(reminders.get(i).getRemind()).isEqualTo(dateTimeOrderedByDate.get(i));
        }
    }

    @Test
    void shouldSortByTime() throws Exception {
        List<LocalDateTime> dateTimeOrderedByTime = Arrays.asList(
                LocalDateTime.of(2000, 1, 1, 0, 1),
                LocalDateTime.of(2000, 1, 1, 0, 2),
                LocalDateTime.of(2000, 1, 1, 0, 3),
                LocalDateTime.of(2000, 1, 1, 0, 4),
                LocalDateTime.of(2000, 1, 1, 0, 5)
        );

        List<LocalDateTime> shuffled = new ArrayList<>(dateTimeOrderedByTime);
        Collections.shuffle(shuffled);

        for (LocalDateTime dateTime : shuffled) {
            createReminderAndGetId(TITLE, DESCRIPTION, dateTime);
        }

        List<ReminderDto> reminders = getSortedList(TIME_SORT);

        for (int i = 0; i < reminders.size(); i++) {
            assertThat(reminders.get(i).getRemind()).isEqualTo(dateTimeOrderedByTime.get(i));
        }
    }

    @Test
    void shouldReturnUnauthorizedWithoutUser() throws Exception {
        mockMvc.perform(get(GET_SORTED_REMINDER_LIST_URL)
                        .param(SORT_TYPE_PARAM, ReminderSortType.TIME.name()))
                .andExpect(status().isUnauthorized());
    }

    private List<ReminderDto> getSortedList(String sortType) throws Exception {
        MvcResult result = mockMvc.perform(get(GET_SORTED_REMINDER_LIST_URL)
                        .with(user())
                        .param(SORT_TYPE_PARAM, sortType))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        return objectMapper.readValue(responseBody, new TypeReference<>() {
        });
    }
}
