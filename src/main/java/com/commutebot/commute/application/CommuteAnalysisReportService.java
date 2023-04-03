package com.commutebot.commute.application;

import java.util.List;

import static com.commutebot.commute.dto.CommuteAnalysisReportResponseDto.*;

public interface CommuteAnalysisReportService {
    List<Daily> getDailyReport(int startDate, int lastDate, String username);
    List<Weekly> getWeeklyReport(int startDate, int lastDate, String username);
    List<Monthly> getMonthlyReport(int startDate, int lastDate, String username);

}
