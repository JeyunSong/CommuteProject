package com.commutebot.global.auth.security;


import com.commutebot.global.auth.jwt.JwtFilter;
import com.commutebot.global.auth.jwt.JwtTokenProvider;
import com.commutebot.global.auth.oAuth2.OAuth2AuthenticationSuccessHandler;
import com.commutebot.global.auth.oAuth2.OAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2UserServiceImpl oAuth2UserServiceImpl;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .headers().frameOptions().disable()

                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/auth/**", "/oauth2/**", "/o/oauth2/**", "/", "/error", "/login/**").permitAll()
                .antMatchers(  "/api/login","/api/signup","/api/main", "/api/main/{token}").permitAll()
                // COMMUTE
                .antMatchers(HttpMethod.GET, "/api/record/*").access("hasRole('ROLE_COMMON') or hasRole('ROLE_LEADER')")
                .antMatchers(HttpMethod.POST, "/api/record/*").access("hasRole('ROLE_COMMON') or hasRole('ROLE_LEADER')")
                // REPORT
                .antMatchers(HttpMethod.GET, "/api/report/*").access("hasRole('ROLE_COMMON') or hasRole('ROLE_LEADER')")
                // ACCESS LEADER
                .antMatchers(HttpMethod.POST, "/api/management/members/role").access("hasRole('ROLE_LEADER')")
                // ACCESS ADMIN :: SCHEDULING
                .antMatchers(HttpMethod.GET, "/api/admin/*").access("hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/admin/*").access("hasRole('ROLE_ADMIN')")

                .anyRequest().authenticated()

                .and()
                .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        http
                .oauth2Login()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .userInfoEndpoint().userService(oAuth2UserServiceImpl);

        return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/h2-console/**", "/favicon.ico")
                .antMatchers("/css/**", "/images/**", "/js/**");
    }
}