package org.katyshevtseva.bot;

import lombok.extern.slf4j.Slf4j;
import org.katyshevtseva.service.TelegramService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class ReminderTelegramBot extends TelegramLongPollingBot {

    private final TelegramService telegramService;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    public ReminderTelegramBot(@Value("${telegram.bot.token}") String token, TelegramService telegramService) {
        super(token);
        this.telegramService = telegramService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (isStartMessage(update)) {

            Long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();

            boolean savedSuccessfully;
            if (username == null || username.isEmpty()) {
                log.warn("Username is empty chatId: {}", chatId);
                savedSuccessfully = false;
            } else {
                savedSuccessfully = saveUserNameAndChatId(username, chatId);
            }
            sendResponse(username, chatId, savedSuccessfully);
        }
    }

    private boolean saveUserNameAndChatId(String username, Long chatId) {
        boolean savedSuccessfully = true;
        try {
            telegramService.saveUserChatId(username, chatId);
            log.info("User {} chatId saved", username);
        } catch (Exception e) {
            savedSuccessfully = false;
            log.error("Error saving user {} chatId: ", username, e);
        }
        return savedSuccessfully;
    }

    private void sendResponse(String username, Long chatId, boolean savedSuccessfully) {
        String text;
        if (savedSuccessfully) {
            text = String.format("Добро пожаловать %s! Ваши напоминания будут приходить в этот чат.", username);
        } else {
            text = "Произошла ошибка при запуске бота";
        }

        SendMessage response = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        try {
            execute(response);
        } catch (TelegramApiException e) {
            log.error("Error sending response to user: ", e);
        }
    }

    private boolean isStartMessage(Update update) {
        return update.hasMessage()
                && update.getMessage().hasText()
                && "/start".equalsIgnoreCase(update.getMessage().getText().trim());
    }
}
