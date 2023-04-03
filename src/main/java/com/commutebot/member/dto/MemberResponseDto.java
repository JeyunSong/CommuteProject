package com.commutebot.member.dto;


import com.commutebot.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private String username;
    private String email;
    private String tel;
    private int teamCode;
    private String role;
    private String signUpChannel;

    public static MemberResponseDto memberDto(Member member){
        return new MemberResponseDto(member.getUsername(),
                                    member.getEmail(),
                                    member.getTel(),
                                    member.getTeamCode(),
                                    String.valueOf(member.getRole()),
                                    String.valueOf(member.getSignUpChannel()));
    }
}
