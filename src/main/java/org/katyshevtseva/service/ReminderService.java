package org.katyshevtseva.service;

import com.katyshevtseva.dto.PageReminderDto;
import com.katyshevtseva.dto.ReminderDto;
import com.katyshevtseva.dto.ReminderRequestDto;
import lombok.RequiredArgsConstructor;
import org.katyshevtseva.domain.ReminderEmailStatus;
import org.katyshevtseva.domain.ReminderSortType;
import org.katyshevtseva.domain.ReminderTelegramStatus;
import org.katyshevtseva.entity.Reminder;
import org.katyshevtseva.mapper.ReminderMapper;
import org.katyshevtseva.repository.ReminderRepository;
import org.katyshevtseva.repository.UserProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final ReminderMapper mapper;
    private final UserProfileRepository profileRepository;

    public ReminderDto createReminder(String userId, ReminderRequestDto requestDto) {
        validateTitle(requestDto.getTitle());
        Reminder reminder = mapper.toEntity(requestDto);
        reminder.setUserProfile(profileRepository.getOrCreate(userId));
        reminder.setTelegramStatus(ReminderTelegramStatus.NOT_SENT);
        reminder.setEmailStatus(ReminderEmailStatus.NOT_SENT);
        return mapper.toDto(reminderRepository.save(reminder));
    }

    public void deleteReminder(String userId, Long reminderId) {
        validateOwnershipAndGetReminder(userId, reminderId);
        reminderRepository.deleteById(reminderId);
    }

    public List<ReminderDto> getSortedReminders(String userId, ReminderSortType sortType) {
        List<Reminder> reminders = null;
        switch (sortType) {
            case DATE -> reminders = reminderRepository.findByUserProfileIdOrderByRemind(userId);
            case TIME -> reminders = reminderRepository.findByUserIdOrderByTime(userId);
            case TITLE -> reminders = reminderRepository.findByUserProfileIdOrderByTitle(userId);
        }

        return reminders.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<ReminderDto> getFilteredReminders(String userId, LocalDate date, String timeString) {
        LocalTime time = (timeString != null) ? LocalTime.parse(timeString) : null;
        List<Reminder> reminders = reminderRepository.findFilteredReminders(userId, date, time);
        return reminders.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public PageReminderDto getReminderPage(String userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size > 0 ? size : 10);
        Page<Reminder> reminderPage = reminderRepository.findByUserProfileId(userId, pageable);
        return toPageReminderDto(reminderPage);
    }

    public ReminderDto updateReminder(String userId, Long reminderId, ReminderRequestDto reminderRequestDto) {
        validateTitle(reminderRequestDto.getTitle());
        Reminder reminder = validateOwnershipAndGetReminder(userId, reminderId);
        mapper.updateReminderFromDto(reminderRequestDto, reminder);
        return mapper.toDto(reminderRepository.save(reminder));
    }

    private Reminder validateOwnershipAndGetReminder(String userId, Long reminderId) {
        Reminder reminder = reminderRepository.findById(reminderId).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("Reminder with id=%d not found", reminderId)
                )
        );
        if (!reminder.getUserProfile().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Reminder does not belong to the user");
        }
        return reminder;
    }

    private PageReminderDto toPageReminderDto(Page<Reminder> page) {
        PageReminderDto pageDto = new PageReminderDto();
        pageDto.setContent(page.getContent().stream().map(mapper::toDto).collect(Collectors.toList()));
        pageDto.setTotalElements(page.getTotalElements());
        pageDto.setTotalPages(page.getTotalPages());
        pageDto.setSize(page.getSize());
        pageDto.setPage(page.getNumber());
        return pageDto;
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank");
        }
    }
}
