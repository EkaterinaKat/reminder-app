package org.katyshevtseva.repository;

import jakarta.persistence.LockModeType;
import org.katyshevtseva.domain.ReminderEmailStatus;
import org.katyshevtseva.domain.ReminderTelegramStatus;
import org.katyshevtseva.entity.Reminder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    Page<Reminder> findByUserProfileId(String userId, Pageable pageable);

    List<Reminder> findByUserProfileIdOrderByRemind(@Param("userId") String userId);

    @Query(value = "SELECT * FROM reminder r WHERE r.user_id = :userId " +
            "ORDER BY CAST(r.remind AS time)", nativeQuery = true)
    List<Reminder> findByUserIdOrderByTime(@Param("userId") String userId);

    List<Reminder> findByUserProfileIdOrderByTitle(String userId);

    @Query(value = "SELECT * FROM reminder r WHERE r.user_id = :userId " +
            "AND (CAST(:date AS date) IS NULL OR DATE(r.remind) = :date) "
            + "AND (CAST(:time AS time) IS NULL OR CAST(r.remind AS time) = :time)",
            nativeQuery = true)
    List<Reminder> findFilteredReminders(
            @Param("userId") String userId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Reminder r WHERE r.remind <= :date " +
            "AND (r.telegramStatus = :telegramStatus OR r.emailStatus = :emailStatus)")
    List<Reminder> findByRemindBeforeAndByStatuses(
            LocalDateTime date,
            ReminderTelegramStatus telegramStatus,
            ReminderEmailStatus emailStatus
    );
}
