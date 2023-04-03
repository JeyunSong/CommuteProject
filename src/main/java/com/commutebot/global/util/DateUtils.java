package com.commutebot.global.util;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Component
@Getter
@Slf4j
public class DateUtils {

    public LocalDate nowDate(){
        return LocalDate.now();
    }

    public LocalDateTime nowDateTime(){
        return LocalDateTime.now();
    }

    public String getCurrentTimeToString(){
        LocalDateTime dateTime = nowDateTime();
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public List<Integer> getFirstDateAndLastDateInMonth(int year, int month){
        int startDay = getFirstDateInMonth(year, month);
        int lastDay = getLastDateInMonth(year, month);

        ArrayList<Integer> list = new ArrayList<>();
        list.add(startDay);
        list.add(lastDay);

        return list;
    }

    public int getFirstDateInMonth(int year, int month){
        String date = LocalDate.of(year,month,1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return Integer.parseInt(date);
    }
    public int getLastDateInMonth(int year, int month){
        int day = Month.of(month).maxLength();
        String date = LocalDate.of(year,month,day).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return Integer.parseInt(date);
    }

    public static String convertFormatToClock(int dailyWorkedTime){
        int hour = dailyWorkedTime/60;
        int minute = dailyWorkedTime%60;

        return hour+"h " + minute+"m";
    }
}
