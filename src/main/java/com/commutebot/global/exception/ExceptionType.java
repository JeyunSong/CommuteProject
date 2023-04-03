package com.commutebot.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionType {

    // Default
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_EXIST_DATA(HttpStatus.NOT_FOUND, "요청한 데이터가 존재하지 않습니다."),

    // Authentication
    ALREADY_EXIST_USERNAME(HttpStatus.CONFLICT, "중복되는 이름입니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.CONFLICT, "중복되는 이메일입니다."),
    INCOMPLETE_MEMBER_INFO(HttpStatus.BAD_REQUEST, "추가 계정 정보 작성이 필요합니다."),

    // Commute
    NOT_CHECKIN(HttpStatus.NOT_FOUND, "출근 기록이 존재하지 않습니다."),
    ALREADY_CHECKIN(HttpStatus.CONFLICT, "출근 기록이 존재합니다."),
    ALREADY_CHECKOUT(HttpStatus.CONFLICT, "퇴근 기록이 존재합니다."),

    // Token Valid Check
    NOT_VALID_TOKEN(HttpStatus.NOT_FOUND, "토큰이 존재하지 않습니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "지원되지 않는 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "만료된 토큰입니다.");


    private final HttpStatus httpStatus;
    private final String message;
    }
