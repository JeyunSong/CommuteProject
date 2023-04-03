package com.commutebot.commute.scheduled;

import com.commutebot.commute.application.CommuteScheduledService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Component
public class CommuteScheduler {
    private final CommuteScheduledService commuteScheduledService;

    @Scheduled(cron = "0 0 06 * * *")
    public void autoCheckOut() {
        commuteScheduledService.autoCheckOut();
    }

    @Scheduled(cron = "0 0 22 * * *")
    public void regularReport() {
        commuteScheduledService.regularCommuteReportToTeamLeader();
    }
}
