package org.katyshevtseva.controller;

import com.katyshevtseva.api.ReminderApi;
import com.katyshevtseva.dto.PageReminderDto;
import com.katyshevtseva.dto.ReminderDto;
import com.katyshevtseva.dto.ReminderRequestDto;
import org.katyshevtseva.domain.ReminderSortType;
import org.katyshevtseva.service.ReminderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static org.katyshevtseva.util.SecurityUtil.getUserId;

@RestController
public class ReminderController implements ReminderApi {

    private final ReminderService service;

    public ReminderController(ReminderService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<ReminderDto> createReminder(ReminderRequestDto reminderRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createReminder(getUserId(), reminderRequestDto));
    }

    @Override
    public ResponseEntity<Void> deleteReminder(Long reminderId) {
        service.deleteReminder(getUserId(), reminderId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<ReminderDto>> getSortedList(String sortType) {
        ReminderSortType sortTypeEnum = ReminderSortType.fromValue(sortType);
        return ResponseEntity.ok(service.getSortedReminders(getUserId(), sortTypeEnum));
    }

    @Override
    public ResponseEntity<List<ReminderDto>> getFilteredList(LocalDate date, String time) {
        return ResponseEntity.ok(service.getFilteredReminders(getUserId(), date, time));
    }

    @Override
    public ResponseEntity<PageReminderDto> getReminderPage(Integer page, Integer size) {
        return ResponseEntity.ok(service.getReminderPage(getUserId(), page, size));
    }

    @Override
    public ResponseEntity<ReminderDto> updateReminder(Long id, ReminderRequestDto reminderRequestDto) {
        return ResponseEntity.ok(service.updateReminder(getUserId(), id, reminderRequestDto));
    }
}
