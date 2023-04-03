package com.commutebot.commute.domain;

import org.springframework.beans.factory.annotation.Value;

public interface MessageSender {
    void sendSlack(String text);
    void sendMail(String to, String title, String content);
}
