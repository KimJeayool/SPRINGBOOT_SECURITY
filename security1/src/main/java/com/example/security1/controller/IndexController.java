package com.example.security1.controller;

import com.example.security1.config.auth.PrincipalDetails;
import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    /**
     * JPA Repository
     * */
    @Autowired
    private UserRepository userRepository;

    /**
     * 비밀번호 암호화
     * */
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;



    /**
     * 일반 로그인
     * */
    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) { // DI(의존성 주입)
        System.out.println("/test/login=========================");
        // User 정보 가져오는 방법.

        // 첫번째 방법 : Authentication 통해서 다운캐스트하여 가져오는 방법.
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : " + principalDetails.getUser());

        // 두번째 방법 : @AuthenticationPrincipal 사용하여 가져오는 방법.
        System.out.println("userDetails : " + userDetails.getUser());

        return "세션 정보 확인 ";
    }


    /**
     * OAuth 로그인
     * */
    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) { // DI(의존성 주입)
        System.out.println("/test/oauth/login=========================");
        // Oauth User 정보 가져오는 방법.

        // 첫번째 방법 : OAuth2User 통해서 다운캐스트하여 가져오는 방법.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication : " + oAuth2User.getAttributes());

        // 두번째 방법 : @AuthenticationPrincipal 사용하여 가져오는 방법.
        System.out.println("oauth2User : " + oauth.getAttributes());

        return "OAuth 세션 정보 확인 ";
    }



    // localhost:8080
    // localhost:8080/
    @GetMapping({"", "/"})
    public String index() {
        // 머스테치 기본폴더 : src/main/resources
        // 뷰리졸버 설정 : templates (prefix), mustache (suffix) => 생략 가능
        return "index";
    }

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 일반 로그인 & OAuth 로그인 시 PrincipalDetails 얻을 수 있다.
        System.out.println("principalDetails : " + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/loginForm") // SecurityConfig 설정 후 미작동
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        System.out.println(user); // 회원가입 값 확인

        // User 롤 적용
        user.setRole("ROLE_USER");

        // 비밀번호 암호화
        String rawPwd = user.getPassword();
        String encPwd = bCryptPasswordEncoder.encode(rawPwd);
        user.setPassword(encPwd);

        // DB 저장
        userRepository.save(user);

        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN") // @PreAuthorize 보다 최근 나온 문법
    @GetMapping("/info")
    public @ResponseBody String info() {
        return  "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 함수 실행 전에 실행된다.
    @GetMapping("/data")
    public @ResponseBody String data() {
        return  "데이터정보";
    }
}
