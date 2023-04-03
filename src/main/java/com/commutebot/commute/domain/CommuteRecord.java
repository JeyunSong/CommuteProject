package com.commutebot.commute.domain;

import com.commutebot.commute.dto.CommuteRecordChangeRequestDto.AutoCheckOut;
import com.commutebot.commute.dto.CommuteRecordChangeRequestDto.CheckOut;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommuteRecord {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int pk;

        @Column(nullable = false)
        private String username;

        @Column(nullable = false)
        private int teamCode;

        @Column(nullable = false)
        private LocalDate date;

        @Column(nullable = false)
        private String checkIn;

        @Column(nullable = false)
        private String checkOut;

        @Column(nullable = false)
        private int workedMinute;

        @Column(nullable = false)
        private String status;


        public CommuteRecord(String username, int teamCode, LocalDate date, String checkIn, String checkOut, int workedMinute, String status) {
                this.username = username;
                this.teamCode = teamCode;
                this.date = date;
                this.checkIn = checkIn;
                this.checkOut = checkOut;
                this.workedMinute = workedMinute;
                this.status = status;
        }

        public void changeCheckInState(String username, int teamCode, LocalDate date, String time){
                this.username = username;
                this.teamCode = teamCode;
                this.date = date;
                this.checkIn = time;
                this.checkOut = "-";
                this.workedMinute = 0;
                this.status = "정상 출근";
        }

        public String changeCheckOutState(CheckOut checkOut){
                this.checkOut = checkOut.getCheckOutTime();
                this.status = "정상 퇴근";
                this.workedMinute = checkOut.getWorkedMinute();
                return checkOut.getWorkedMinuteFormatClock();
        }

        public void changeAutoCheckOutState(AutoCheckOut autoCheckOut){
                this.checkOut = autoCheckOut.getCheckOutTime();
                this.status = "퇴근 누락";
                this.workedMinute = autoCheckOut.getWorkedMinute();
        }
}
