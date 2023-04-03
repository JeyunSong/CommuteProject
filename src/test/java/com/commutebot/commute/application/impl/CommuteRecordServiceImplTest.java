package com.commutebot.commute.application.impl;

import com.commutebot.commute.application.CommuteMessageService;
import com.commutebot.commute.dto.CommuteRecordResponseDto;
import com.commutebot.global.util.DateUtils;
import com.commutebot.commute.domain.CommuteRecord;
import com.commutebot.commute.domain.CommuteRecordRepository;
import com.commutebot.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@Nested
@DisplayName("출퇴근 기록 테스트")
@ExtendWith(MockitoExtension.class)
class CommuteRecordServiceImplTest {

    @InjectMocks
    private CommuteRecordServiceImpl commuteRecordService;
    @Mock
    private CommuteRecordRepository commuteRecordRepository;
    @Mock
    private CommuteMessageService commuteMessageService;
    @Spy
    private BCryptPasswordEncoder passwordEncoder;
    @Spy
    private DateUtils dateUtils;


    @Test
    @DisplayName("출근")
    void arrive() {
        // GIVEN
        String username = "TEST";
        int teamCode = 999;

        // WHEN
        CommuteRecordResponseDto record = commuteRecordService.checkIn(username, teamCode);

        // THEN
        assertEquals(record.getStatus(), "정상 출근");
    }

    @Test
    @DisplayName("출근 - INCOMPLETE_MEMBER_INFO Exception")
    void arrive_Exception_Case_1() {
        // GIVEN
        String username = "TEST";
        int teamCode = 0;

        // WHEN
        CustomException exception = assertThrows(CustomException.class, ()-> {
            commuteRecordService.checkIn(username, teamCode); });
        String message = exception.getMessage();

        // THEN
        assertEquals("추가 계정 정보 작성이 필요합니다.", message);
    }

    @Test
    @DisplayName("출근 - ALREADY_ARRIVE Exception")
    void arrive_Exception_Case_2() {
        // GIVEN
        String username = "TEST";
        int teamCode = 999;
        LocalDate date =LocalDate.now();

        // WHEN
        when(commuteRecordRepository.checkAlreadyCommuteRecord(username, date)).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, ()-> {
            commuteRecordService.checkIn(username, teamCode); });

        // THEN
        assertEquals("이미 출근 기록이 존재합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("퇴근")
    void leave() {
        // GIVEN
        LocalDate date = LocalDate.now();
        String username = "TEST";
        CommuteRecord commuteRecord =
                CommuteRecord.builder()
                        .username(username)
                        .teamCode(999)
                        .date(date)
                        .checkIn("09:00:00")
                        .checkOut("-")
                        .workedMinute(0)
                        .status("정상 출근")
                        .build();

        // WHEN
        when(commuteRecordRepository.findTodayCommuteRecord(username, date)).thenReturn(commuteRecord);
        CommuteRecordResponseDto record = commuteRecordService.checkOut(username);

        // THEN
        assertEquals(record.getStatus(), "정상 퇴근");
        assertNotEquals(record.getCheckOut(), "-");
    }

    @Test
    @DisplayName("퇴근 - NOT_ARRIVE Exception")
    void leave_Exception_Case_1() {
        // GIVEN
        LocalDate date = LocalDate.now();
        String username = "TEST";

        // WHEN
        when(commuteRecordRepository.findTodayCommuteRecord(username, date)).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, ()-> {
            commuteRecordService.checkOut(username); });

        // THEN
        assertEquals("출근 기록이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("퇴근 - ALREADY_LEAVE Exception")
    void leave_Exception_Case_2() {
        // GIVEN
        LocalDate date = LocalDate.now();
        String username = "TEST";
        CommuteRecord commuteRecord =
                CommuteRecord.builder()
                        .username(username)
                        .teamCode(999)
                        .date(date)
                        .checkIn("09:00:00")
                        .checkOut("18:00:00")
                        .workedMinute(0)
                        .status("정상 퇴근")
                        .build();

        // WHEN
        when(commuteRecordRepository.findTodayCommuteRecord(username, date)).thenReturn(commuteRecord);

        CustomException exception = assertThrows(CustomException.class, () -> {
            commuteRecordService.checkOut(username);});

        // THEN
        assertEquals("퇴근 기록이 존재합니다.", exception.getMessage());
    }
}