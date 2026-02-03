package org.katyshevtseva.service;

import lombok.RequiredArgsConstructor;
import org.katyshevtseva.bot.ReminderTelegramBot;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemindSchedulerService {

    private final ReminderTelegramBot telegramBot;

    public void sendReminds() {

    }
}
