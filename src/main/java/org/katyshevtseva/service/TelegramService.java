package org.katyshevtseva.service;

import lombok.RequiredArgsConstructor;
import org.katyshevtseva.entity.UserTelegramInfo;
import org.katyshevtseva.repository.UserTelegramInfoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramService {

    private final UserTelegramInfoRepository telegramInfoRepository;

    public void saveUserChatId(String username, Long chatId) {
        UserTelegramInfo info = new UserTelegramInfo(username, chatId);
        telegramInfoRepository.save(info);
    }
}
