package com.commutebot.commute.dto.projection;

import java.time.LocalDate;

public interface WeeklyReportProjection {
    String getDateRange();
    Integer getWeeklyWorkedTime();
    Integer getWeeklyWorkedTimeAverage();
}
