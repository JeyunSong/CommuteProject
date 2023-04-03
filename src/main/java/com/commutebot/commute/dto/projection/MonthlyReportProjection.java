package com.commutebot.commute.dto.projection;

public interface MonthlyReportProjection {

    Integer getMonth();
    Integer getMonthlyWorkedTime();
    Integer getMonthlyWorkedTimeAverage();
}
