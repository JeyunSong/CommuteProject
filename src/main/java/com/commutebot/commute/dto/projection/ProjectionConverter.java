package com.commutebot.commute.dto.projection;

import com.commutebot.commute.dto.CommuteAnalysisReportResponseDto;
import com.commutebot.global.exception.CustomException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.commutebot.commute.dto.CommuteAnalysisReportResponseDto.*;
import static com.commutebot.global.exception.ExceptionType.NOT_EXIST_DATA;

// Native Query Mapping Projection -> DTO
@Component
public class ProjectionConverter {

    public List<Daily> convertDailyDto(List<DailyReportProjection> projections){
        if (projections == null) throw new CustomException(NOT_EXIST_DATA);

        List<Daily> dailyReportDto = new ArrayList<>();
        for (DailyReportProjection daily : projections) {
            dailyReportDto.add(Daily.newDailyReport(daily.getDate(),daily.getCheckIn(), daily.getCheckOut(), daily.getDailyWorkedTime()));
        }
        return dailyReportDto;
    }
    public List<Weekly> convertWeeklyDto(List<WeeklyReportProjection> projections) {
        if (projections == null) throw new CustomException(NOT_EXIST_DATA);

        List<Weekly> weeklyReportDto = new ArrayList<>();
        for (WeeklyReportProjection weekly : projections) {
            weeklyReportDto.add(Weekly.newWeeklyReport(weekly.getDateRange(), weekly.getWeeklyWorkedTime(), weekly.getWeeklyWorkedTimeAverage()));
        }
        return weeklyReportDto;
    }
    public List<Monthly> convertMonthlyDto(List<MonthlyReportProjection> projections) {
        if (projections == null) throw new CustomException(NOT_EXIST_DATA);

        List<Monthly> monthlyReportDto = new ArrayList<>();
        for (MonthlyReportProjection monthly : projections) {
            monthlyReportDto.add(Monthly.newMonthlyReport(monthly.getMonth(), monthly.getMonthlyWorkedTime(), monthly.getMonthlyWorkedTimeAverage()));
        }
        return monthlyReportDto;
    }
}
