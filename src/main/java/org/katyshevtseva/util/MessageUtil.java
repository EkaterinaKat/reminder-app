package org.katyshevtseva.util;

import org.katyshevtseva.entity.Reminder;

public final class MessageUtil {
    private MessageUtil() {
    }

    public static String formMessage(Reminder reminder) {
        return String.format("%s\n%s", reminder.getTitle(), reminder.getDescription());
    }
}
