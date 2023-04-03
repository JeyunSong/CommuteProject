package com.commutebot.commute.dto.projection;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DailyReportProjection {

    LocalDate getDate();
    String getCheckIn();
    String getCheckOut();
    Integer getDailyWorkedTime();
}
