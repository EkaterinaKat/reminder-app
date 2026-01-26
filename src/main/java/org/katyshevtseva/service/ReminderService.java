package org.katyshevtseva.service;

import com.katyshevtseva.dto.PageReminderDto;
import com.katyshevtseva.dto.ReminderDto;
import com.katyshevtseva.dto.ReminderRequestDto;
import org.katyshevtseva.domain.ReminderSortType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReminderService {

    public ReminderDto createReminder(String userId, ReminderRequestDto requestDto) {
        return new ReminderDto(8L, requestDto.getTitle(), requestDto.getRemind());
    }

    public void deleteReminder(String userId, Long reminderId) {

    }

    public List<ReminderDto> getSortedReminders(String userId, ReminderSortType sortType) {
        return new ArrayList<>();
    }

    public List<ReminderDto> getFilteredReminders(String userId, LocalDate date, String time) {
        return new ArrayList<>();
    }

    public PageReminderDto getReminderPage(String userId, Integer page, Integer size) {
        return new PageReminderDto();
    }

    public ReminderDto updateReminder(String userId, Long id, ReminderRequestDto reminderRequestDto) {
        return new ReminderDto();
    }
}
