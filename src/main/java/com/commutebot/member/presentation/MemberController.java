package com.commutebot.member.presentation;

import com.commutebot.global.auth.jwt.JwtTokenDto;
import com.commutebot.global.auth.security.CustomUser;
import com.commutebot.member.application.MemberService;
import com.commutebot.member.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.commutebot.member.dto.MemberRequestDto.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;


    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@Valid @RequestBody SignUp signUpRequestDto) {
        MemberResponseDto member = memberService.signup(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody Login loginRequestDto, HttpServletResponse response){
        JwtTokenDto token = memberService.login(loginRequestDto);
        response.addHeader("AUTHORIZATION_HEADER", token.getAccessToken());
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS");
    }

    @DeleteMapping("/management/members")
    public ResponseEntity<String> withdrawal(@AuthenticationPrincipal User user) {
        memberService.withdrawal(user.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS");
    }

    @PatchMapping("/management/members/info")
    public ResponseEntity<MemberResponseDto> changeDetailInfo(@AuthenticationPrincipal CustomUser user, @Valid @RequestBody UpdateDetailInfo updateDetailInfo) {
        MemberResponseDto member = memberService.updateInfo(user.getUsername(), updateDetailInfo);
        return ResponseEntity.status(HttpStatus.OK).body(member);
    }

    @PatchMapping("/management/members/role")
    public ResponseEntity<MemberResponseDto> changeLeader(@AuthenticationPrincipal CustomUser user, @RequestBody String targetName) {
        MemberResponseDto member = memberService.changeLeader(user.getUsername(), targetName);
        return ResponseEntity.status(HttpStatus.OK).body(member);
    }
}
