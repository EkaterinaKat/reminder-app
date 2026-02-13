package org.katyshevtseva.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_telegram_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTelegramInfo {

    @Id
    private String userName;

    private Long chatId;
}
