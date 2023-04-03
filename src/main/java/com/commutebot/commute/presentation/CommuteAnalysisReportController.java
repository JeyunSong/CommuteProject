package com.commutebot.commute.presentation;


import com.commutebot.commute.application.CommuteAnalysisReportService;
import com.commutebot.global.util.DateUtils;
import com.commutebot.global.auth.security.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.commutebot.commute.dto.CommuteAnalysisReportResponseDto.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/report")
public class CommuteAnalysisReportController {

    private final CommuteAnalysisReportService commuteAnalysisReportService;
    private final DateUtils dateUtils;

    @GetMapping("/daily/{month}/{year}")
    public ResponseEntity<List<Daily>> dailyReport(@PathVariable int month, @PathVariable int year, @AuthenticationPrincipal CustomUser user) {

        List<Integer> date = dateUtils.getFirstDateAndLastDateInMonth(year, month);
        List<Daily> list = commuteAnalysisReportService.getDailyReport(date.get(0), date.get(1), user.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }
    @GetMapping("/weekly/{month}/{year}")
    public ResponseEntity<List<Weekly>> weeklyReport(@PathVariable int month, @PathVariable int year, @AuthenticationPrincipal CustomUser user) {

        List<Integer> date = dateUtils.getFirstDateAndLastDateInMonth(year, month);
        List<Weekly> list = commuteAnalysisReportService.getWeeklyReport(date.get(0), date.get(1), user.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }
    @GetMapping("/monthly/{month}/{year}")
    public ResponseEntity<List<Monthly>> monthlyReport(@PathVariable int month, @PathVariable int year, @AuthenticationPrincipal CustomUser user) {

        List<Integer> date = dateUtils.getFirstDateAndLastDateInMonth(year, month);
        List<Monthly> list = commuteAnalysisReportService.getMonthlyReport(date.get(0), date.get(1), user.getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }
}
