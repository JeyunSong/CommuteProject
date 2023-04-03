package com.commutebot.commute.application.impl;

import com.commutebot.commute.domain.CommuteRecord;
import com.commutebot.commute.domain.CommuteRecordRepository;
import com.commutebot.commute.domain.MessageSender;
import com.commutebot.commute.dto.CommuteMessageWriter;
import com.commutebot.global.util.DateUtils;
import com.commutebot.member.domain.Team;
import com.commutebot.member.domain.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Nested
@DisplayName("출퇴근 기록 테스트")
@ExtendWith(MockitoExtension.class)
class CommuteScheduledServiceImplTest {

    @InjectMocks
    private CommuteScheduledServiceImpl commuteScheduledService;
    @Spy
    private  DateUtils dateUtils;
    @Spy
    private  CommuteMessageWriter commuteMessageWriter;
    @Mock
    private  MessageSender messageSender;
    @Mock
    private  CommuteRecordRepository commuteRecordRepository;
    @Mock
    private  TeamRepository teamRepository;

    @Test
    @DisplayName("팀장 정기 보고")
    void regularCommuteReportToTeamLeader_1() {
        // GIVEN
        LocalDate date = LocalDate.from(LocalDateTime.now());
        int teamCode = 101;
        String teamName = "A";
        String leaderName = "A";

        Team team_A = new Team(teamCode, teamName, leaderName);
        List<Team> teamList = new ArrayList<>();
        teamList.add(team_A);

        CommuteRecord commuteRecord_1 = new CommuteRecord("TEST_1", 101, date, "TEST", "TEST", 540, "TEST");
        CommuteRecord commuteRecord_2 = new CommuteRecord("TEST_2", 101, date, "TEST", "TEST", 540, "TEST");
        List<CommuteRecord> commuteRecords = new ArrayList<>();
        commuteRecords.add(commuteRecord_1);
        commuteRecords.add(commuteRecord_2);

        StringBuilder content = new StringBuilder();
        for (CommuteRecord commuteRecord : commuteRecords) {
            content.append(commuteMessageWriter.regularCommuteReport(commuteRecord));
        }

        // WHEN
        when(teamRepository.getAllTeam()).thenReturn(teamList);
        when(commuteRecordRepository.getYesterdayCommuteRecord(date, teamCode)).thenReturn(commuteRecords);

        commuteScheduledService.regularCommuteReportToTeamLeader();

        // THEN
        verify(messageSender, times(1)).sendMail(anyString(), anyString(), eq(String.valueOf(content)));
    }

    @Test
    @DisplayName("팀장 정기 보고 - Not Exist Member In Team")
    void regularCommuteReportToTeamLeader_2() {
        // GIVEN
        LocalDate date = LocalDate.from(LocalDateTime.now());
        int teamCode = 101;
        String teamName = "A";
        String leaderName = "A";

        Team team_A = new Team(teamCode, teamName, leaderName);
        List<Team> teamList = new ArrayList<>();
        teamList.add(team_A);

        List<CommuteRecord> commuteRecords = new ArrayList<>();

        // WHEN
        when(teamRepository.getAllTeam()).thenReturn(teamList);
        when(commuteRecordRepository.getYesterdayCommuteRecord(date, teamCode)).thenReturn(commuteRecords);

        commuteScheduledService.regularCommuteReportToTeamLeader();

        // THEN
        verify(messageSender, times(1)).sendMail(anyString(), anyString(), eq("부서원의 출근 기록이 없습니다."));
    }

    @Test
    @DisplayName("팀장 정기 보고 - Not Exist Leader In Team")
    void regularCommuteReportToTeamLeader_3() {
        // GIVEN
        int teamCode = 101;
        String teamName = "A";
        String leaderName = "-";

        Team team_A = new Team(teamCode, teamName, leaderName);
        List<Team> teamList = new ArrayList<>();
        teamList.add(team_A);

        // WHEN
        when(teamRepository.getAllTeam()).thenReturn(teamList);

        commuteScheduledService.regularCommuteReportToTeamLeader();

        // THEN
        verify(messageSender, never()).sendMail(anyString(), anyString(), anyString());
    }
}