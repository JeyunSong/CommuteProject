package com.commutebot.commute.application.impl;

import com.commutebot.commute.application.CommuteAnalysisReportService;
import com.commutebot.commute.domain.CommuteRecordRepository;
import com.commutebot.commute.dto.projection.DailyReportProjection;
import com.commutebot.commute.dto.projection.ProjectionConverter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
@DisplayName("출퇴근 분석 리포트 테스트")
@ExtendWith(MockitoExtension.class)
class CommuteAnalysisReportServiceImplTest {

    @InjectMocks
    private CommuteAnalysisReportServiceImpl commuteAnalysisService;
    @Mock
    private CommuteRecordRepository commuteRecordRepository;
   @Mock
    private ProjectionConverter projectionConverter;


    @Test
    @DisplayName("일간 리포트")
    void getDailyReport() {
        // WHEN
        commuteAnalysisService.getDailyReport(anyInt(), anyInt(), anyString());

        // THEN
        verify(projectionConverter, times(1)).convertDailyDto(anyList());
    }

    @Test
    @DisplayName("주간 리포트")
    void getWeeklyReport() {
        // WHEN
        commuteAnalysisService.getWeeklyReport(anyInt(), anyInt(), anyString());

        // THEN
        verify(projectionConverter, times(1)).convertWeeklyDto(anyList());
    }

    @Test
    @DisplayName("월간 리포트")
    void getMonthlyReport() {
        // WHEN
        commuteAnalysisService.getMonthlyReport(anyInt(), anyInt(), anyString());

        // THEN
        verify(projectionConverter, times(1)).convertMonthlyDto(anyList());
    }
}