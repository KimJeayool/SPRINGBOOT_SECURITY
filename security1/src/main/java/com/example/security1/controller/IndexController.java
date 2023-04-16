package com.example.security1.controller;

import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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



    // localhost:8080
    // localhost:8080/
    @GetMapping({"", "/"})
    public String index() {
        // 머스테치 기본폴더 : src/main/resources
        // 뷰리졸버 설정 : templates (prefix), mustache (suffix) => 생략 가능
        return "index";
    }

    @GetMapping("/user")
    public String user() {
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
}
