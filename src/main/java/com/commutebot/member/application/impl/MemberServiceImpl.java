package com.commutebot.member.application.impl;

import com.commutebot.global.auth.jwt.AuthenticationBuilder;
import com.commutebot.global.auth.jwt.JwtTokenDto;
import com.commutebot.global.auth.jwt.JwtTokenProvider;
import com.commutebot.global.exception.CustomException;
import com.commutebot.member.application.MemberService;
import com.commutebot.member.domain.*;
import com.commutebot.member.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.commutebot.global.exception.ExceptionType.*;
import static com.commutebot.member.domain.Role.*;
import static com.commutebot.member.dto.MemberRequestDto.*;
import static com.commutebot.member.dto.MemberResponseDto.memberDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationBuilder authenticationBuilder;


    @Override
    @Transactional
    public JwtTokenDto login(Login loginRequestDto) {
        Member member = memberRepository.findMemberToUsername(loginRequestDto.getUsername());
        if (member == null) throw new CustomException(NOT_EXIST_DATA);

        Authentication authentication = authenticationBuilder.getAuthenticationForLogin(loginRequestDto); // 권한 객체 생성
        return jwtTokenProvider.generateToken(authentication, member.getTeamCode()); // 권한 정보와 팀 코드가 담긴 토근 생성
    }


    @Override
    @Transactional
    public MemberResponseDto signup(SignUp signUpRequestDto) {

        duplicateCheck(signUpRequestDto);

        Role userRole = ROLE_COMMON;
        if (teamRepository.teamCodeConverterToLeader(signUpRequestDto.getTeamCode()).equals("-")) userRole = ROLE_LEADER; // 팀에 팀장이 없으면 강제 부여

        String password = passwordEncoder.encode(signUpRequestDto.getPassword());

        Member member = new Member();
        member.makeMemberForSignUp(signUpRequestDto, userRole, password); // 회원가입 멤버 객체 생성

        memberRepository.store(member);

        return memberDto(member);
    }

    private void duplicateCheck(SignUp signUpRequestDto){
        if (memberRepository.usernameDuplicateCheck(signUpRequestDto.getUsername())) throw new CustomException(ALREADY_EXIST_USERNAME);
        else if (memberRepository.emailDuplicateCheck(signUpRequestDto.getEmail())) throw new CustomException(ALREADY_EXIST_EMAIL);
    }

    @Override
    @Transactional
    public void withdrawal(String username) {
        if (!memberRepository.usernameDuplicateCheck(username)) throw new CustomException(NOT_EXIST_DATA);
        memberRepository.exit(username);
    }


    @Override
    @Transactional
    public MemberResponseDto changeLeader(String username, String targetName) {
        try {
            Team team = teamRepository.findTeam(username);

            Member existingLeader = memberRepository.findMemberToUsername(username);
            Member targetMember = memberRepository.findMemberToUsername(targetName);

            existingLeader.changeMemberRole(ROLE_COMMON); // 권한 변경
            targetMember.changeMemberRole(ROLE_LEADER);
            team.changeLeader(targetName); // 팀 정보 변경

            return memberDto(existingLeader);
        } catch (NullPointerException e) {
            throw new CustomException(BAD_REQUEST);
        }
    }


    @Override
    @Transactional
    public MemberResponseDto updateInfo(String username, UpdateDetailInfo updateDetailInfo) {
        try {
            String leaderName = teamRepository.teamCodeConverterToLeader(updateDetailInfo.getTeamCode());

            Member member = memberRepository.findMemberToUsername(username);
            member.updateMemberDetailInfo(updateDetailInfo); // 상세 정보 변경

            if (leaderName.equals("-")) member.changeMemberRole(ROLE_LEADER);
            else member.changeMemberRole(ROLE_COMMON); // 팀에 팀장이 없다면 팀장으로 권한 변경

            return memberDto(member);

        } catch (NullPointerException e) {
            throw new CustomException(BAD_REQUEST);
        }
    }
}
