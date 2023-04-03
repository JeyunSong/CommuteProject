package com.commutebot.member.domain;

import lombok.Getter;

@Getter
public enum SignUpChannel {

    MAIN("메인"), GOOGLE("구글");

    private final String description;
    SignUpChannel(String description) {
        this.description = description;
    }
}
