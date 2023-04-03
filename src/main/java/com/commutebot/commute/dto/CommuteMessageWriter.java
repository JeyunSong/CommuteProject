package com.commutebot.commute.dto;

import com.commutebot.commute.domain.CommuteRecord;
import com.commutebot.global.util.DateUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CommuteMessageWriter {

    public String checkInAlert(String username, String checkIn){
        return "[" + checkIn + "] "+ username + "님이 출근하셨습니다.";
    }

    public String checkOutAlert(String username, String checkOut, String workedTime){
        return "[" + checkOut + "] "+ username + "님이 퇴근하셨습니다. 총 근무 시간은 : " + workedTime + " 입니다.";
    }

    public String regularCommuteReport(CommuteRecord commuteRecord){

        StringBuilder content = new StringBuilder();

        return String.valueOf(content.append(commuteRecord.getUsername())
                .append(" - ")
                .append("출근 : [")
                .append(commuteRecord.getCheckIn())
                .append("] , 퇴근 : [")
                .append(commuteRecord.getCheckOut())
                .append("] , 총 근무 시간 : [")
                .append(DateUtils.convertFormatToClock(commuteRecord.getWorkedMinute()))
                .append("], ")
                .append(commuteRecord.getStatus())
                .append("\n"));
    }

    public String regularCommuteReportTitle(LocalDate date, String teamName){
        return "[" + date + "] " + teamName + "팀 출퇴근 리포트";
    }
    public String regularCommuteReportTo(String username){
        return username + "@sweettracker.co.kr";
    }
}
