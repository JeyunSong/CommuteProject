package com.commutebot.member.domain;

import com.commutebot.member.dto.MemberRequestDto.SignUp;
import com.commutebot.member.dto.MemberRequestDto.UpdateDetailInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.commutebot.member.domain.SignUpChannel.GOOGLE;
import static com.commutebot.member.domain.SignUpChannel.MAIN;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pk;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String tel;

    @Column(nullable = false)
    private int teamCode;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SignUpChannel signUpChannel;

    public Member(String username, String password, String email, String tel, int teamCode, Role role, SignUpChannel signUpChannel) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.tel = tel;
        this.teamCode = teamCode;
        this.role = role;
        this.signUpChannel = signUpChannel;
    }

    public void makeMemberForSignUp(SignUp signUpRequestDto, Role role, String password){
        this.username = signUpRequestDto.getUsername();
        this.password = password;
        this.email = signUpRequestDto.getEmail();
        this.tel = signUpRequestDto.getTel();
        this.teamCode = signUpRequestDto.getTeamCode();
        this.role = role;
        this.signUpChannel = MAIN;
    }

    public void makeMemberForSignUpGoogle(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
        this.tel = "";
        this.teamCode = 0;
        this.role = Role.ROLE_COMMON;
        this.signUpChannel = GOOGLE;
    }

    public void updateMemberDetailInfo(UpdateDetailInfo updateDetailInfo){
        this.tel = updateDetailInfo.getTel();
        this.teamCode = updateDetailInfo.getTeamCode();
    }

    public void changeMemberRole(Role role){
        this.role = role;
    }
}
