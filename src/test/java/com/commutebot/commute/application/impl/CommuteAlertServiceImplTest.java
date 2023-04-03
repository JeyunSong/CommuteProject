package com.commutebot.commute.application.impl;

import com.commutebot.commute.dto.CommuteMessageWriter;
import com.commutebot.commute.infrastructure.MessageSenderImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;


@Nested
@DisplayName("출퇴근 메세징 테스트")
@ExtendWith(MockitoExtension.class)
class CommuteAlertServiceImplTest {

    @InjectMocks
    private CommuteAlertServiceImpl commuteMessageService;
    @Mock
    private MessageSenderImpl messageSender;
    @Spy
    private CommuteMessageWriter commuteMessageWriter;

    @Test
    @DisplayName("출근 슬랙 메세지")
    void checkInAlertToSlack() {
        // GIVEN
        String username = "test";
        String checkIn = "09:00:00";
        String content  = commuteMessageWriter.checkInAlert(username,checkIn);

        // WHEN
        commuteMessageService.checkInAlertToSlack(username, checkIn);

        // THEN
        verify(messageSender, times(1)).sendSlack(eq(content));

    }

    @Test
    @DisplayName("퇴근 슬랙 메세지")
    void checkOutAlertToSlack() {
        // GIVEN
        String username = "test";
        String checkOut = "18:00:00";
        String workedTime = "9h 0m";
        String content  = commuteMessageWriter.checkOutAlert(username,checkOut,workedTime);

        // WHEN
        commuteMessageService.checkOutAlertToSlack(username, checkOut, workedTime);

        // THEN
        verify(messageSender, times(1)).sendSlack(eq(content));

    }
}