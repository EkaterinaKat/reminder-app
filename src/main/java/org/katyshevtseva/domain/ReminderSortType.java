package org.katyshevtseva.domain;

public enum ReminderSortType {

    TITLE("title"),
    DATE("date"),
    TIME("time");

    private final String value;

    ReminderSortType(String value) {
        this.value = value;
    }

    public static ReminderSortType fromValue(String value) {
        for (ReminderSortType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown sort type: " + value);
    }
}
