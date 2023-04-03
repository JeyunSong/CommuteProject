package com.commutebot.commute.application.impl;

import com.commutebot.commute.application.CommuteAnalysisReportService;
import com.commutebot.commute.domain.CommuteRecordRepository;
import com.commutebot.commute.dto.projection.ProjectionConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.commutebot.commute.dto.CommuteAnalysisReportResponseDto.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommuteAnalysisReportServiceImpl implements CommuteAnalysisReportService {
    private final CommuteRecordRepository commuteRecordRepository;
    private final ProjectionConverter projectionConverter;

    @Override
    @Transactional
    public List<Daily> getDailyReport(int startDate, int lastDate, String username) {
        try {
            return projectionConverter.convertDailyDto(commuteRecordRepository.getDailyCommuteAnalysisReport(username, startDate, lastDate));
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public List<Weekly> getWeeklyReport(int startDate, int lastDate, String username) {
        try {
            return projectionConverter.convertWeeklyDto(commuteRecordRepository.getWeeklyCommuteAnalysisReport(username, startDate, lastDate));
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public List<Monthly> getMonthlyReport(int startDate, int lastDate, String username){
        try {
            return projectionConverter.convertMonthlyDto(commuteRecordRepository.getMonthlyCommuteAnalysisReport(username, startDate, lastDate));
        } catch (NullPointerException e) {
            return null;
        }
    }
}



