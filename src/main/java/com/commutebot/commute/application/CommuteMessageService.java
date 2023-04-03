package com.commutebot.commute.application;

public interface CommuteMessageService {
    void checkInAlertToSlack(String username, String checkIn);
    void checkOutAlertToSlack(String username, String checkOut, String workedTime);
}
