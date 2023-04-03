package com.commutebot.commute.dto;

import com.commutebot.global.util.DateUtils;
import com.commutebot.member.dto.MemberRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CommuteAnalysisReportResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Daily {
        private LocalDate date;
        private String checkIn;
        private String checkOut;
        private String dailyWorkedTime;

        public static Daily newDailyReport(LocalDate date, String checkIn, String checkOut, int dailyWorkedTime) {
            return new Daily(
                    date,
                    checkIn,
                    checkOut,
                    DateUtils.convertFormatToClock(dailyWorkedTime));
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Weekly {
        private String dateRange;
        private String weeklyWorkedTime;
        private String weeklyWorkedTimeAverage;

        public static Weekly newWeeklyReport(String dateRange, int weeklyWorkedTime, int weeklyWorkedTimeAverage) {
            return new Weekly(
                    dateRange,
                    DateUtils.convertFormatToClock(weeklyWorkedTime),
                    DateUtils.convertFormatToClock(weeklyWorkedTimeAverage));
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Monthly {
        private int month;
        private String monthlyWorkedTime;
        private String monthlyWorkedTimeAverage;

        public static Monthly newMonthlyReport(int month, int monthlyWorkedTime, int monthlyWorkedTimeAverage) {
            return new Monthly(
                    month,
                    DateUtils.convertFormatToClock(monthlyWorkedTime),
                    DateUtils.convertFormatToClock(monthlyWorkedTimeAverage));
        }
    }
}
