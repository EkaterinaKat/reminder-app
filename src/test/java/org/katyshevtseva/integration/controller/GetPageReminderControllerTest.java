package org.katyshevtseva.integration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GetPageReminderControllerTest extends BaseReminderControllerTest {

    private final String PAGE_PARAM = "page";
    private final String SIZE_PARAM = "size";

    private final int TOTAL_ELEMENTS = 25;
    private final int PAGE_SIZE = 10;
    private final int DEFAULT_PAGE_SIZE = 20;
    private final int EXPECTED_PAGES = 3;
    private final int LAST_PAGE_SIZE = 5;

    private final int FIRST_PAGE_NUMBER = 0;
    private final int SECOND_PAGE_NUMBER = 1;
    private final int THIRD_PAGE_NUMBER = 2;

    @Test
    void shouldReturnFirstPage() throws Exception {
        createReminders(TOTAL_ELEMENTS);

        mockMvc.perform(get(GET_REMINDER_LIST_URL)
                        .with(user())
                        .param(PAGE_PARAM, String.valueOf(FIRST_PAGE_NUMBER))
                        .param(SIZE_PARAM, String.valueOf(PAGE_SIZE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(PAGE_SIZE))
                .andExpect(jsonPath("$.totalElements").value(TOTAL_ELEMENTS))
                .andExpect(jsonPath("$.totalPages").value(EXPECTED_PAGES))
                .andExpect(jsonPath("$.size").value(PAGE_SIZE))
                .andExpect(jsonPath("$.page").value(FIRST_PAGE_NUMBER));
    }

    @Test
    void shouldReturnSecondPage() throws Exception {
        createReminders(TOTAL_ELEMENTS);

        mockMvc.perform(get(GET_REMINDER_LIST_URL)
                        .with(user())
                        .param(PAGE_PARAM, String.valueOf(SECOND_PAGE_NUMBER))
                        .param(SIZE_PARAM, String.valueOf(PAGE_SIZE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(PAGE_SIZE))
                .andExpect(jsonPath("$.totalElements").value(TOTAL_ELEMENTS))
                .andExpect(jsonPath("$.totalPages").value(EXPECTED_PAGES))
                .andExpect(jsonPath("$.size").value(PAGE_SIZE))
                .andExpect(jsonPath("$.page").value(SECOND_PAGE_NUMBER));
    }

    @Test
    void shouldReturnLastPageWithRemainingElements() throws Exception {
        createReminders(TOTAL_ELEMENTS);

        mockMvc.perform(get(GET_REMINDER_LIST_URL)
                        .with(user())
                        .param(PAGE_PARAM, String.valueOf(THIRD_PAGE_NUMBER))
                        .param(SIZE_PARAM, String.valueOf(PAGE_SIZE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(LAST_PAGE_SIZE))
                .andExpect(jsonPath("$.totalElements").value(TOTAL_ELEMENTS))
                .andExpect(jsonPath("$.totalPages").value(EXPECTED_PAGES))
                .andExpect(jsonPath("$.size").value(PAGE_SIZE))
                .andExpect(jsonPath("$.page").value(THIRD_PAGE_NUMBER));
    }

    @Test
    void shouldDefaultParams() throws Exception {
        mockMvc.perform(get(GET_REMINDER_LIST_URL)
                        .with(user()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(DEFAULT_PAGE_SIZE))
                .andExpect(jsonPath("$.page").value(FIRST_PAGE_NUMBER));
    }

    @Test
    void shouldReturnEmptyPageWhenNoReminders() throws Exception {
        mockMvc.perform(get(GET_REMINDER_LIST_URL)
                        .with(user())
                        .param(PAGE_PARAM, String.valueOf(FIRST_PAGE_NUMBER))
                        .param(SIZE_PARAM, String.valueOf(PAGE_SIZE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0))
                .andExpect(jsonPath("$.size").value(PAGE_SIZE))
                .andExpect(jsonPath("$.page").value(FIRST_PAGE_NUMBER));
    }

    @Test
    void shouldReturnEmptyPageWhenPageNumberExceedsTotalPages() throws Exception {
        createReminders(TOTAL_ELEMENTS);

        String pageNumber = "10";

        mockMvc.perform(get(GET_REMINDER_LIST_URL)
                        .with(user())
                        .param(PAGE_PARAM, pageNumber)
                        .param(SIZE_PARAM, String.valueOf(PAGE_SIZE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.page").value(pageNumber));
    }

    @Test
    void shouldReturnOnlyCurrentUserReminders() throws Exception {
        int firstUserRemindsCount = 6;
        int secondUserRemindsCount = 9;
        String firstUserReminderTitle = "reminder for first user";

        createReminders(firstUserRemindsCount, user(), firstUserReminderTitle);
        createReminders(secondUserRemindsCount, otherUser(), TITLE);

        mockMvc.perform(get(GET_REMINDER_LIST_URL)
                        .with(user())
                        .param(PAGE_PARAM, String.valueOf(FIRST_PAGE_NUMBER))
                        .param(SIZE_PARAM, String.valueOf(PAGE_SIZE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(firstUserRemindsCount))
                .andExpect(jsonPath("$.totalElements").value(firstUserRemindsCount));
    }

    @Test
    void shouldReturnUnauthorizedWithoutUser() throws Exception {
        mockMvc.perform(get(GET_REMINDER_LIST_URL)
                        .param(PAGE_PARAM, String.valueOf(FIRST_PAGE_NUMBER))
                        .param(SIZE_PARAM, String.valueOf(PAGE_SIZE)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn400ForNegativePage() throws Exception {
        mockMvc.perform(get(GET_REMINDER_LIST_URL)
                        .with(user())
                        .param(PAGE_PARAM, "-1")
                        .param(SIZE_PARAM, String.valueOf(PAGE_SIZE)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForZeroSize() throws Exception {
        mockMvc.perform(get(GET_REMINDER_LIST_URL)
                        .with(user())
                        .param(PAGE_PARAM, String.valueOf(FIRST_PAGE_NUMBER))
                        .param(SIZE_PARAM, "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForTooLargeSize() throws Exception {
        mockMvc.perform(get(GET_REMINDER_LIST_URL)
                        .with(user())
                        .param(PAGE_PARAM, String.valueOf(FIRST_PAGE_NUMBER))
                        .param(SIZE_PARAM, "1000"))
                .andExpect(status().isBadRequest());
    }

    private void createReminders(int amount) throws Exception {
        for (int i = 0; i < amount; i++) {
            createReminderAndGetId(TITLE, DESCRIPTION, DATE_TIME);
        }
    }

    private void createReminders(int amount, RequestPostProcessor user, String title) throws Exception {
        for (int i = 0; i < amount; i++) {
            createReminderAndGetId(title, DESCRIPTION, DATE_TIME, user);
        }
    }
}
