package com.commutebot.global.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AccessLogger {

    Logger log = LoggerFactory.getLogger("ACCESS");

    private String getURL(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getRequestURL());

        String queryString = request.getQueryString();
        if (queryString != null) {
            sb.append("?").append(queryString);
        }
        return sb.toString();
    }

    public void log(HttpServletRequest request, HttpServletResponse response, long timeTaken) {
        String accessLog = buildAccessLog(request, response, timeTaken);
    }

    private String buildAccessLog(HttpServletRequest request, HttpServletResponse response, long timeTaken) {

        String url = getURL(request);
        String method = request.getMethod();
        String contentType = request.getContentType();
        String clientIp = getClientIP(request);
        int status = response.getStatus();

        StringBuilder sb = new StringBuilder();
        sb      .append("{")
                .append("\"").append("url").append("\"")
                .append(":")
                .append("\"").append(url).append("\"");
        if (method != null) {
            sb.append(",")
                .append("\"").append("method").append("\"")
                .append(":")
                .append("\"").append(method).append("\"");}
        if (contentType != null) {
            sb.append(",")
                .append("\"").append("contentType").append("\"")
                .append(":")
                .append("\"").append(contentType).append("\"");}
        if (status != 0) {
            sb.append(",")
                .append("\"").append("status").append("\"")
                .append(":")
                .append("\"").append(status).append("\"");}
        if (clientIp != null) {
            sb.append(",")
                .append("\"").append("clientIp").append("\"")
                .append(":")
                .append("\"").append(clientIp).append("\"");}
        if (timeTaken != 0) {
            sb.append(",")
                .append("\"").append("timeTaken").append("\"")
                .append(":")
                .append(timeTaken)
                .append("ms");}
        sb.append("}");
        return sb.toString();
    }

    public String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) ip = request.getHeader("Proxy-Client-IP");
        if (ip == null) ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null) ip = request.getHeader("HTTP_CLIENT_IP");
        if (ip == null) ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip == null) ip = request.getRemoteAddr();
        return ip;
    }
}