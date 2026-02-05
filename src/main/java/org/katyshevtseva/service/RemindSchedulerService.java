package org.katyshevtseva.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.katyshevtseva.bot.ReminderTelegramBot;
import org.katyshevtseva.domain.ReminderEmailStatus;
import org.katyshevtseva.domain.ReminderTelegramStatus;
import org.katyshevtseva.entity.Reminder;
import org.katyshevtseva.entity.UserTelegramInfo;
import org.katyshevtseva.repository.ReminderRepository;
import org.katyshevtseva.repository.UserTelegramInfoRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.katyshevtseva.util.MessageUtil.formMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class RemindSchedulerService {

    private final ReminderTelegramBot telegramBot;
    private final ReminderRepository reminderRepository;
    private final UserTelegramInfoRepository telegramInfoRepository;
    private final EmailService emailService;

    @Async
    @Transactional
    public void sendReminds() {
        List<Reminder> reminders = reminderRepository.findByRemindBeforeAndByStatuses(
                LocalDateTime.now(),
                ReminderTelegramStatus.NOT_SENT,
                ReminderEmailStatus.NOT_SENT
        );

        for (Reminder reminder : reminders) {

            if (reminder.getTelegramStatus() == ReminderTelegramStatus.NOT_SENT) {
                ReminderTelegramStatus newStatus = telegramSend(reminder);
                reminder.setTelegramStatus(newStatus);
            }

            if (reminder.getEmailStatus() == ReminderEmailStatus.NOT_SENT) {
                ReminderEmailStatus newStatus = emailSend(reminder);
                reminder.setEmailStatus(newStatus);
            }

            reminderRepository.save(reminder);
        }
    }

    private ReminderTelegramStatus telegramSend(Reminder reminder) {
        if (reminder.getUserProfile().getTelegram() == null) {
            return ReminderTelegramStatus.TELEGRAM_NOT_SPECIFIED;
        }

        Optional<UserTelegramInfo> infoOptional = telegramInfoRepository.findById(
                reminder.getUserProfile().getTelegram()
        );

        if (infoOptional.isEmpty()) {
            return ReminderTelegramStatus.TELEGRAM_CHAT_NOT_FOUND;
        }

        UserTelegramInfo info = infoOptional.get();

        try {
            telegramBot.sendMessage(info.getChatId(), formMessage(reminder));
            return ReminderTelegramStatus.SENT;
        } catch (TelegramApiException e) {
            log.error("Error sending telegram message to user {}: ", info.getUserName(), e);
            return ReminderTelegramStatus.ERROR;
        }
    }

    private ReminderEmailStatus emailSend(Reminder reminder) {
        String emailAddress = reminder.getUserProfile().getEmail();

        if (emailAddress == null) {
            return ReminderEmailStatus.EMAIL_NOT_SPECIFIED;
        }

        try {
            emailService.sendEmail(
                    emailAddress,
                    reminder.getTitle(),
                    formMessage(reminder)
            );
            return ReminderEmailStatus.SENT;
        } catch (Exception e) {
            log.error("Error sending email to {}: ", emailAddress, e);
            return ReminderEmailStatus.ERROR;
        }
    }
}
