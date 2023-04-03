package com.commutebot.commute.application;

import com.commutebot.commute.dto.CommuteRecordResponseDto;
import org.springframework.scheduling.annotation.Scheduled;

public interface CommuteRecordService {

    CommuteRecordResponseDto checkIn(String username, int teamCode);
    CommuteRecordResponseDto checkOut(String username);
}
