package com.commutebot.member.domain;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_COMMON("팀원"), ROLE_LEADER("팀장"), ROLE_ADMIN("관리자");

    private final String description;
    Role(String description) {
        this.description = description;
    }
}
