package com.commutebot.commute.application.impl;

import com.commutebot.commute.application.CommuteMessageService;
import com.commutebot.commute.application.CommuteRecordService;
import com.commutebot.commute.domain.CommuteRecord;
import com.commutebot.commute.domain.CommuteRecordRepository;
import com.commutebot.commute.dto.CommuteRecordResponseDto;
import com.commutebot.global.exception.CustomException;
import com.commutebot.global.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static com.commutebot.commute.dto.CommuteRecordChangeRequestDto.CheckOut.checkOutDto;
import static com.commutebot.commute.dto.CommuteRecordResponseDto.recordDto;
import static com.commutebot.global.exception.ExceptionType.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class CommuteRecordServiceImpl implements CommuteRecordService {

    private final CommuteRecordRepository commuteRecordRepository;
    private final CommuteMessageService commuteMessageService;
    private final DateUtils dateUtils;

    @Override
    @Transactional
    public CommuteRecordResponseDto checkIn(String username, int teamCode) {

        if (teamCode == 0) throw new CustomException(INCOMPLETE_MEMBER_INFO); // TeamCode 0 : 소셜 로그인 기본 설정 (팀 설정 필요)

        LocalDate date = dateUtils.nowDate();
        String time = dateUtils.getCurrentTimeToString();

        if (commuteRecordRepository.checkAlreadyCommuteRecord(username, date)) throw new CustomException(ALREADY_CHECKIN); // 출근 이력이 존재

        CommuteRecord commuteRecord = new CommuteRecord();
        commuteRecord.changeCheckInState(username, teamCode, date, time); // 금일 출퇴근 이력 데이터 생성

        commuteRecordRepository.store(commuteRecord); // 저장
        commuteMessageService.checkInAlertToSlack(username, time); // 슬랙 알림 발송

        return recordDto(commuteRecord);
    }

    @Override
    @Transactional
    public CommuteRecordResponseDto checkOut(String username) {

        LocalDate date = dateUtils.nowDate();
        String time = dateUtils.getCurrentTimeToString();
        CommuteRecord commuteRecord = commuteRecordRepository.findTodayCommuteRecord(username, date);

        if (commuteRecord == null) throw new CustomException(NOT_CHECKIN); // 미출근
        else if (commuteRecord.getStatus().equals("정상 퇴근")) throw new CustomException(ALREADY_CHECKOUT); // 퇴근 이력이 존재

        String workedTime = commuteRecord.changeCheckOutState(checkOutDto(commuteRecord.getCheckIn(), time)); // 금일 출퇴근 이력 업데이트 후 근무 시간 리턴 (+퇴근)
        commuteMessageService.checkOutAlertToSlack(username, time, workedTime); // 슬랙 알림 발송

        return recordDto(commuteRecord);
    }
}
