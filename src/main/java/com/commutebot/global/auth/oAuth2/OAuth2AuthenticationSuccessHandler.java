package com.commutebot.global.auth.oAuth2;

import com.commutebot.global.auth.jwt.JwtTokenDto;
import com.commutebot.global.auth.jwt.JwtTokenProvider;
import com.commutebot.global.auth.security.CustomUser;
import com.commutebot.member.domain.Member;
import com.commutebot.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.commutebot.global.exception.CustomException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static com.commutebot.global.exception.ExceptionType.BAD_REQUEST;
import static com.commutebot.member.domain.SignUpChannel.MAIN;


@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // OAuth2UserServiceImpl 이 성공적으로 실행됐다면 SuccessHandler 가 동작하게 됩니다.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

        String email = (String) oAuth2User.getAttributes().get("email");
        Member member = memberRepository.findMemberToEmail(email);
        
        if (member.getSignUpChannel().equals(MAIN)) throw new CustomException(BAD_REQUEST);

        String accessToken = forceLogin(member);

        response.setHeader("AUTHORIZATION_HEADER", "BEARER " + accessToken);
        response.sendRedirect("/api/main/"+ accessToken);
    }

    private String forceLogin(Member member) {
        UserDetails userDetails = createUserDetails(member);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtTokenDto jwtTokenDto = jwtTokenProvider.generateToken(authentication, member.getTeamCode());
        return jwtTokenDto.getAccessToken();
    }

    private UserDetails createUserDetails(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().toString());

        return new User(
                String.valueOf(member.getUsername()),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
