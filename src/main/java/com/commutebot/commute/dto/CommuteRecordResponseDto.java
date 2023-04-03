package com.commutebot.commute.dto;

import com.commutebot.commute.domain.CommuteRecord;
import com.commutebot.global.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommuteRecordResponseDto {
    private String username;
    private String date;
    private String checkIn;
    private String checkOut;
    private int workedMinute;
    private String status;

    public static CommuteRecordResponseDto recordDto(CommuteRecord commuteRecord) {
        return new CommuteRecordResponseDto(
                commuteRecord.getUsername(),
                String.valueOf(commuteRecord.getDate()),
                commuteRecord.getCheckIn(),
                commuteRecord.getCheckOut(),
                commuteRecord.getWorkedMinute(),
                commuteRecord.getStatus());
    }
}
