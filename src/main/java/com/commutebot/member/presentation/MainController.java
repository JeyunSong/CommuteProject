package com.commutebot.member.presentation;

import com.commutebot.global.auth.jwt.JwtTokenProvider;
import com.commutebot.global.auth.security.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MainController {

    private final JwtTokenProvider jwtTokenProvider; // Redirect OAuth2 Sample

    @GetMapping("/main")
    public ResponseEntity<String> main(@AuthenticationPrincipal CustomUser user) {
        String msg = "안녕하세요! " + user.getUsername() + "님";
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    // Redirect OAuth2 Sample
    @GetMapping("/main/{token}")
    public ResponseEntity<String> redirect(@PathVariable String token) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        return ResponseEntity.status(HttpStatus.OK).body("반갑습니다. " + authentication.getName() + "님 !");
    }
}
