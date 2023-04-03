package com.commutebot.member.application;

import com.commutebot.global.auth.jwt.JwtTokenDto;
import com.commutebot.member.dto.MemberRequestDto;
import com.commutebot.member.dto.MemberResponseDto;


public interface MemberService {

    MemberResponseDto signup(MemberRequestDto.SignUp signUpRequestDto);
    JwtTokenDto login(MemberRequestDto.Login loginRequestDto);
    void withdrawal(String username);
    MemberResponseDto changeLeader(String username, String targetName);
    MemberResponseDto updateInfo(String username, MemberRequestDto.UpdateDetailInfo updateDetailInfo);
}
