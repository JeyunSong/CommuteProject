package com.commutebot.global.auth.jwt;

import com.commutebot.member.dto.MemberRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationBuilder {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // Login Request 정보로 UsernamePasswordAuthenticationToken 을 만들어 authenticate() 메소드로 검증합니다.
    // 이 때 loadUserByUsername() 이 동작하면서 DB 상의 정보와 대조 후 일치하다면 권한 객체를 반환합니다.

    public Authentication getAuthenticationForLogin(MemberRequestDto.Login loginRequestDto){
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }

}
