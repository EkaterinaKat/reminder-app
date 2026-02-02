package org.katyshevtseva.scheduler;

import lombok.RequiredArgsConstructor;
import org.katyshevtseva.service.RemindSchedulerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemindScheduler {

    private final RemindSchedulerService service;

    @Scheduled(fixedRateString = "${reminder.send.interval-ms:60000}")
    public void execute() {
        service.sendReminds();
    }
}
