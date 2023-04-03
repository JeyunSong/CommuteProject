package com.commutebot.commute.dto;


import com.commutebot.global.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommuteRecordChangeRequestDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckOut {
        private String checkOutTime;
        private int workedMinute;
        private String workedMinuteFormatClock;

        public static CheckOut checkOutDto(String checkIn, String time) {
            int min = circulateWorkedMinute(checkIn, time);
            return new CheckOut(
                    time,
                    min,
                    DateUtils.convertFormatToClock(min));
        }
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AutoCheckOut {
        private String checkOutTime;
        private int workedMinute;

        public static AutoCheckOut autoCheckOutDto(String checkIn) {
            int min = circulateWorkedMinute(checkIn, "");
            return new AutoCheckOut(
                    circulateAutoCheckOutTime(checkIn),
                    min
            );
        }
    }

    // Circulate
    private static String circulateAutoCheckOutTime(String checkIn){
        int time = Integer.parseInt(checkIn.substring(0,2));
        int modifiedTime = time + 9;

        return checkIn.replaceFirst(time+"", modifiedTime+"");
    }

    private static int circulateWorkedMinute(String checkIn, String checkOut){
        if (checkOut.equals("")) return 540;
        int hour = Integer.parseInt(checkOut.substring(0, 2)) - Integer.parseInt(checkIn.substring(0, 2));
        int minute = Integer.parseInt(checkOut.substring(3, 5)) - Integer.parseInt(checkIn.substring(3, 5));

        return  (hour*60)+minute;
    }
}
