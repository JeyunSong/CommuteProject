package com.commutebot.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MemberRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login {
        @NotBlank(message = "이름을 입력해주세요.")
        private String username;
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(username, password);
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUp {
        @NotBlank(message = "사용하실 이름을 입력해주세요.")
        private String username;
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식만 가능합니다.")
        private String email;
        @NotBlank(message = "전화번호를 입력해주세요.")
        private String tel;
        @NotNull(message = "소속을 선택해주세요.")
        private int teamCode;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateDetailInfo {
        @NotBlank(message = "변경하실 전화번호를 입력해주세요.")
        private String tel;
        @NotNull(message = "변경하실 소속을 선택해주세요.")
        private int teamCode;
    }
}
