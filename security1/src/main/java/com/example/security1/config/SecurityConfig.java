package com.example.security1.config;

import com.example.security1.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Oauth2 프로세스
 * 1. 코드 받기 (인증)
 * 2. 엑세스 토큰 (권한)
 * 3. 사용자 프로필 가져오기
 * 4-1. 정보를 통해서 회원가입 진행
 * 4-2. 추가 정보 입력 (주소, 전화번호 등)
 *
 * */
@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터를 스프링 필터 체인에 등록한다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // @Secured, @PreAuthorize, @PostAuthorize 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    // Return 오브젝트를 IOC 등록
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // csrf 비활성화
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/user/**")
                    .authenticated()
                .antMatchers("/manager/**")
                    .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm") // 권한 없을 경우, 로그인 페이지 이동
                .loginProcessingUrl("/login") // login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/loginForm")
                .userInfoEndpoint() // Tip. (엑세스토큰 + 사용자 프로필 정보)
                .userService(principalOauth2UserService);

    }
}
