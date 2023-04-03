package com.commutebot.commute.application.impl;

import com.commutebot.commute.application.CommuteMessageService;
import com.commutebot.commute.dto.CommuteMessageWriter;
import com.commutebot.commute.infrastructure.MessageSenderImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommuteAlertServiceImpl implements CommuteMessageService {

    private final MessageSenderImpl messageSender;
    private final CommuteMessageWriter commuteMessageWriter;


    @Override
    @Transactional
    public void checkInAlertToSlack(String username, String checkIn) {
        String message = commuteMessageWriter.checkInAlert(username,checkIn);
        messageSender.sendSlack(message);
    }

    @Override
    @Transactional
    public void checkOutAlertToSlack(String username, String checkOut, String workedTime) {
        String message = commuteMessageWriter.checkOutAlert(username,checkOut,workedTime);
        messageSender.sendSlack(message);
    }
}
