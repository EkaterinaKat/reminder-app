package org.katyshevtseva.util;

import org.katyshevtseva.entity.Reminder;

public final class MessageUtil {
    private MessageUtil() {
    }

    public static String formEmailSubject(Reminder reminder) {
        return reminder.getTitle();
    }

    public static String formEmailMessage(Reminder reminder) {
        return String.format("%s\n%s", reminder.getTitle(), reminder.getDescription());
    }

    public static String formTelegramMessage(Reminder reminder) {
        return String.format("%s\n%s", reminder.getTitle(), reminder.getDescription());
    }
}
