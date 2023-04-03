package com.commutebot.commute.domain;

import com.commutebot.commute.dto.projection.DailyReportProjection;
import com.commutebot.commute.dto.projection.MonthlyReportProjection;
import com.commutebot.commute.dto.projection.WeeklyReportProjection;

import java.time.LocalDate;
import java.util.List;

public interface CommuteRecordRepository {
    void store(CommuteRecord commuteRecord);
    boolean checkAlreadyCommuteRecord(String username, LocalDate date);
    CommuteRecord findTodayCommuteRecord(String username, LocalDate date);
    List<CommuteRecord> findNotLeaveMembers(LocalDate date);
    List<CommuteRecord> getYesterdayCommuteRecord(LocalDate date, int teamCode);
    List<DailyReportProjection> getDailyCommuteAnalysisReport(String username, int startDate, int lastDate);
    List<WeeklyReportProjection> getWeeklyCommuteAnalysisReport(String username, int startDate, int lastDate);
    List<MonthlyReportProjection> getMonthlyCommuteAnalysisReport(String username, int startDate, int lastDate);

}
