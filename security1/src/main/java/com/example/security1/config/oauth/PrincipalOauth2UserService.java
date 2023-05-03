package com.example.security1.config.oauth;

import com.example.security1.config.auth.PrincipalDetails;
import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder; // 암호화

    @Autowired
    private UserRepository userRepository;

    /**
     * Google 인증 후 후처리 함수
     * */
    // 함수 종료 시 @AuthenticationPrincipal 어노테이션 생성.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // registrationId 사용하여 어떤 사이트에서 왔는지 확인 가능 (ex google)
        System.out.println("getClientRegistration: " + userRequest.getClientRegistration());
        // 해당 사이트의 Access Token
        System.out.println("getTokenValue: " + userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        // userRequest 정보 -> loadUser() 호출 -> 회원 프로필 정보
        System.out.println("getAttributes: " + oAuth2User.getAttributes());

        // 회원가입 진행
        String provider = userRequest.getClientRegistration().getClientId();      // Google
        String providerId = oAuth2User.getAttribute("sub");                 // Google Sub ID
        String username = provider + "_" + providerId;                            // name (임시)
        String password = bCryptPasswordEncoder.encode("GetPassword"); // password (임시)
        String email = oAuth2User.getAttribute("email");                    // Email
        String role = "ROLE_USER";                                                // ROLE

        User userEntity = userRepository.findByUsername(username);

        // 계정이 존재하지 않음 -> 회원가입 진행.
        if (userEntity == null) {
            System.out.println("구글 로그인이 최초입니다.");
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        } else {
            System.out.println("구글 로그인을 이미 한 적이 있습니다.");
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
