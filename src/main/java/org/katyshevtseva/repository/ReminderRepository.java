package org.katyshevtseva.repository;

import org.katyshevtseva.entity.Reminder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    Page<Reminder> findByUserProfileId(String userId, Pageable pageable);

    List<Reminder> findByUserProfileId(String userId);

    List<Reminder> findByUserProfileIdOrderByRemind(@Param("userId") String userId);

    @Query(value = "SELECT * FROM reminder r WHERE r.user_id = :userId " +
            "ORDER BY CAST(r.remind AS time)", nativeQuery = true)
    List<Reminder> findByUserIdOrderByTime(@Param("userId") String userId);

    List<Reminder> findByUserProfileIdOrderByTitle(String userId);

    List<Reminder> findByUserProfileIdAndRemind(String userId, LocalDateTime remind);

    @Query(value = "SELECT * FROM reminder r WHERE CAST(r.remind AS time) = :time", nativeQuery = true)
    List<Reminder> findByUserProfileIdAndRemindOnlyTime(@Param("time") LocalTime time);

    @Query(value = "SELECT * FROM reminder r WHERE DATE(r.remind) = :date", nativeQuery = true)
    List<Reminder> findByUserProfileIdAndRemindOnlyDate(@Param("date") LocalDate date);


    @Query(value = "SELECT * FROM reminder r WHERE r.user_id = :userId " +
            "AND (CAST(:date AS date) IS NULL OR DATE(r.remind) = :date) "
            + "AND (CAST(:time AS time) IS NULL OR CAST(r.remind AS time) = :time)",
            nativeQuery = true)
    List<Reminder> findFilteredReminders(
            @Param("userId") String userId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time
    );
}
