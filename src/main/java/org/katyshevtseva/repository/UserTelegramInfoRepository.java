package org.katyshevtseva.repository;

import org.katyshevtseva.entity.UserTelegramInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTelegramInfoRepository extends JpaRepository<UserTelegramInfo, String> {
}
