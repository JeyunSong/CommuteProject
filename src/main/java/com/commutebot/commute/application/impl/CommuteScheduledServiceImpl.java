package com.commutebot.commute.application.impl;

import com.commutebot.commute.application.CommuteScheduledService;
import com.commutebot.commute.domain.CommuteRecord;
import com.commutebot.commute.domain.CommuteRecordRepository;
import com.commutebot.commute.domain.MessageSender;
import com.commutebot.commute.dto.CommuteMessageWriter;
import com.commutebot.global.util.DateUtils;
import com.commutebot.member.domain.Team;
import com.commutebot.member.domain.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static com.commutebot.commute.dto.CommuteRecordChangeRequestDto.AutoCheckOut.autoCheckOutDto;


@Service
@RequiredArgsConstructor
public class CommuteScheduledServiceImpl implements CommuteScheduledService {

    private final DateUtils dateUtils;
    private final CommuteMessageWriter commuteMessageWriter;
    private final MessageSender messageSender;
    private final CommuteRecordRepository commuteRecordRepository;
    private final TeamRepository teamRepository;



    @Override
    @Transactional
    public void regularCommuteReportToTeamLeader() {

        LocalDate date = dateUtils.nowDate();
        List<Team> teamList = teamRepository.getAllTeam();

        for (Team team : teamList) {
            if (team.getLeader().equals("-")) break; // NOT EXIST LEADER

            String to = commuteMessageWriter.regularCommuteReportTo(team.getLeader());             // 메세지를 수신할 사람
            String title = commuteMessageWriter.regularCommuteReportTitle(date, team.getName());   // 메세지 제목
            StringBuilder content = new StringBuilder();                                           // 메세지 내용 저장 객체

            List<CommuteRecord> records = commuteRecordRepository.getYesterdayCommuteRecord(date, team.getCode());

            if (records.size() == 0) content.append("부서원의 출근 기록이 없습니다.");
            else {
                for (CommuteRecord record : records) {
                    content.append(commuteMessageWriter.regularCommuteReport(record));
                }
            }
            messageSender.sendMail(to, title, String.valueOf(content));
        }
    }

    @Override
    @Transactional
    public void autoCheckOut() {

        LocalDate date = dateUtils.nowDate();
        List<CommuteRecord> commuteRecordList = commuteRecordRepository.findNotLeaveMembers(date); // 미퇴근자 조회

        if (commuteRecordList.size() > 0) {
            for (CommuteRecord commuteRecord : commuteRecordList) {
                commuteRecord.changeAutoCheckOutState(autoCheckOutDto(commuteRecord.getCheckIn())); // 퇴근 누락으로 상태 변경
            }
        }
    }
}
