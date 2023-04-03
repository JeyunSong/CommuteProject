package com.commutebot.global.auth.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CustomUser extends User {

    private final int teamCode;

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities, int teamCode) {
        super(username, password, authorities);
        this.teamCode = teamCode;
    }
}
