package org.katyshevtseva.util;

public final class Utils {
    private Utils() {
    }

    public static String normalizeWhitespace(String content) {
        return content
                .replaceAll("\\r\\n", "\n")
                .trim();
    }
}
