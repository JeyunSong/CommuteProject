package com.commutebot.member.application.impl;

import com.commutebot.global.auth.jwt.AuthenticationBuilder;
import com.commutebot.global.auth.jwt.JwtTokenProvider;
import com.commutebot.global.exception.CustomException;
import com.commutebot.member.domain.Member;
import com.commutebot.member.domain.MemberRepository;
import com.commutebot.member.domain.SignUpChannel;
import com.commutebot.member.domain.TeamRepository;
import com.commutebot.member.dto.MemberRequestDto.SignUp;
import com.commutebot.member.dto.MemberRequestDto.UpdateDetailInfo;
import com.commutebot.member.dto.MemberResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.commutebot.member.domain.Role.ROLE_COMMON;
import static com.commutebot.member.domain.Role.ROLE_LEADER;
import static com.commutebot.member.domain.SignUpChannel.*;
import static com.commutebot.member.dto.MemberRequestDto.Login;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Nested
@DisplayName("회원 관리 테스트")
@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private AuthenticationBuilder authenticationBuilder;
    @Spy
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("로그인")
    void login() {
        // GIVEN
        Member member = new Member("Squirrel", "1234", "acron@gmail.com", "010-1234-5678", 999, ROLE_COMMON, GOOGLE);
        Login request = new Login("Squirrel", "1234");

        // WHEN
        when(memberRepository.findMemberToUsername("Squirrel")).thenReturn(member);
        doReturn(null).when(authenticationBuilder).getAuthenticationForLogin(any());

        memberService.login(request);

        // THEN
        verify(jwtTokenProvider, times(1)).generateToken(any(), anyInt());
    }

    @Test
    @DisplayName("로그인 - NOT_EXIST_MEMBER_INFO")
    void login_2() {
        // GIVEN
        Login request = new Login("Squirrel", "1234");

        // WHEN
        when(memberRepository.findMemberToUsername("Squirrel")).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class, ()-> memberService.login(request));

        // THEN
        assertEquals(exception.getMessage(), "요청한 데이터가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("회원 가입")
    void signup() {
        // GIVEN
        SignUp request = new SignUp("Cat", "1234", "fish@gmail.com", "010-1234-5678", 999);
        String password = passwordEncoder.encode(request.getPassword());
        Member memberInput = new Member();
        memberInput.makeMemberForSignUp(request, ROLE_COMMON, password);

        // WHEN
        when(teamRepository.teamCodeConverterToLeader(request.getTeamCode())).thenReturn("Raccoon");
        MemberResponseDto memberOutput = memberService.signup(request);

        // THEN
        verify(memberRepository, times(1)).store(any());
        assertEquals(memberInput.getRole().toString(), memberOutput.getRole());
    }

    @Test
    @DisplayName("회원 가입 - DUPLICATE_CHECK_USERNAME")
    void signup_2() {
        // GIVEN
        SignUp request = new SignUp("Cat", "1234", "fish@gmail.com", "010-1234-5678", 999);

        // WHEN
        when(memberRepository.usernameDuplicateCheck(request.getUsername())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, ()-> memberService.signup(request));

        // THEN
        assertEquals(exception.getMessage(), "중복되는 이름입니다.");
    }

    @Test
    @DisplayName("회원 가입 - DUPLICATE_CHECK_EMAIL")
    void signup_4() {
        // GIVEN
        SignUp request = new SignUp("Cat", "1234", "fish@gmail.com", "010-1234-5678", 999);

        // WHEN
        when(memberRepository.usernameDuplicateCheck(request.getUsername())).thenReturn(false);
        when(memberRepository.emailDuplicateCheck(request.getEmail())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, ()-> memberService.signup(request));

        // THEN
        assertEquals(exception.getMessage(), "중복되는 이메일입니다.");
    }

    @Test
    @DisplayName("회원 가입 - FORCE_ASSIGN_LEADER")
    void signup_3() {
        SignUp request = new SignUp("Cat", "1234", "fish@gmail.com", "010-1234-5678", 999);

        // WHEN
        when(teamRepository.teamCodeConverterToLeader(request.getTeamCode())).thenReturn("-");
        MemberResponseDto member = memberService.signup(request);

        // THEN
        assertEquals(member.getRole(), "ROLE_LEADER");
    }

    @Test
    @DisplayName("회원 탈퇴")
    void withdrawal() {
        // GIVEN
        String username = "Swallow";

        // WHEN
        when(memberRepository.usernameDuplicateCheck(username)).thenReturn(true);
        memberService.withdrawal(username);

        // THEN
        verify(memberRepository, times(1)).exit(username);
    }

    @Test
    @DisplayName("회원 탈퇴 - NOT_EXIST_MEMBER_INFO")
    void withdrawal_2() {
        // GIVEN
        String username = "Swallow";

        // WHEN
        when(memberRepository.usernameDuplicateCheck(username)).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, ()-> memberService.withdrawal(username));

        // THEN
        assertEquals(exception.getMessage(), "요청한 데이터가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("팀장 변경")
    void changeLeader() {
        // GIVEN
        String existingLeaderName = "Tiger";
        String newLeaderName = "Fox";

        Member existingLeader = new Member(existingLeaderName, "1234", "tiger@gmail.com", "010-1234-5678", 999, ROLE_LEADER, GOOGLE);
        Member newLeader = new Member(newLeaderName, "1234", "fox@gmail.com", "010-1234-5678", 999, ROLE_COMMON, GOOGLE);

        // WHEN
        when(memberRepository.findMemberToUsername("Tiger")).thenReturn(existingLeader);
        when(memberRepository.findMemberToUsername("Fox")).thenReturn(newLeader);

        MemberResponseDto beforeLeader = memberService.changeLeader(existingLeaderName, newLeaderName);

        // THEN
        assertEquals(beforeLeader.getRole(), "ROLE_COMMON");
    }

    @Test
    @DisplayName("팀장 변경 - UNAUTHORIZED_REQUEST")
    void changeLeader_2() {
        // GIVEN
        String existingLeaderName = "Tiger";
        Member existingLeader = new Member(existingLeaderName, "1234", "tiger@gmail.com", "010-1234-5678", 999, ROLE_COMMON, GOOGLE);

        // WHEN
        when(memberRepository.findMemberToUsername(existingLeaderName)).thenReturn(existingLeader);

        CustomException exception = assertThrows(CustomException.class, ()-> memberService.changeLeader(existingLeaderName, "Fox"));

        // THEN
        assertEquals(exception.getMessage(), "접근 권한이 없습니다.");
    }

    @Test
    @DisplayName("상세 정보 변경")
    void updateInfo() {
        // GIVEN
        String username = "Bat";
        Member member = new Member(username, "1234", "bat@gmail.com", "-", 0, ROLE_COMMON, GOOGLE);
        UpdateDetailInfo updateDetailInfo = new UpdateDetailInfo("010-1234-5678", 999);

        // WHEN
        when(teamRepository.existCheck(updateDetailInfo.getTeamCode())).thenReturn(true);
        when(memberRepository.findMemberToUsername(username)).thenReturn(member);
        when(teamRepository.teamCodeConverterToLeader(updateDetailInfo.getTeamCode())).thenReturn("Hamster");

        MemberResponseDto changedMember = memberService.updateInfo(username, updateDetailInfo);

        // THEN
        assertEquals(changedMember.getTel(), "010-1234-5678");
        assertEquals(changedMember.getTeamCode(), 999);
    }

    @Test
    @DisplayName("상세 정보 변경 - NOT_EXIST_TEAM")
    void updateInfo_2() {
        // GIVEN
        String username = "Bat";
        UpdateDetailInfo updateDetailInfo = new UpdateDetailInfo("010-1234-5678", 999);

        // WHEN
        when(teamRepository.existCheck(updateDetailInfo.getTeamCode())).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, ()-> memberService.updateInfo(username, updateDetailInfo));

        // THEN
        assertEquals(exception.getMessage(), "요청한 데이터가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("상세 정보 변경 - NOT_EXIST_LEADER")
    void updateInfo_3() {
        // GIVEN
        String username = "Bat";

        Member member = new Member(username, "1234", "bat@gmail.com", "-", 0, ROLE_COMMON, GOOGLE);

        UpdateDetailInfo updateDetailInfo = new UpdateDetailInfo("010-1234-5678", 999);

        // WHEN
        when(teamRepository.existCheck(updateDetailInfo.getTeamCode())).thenReturn(true);
        when(memberRepository.findMemberToUsername(username)).thenReturn(member);
        when(teamRepository.teamCodeConverterToLeader(updateDetailInfo.getTeamCode())).thenReturn("-");

        MemberResponseDto changedMember = memberService.updateInfo(username, updateDetailInfo);

        // THEN
        assertEquals(changedMember.getRole(), "ROLE_LEADER");
    }
}
