package com.commutebot.commute.presentation;

import com.commutebot.commute.application.CommuteRecordService;
import com.commutebot.commute.dto.CommuteRecordResponseDto;
import com.commutebot.global.auth.security.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/record")
public class CommuteRecordController {
    private final CommuteRecordService commuteRecordService;

    @PostMapping("/clock/on")
    public ResponseEntity<CommuteRecordResponseDto> checkIn(@AuthenticationPrincipal CustomUser user) {
        CommuteRecordResponseDto commuteRecord = commuteRecordService.checkIn(user.getUsername(), user.getTeamCode());
        return ResponseEntity.status(HttpStatus.OK).body(commuteRecord);
    }

    @PostMapping("/clock/off")
    public ResponseEntity<CommuteRecordResponseDto> checkOut(@AuthenticationPrincipal CustomUser user) {
        CommuteRecordResponseDto commuteRecord = commuteRecordService.checkOut(user.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(commuteRecord);
    }
}
