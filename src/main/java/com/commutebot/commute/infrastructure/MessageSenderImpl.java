package com.commutebot.commute.infrastructure;

import com.commutebot.commute.domain.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MessageSenderImpl implements MessageSender {

    @Value("${slack.webhook}")
    private static String slackWebHookup;

    @Value("${gmail.from}")
    private static String from;

    private static JavaMailSender javaMailSender;

    @Autowired
    private void setMail(JavaMailSender javaMailSender) {
        MessageSenderImpl.javaMailSender = javaMailSender;
    }


    @Override
    public void sendMail(String to, String title, String content) {
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom(from);
        mail.setTo(to);
        mail.setSubject(title);
        mail.setText(content);

        javaMailSender.send(mail);
    }

    @Override
    public void sendSlack(String content) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> request = new HashMap<>();
        request.put("username", "너구리");
        request.put("text", content);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(request);

        String url = slackWebHookup;

        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
}
