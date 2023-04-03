package com.commutebot.global.auth.oAuth2;

import com.commutebot.global.exception.CustomException;
import com.commutebot.global.exception.ExceptionType;
import com.commutebot.member.domain.Member;
import com.commutebot.member.domain.MemberRepository;
import com.commutebot.member.domain.Role;
import com.commutebot.member.domain.SignUpChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static com.commutebot.global.exception.ExceptionType.*;
import static com.commutebot.member.domain.SignUpChannel.GOOGLE;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 참고 : 이 프로젝트에서는 Google 소셜 로그인만 지원합니다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // AccessToken 을 이용하여 서드파티 서버로부터 유저의 정보를 받아옵니다.
        // ( userRequest 에 서드파티 서버에서 발행한 AccessToken 이 담겨져 있습니다. )
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);


        // 이후 받아온 정보로 회원가입/로그인을 진행합니다.
        try {
            managingOAuth2UserRecord(oAuth2User);
        } catch (IOException | ServletException e) {
            throw new CustomException(BAD_REQUEST);
        }
        return oAuth2User;
    }

    public void managingOAuth2UserRecord(OAuth2User oAuth2User) throws IOException, ServletException {
        String email = (String) oAuth2User.getAttributes().get("email");

        int index = email.indexOf('@');
        String username = email.substring(0, index);

        registerOAuth2UserIfNeeded(email, username);
    }

    private void registerOAuth2UserIfNeeded(String email, String username) {

        // 구글 로그인 기록이 이미 존재합니다.
       if (memberRepository.emailDuplicateCheck(email)) return;

        String modifiedUsername = makeModifiedUsername(username);
        String temporaryPassword = makeTemporaryPassword();

        Member member = new Member();
        member.makeMemberForSignUpGoogle(modifiedUsername, passwordEncoder.encode(temporaryPassword), email);

        memberRepository.store(member);
    }

    // before : "so"
    // return : "so4321"
    private String makeModifiedUsername(String username){

        if (memberRepository.usernameDuplicateCheck(username)) {
            username += (int) (Math.random() * 1000);
        }
        return username;
    }

    // Create Random Password
    // return : (ex) "1a2b3c4d5e"
    private String makeTemporaryPassword(){
        String password = UUID.randomUUID().toString().replaceAll("-", "");
        return password.substring(0, 10);
    }
}
