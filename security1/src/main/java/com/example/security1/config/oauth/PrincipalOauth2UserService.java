package com.example.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    /**
     * Google 인증 후 후처리 함수
     * */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // registrationId 사용하여 어떤 사이트에서 왔는지 확인 가능 (ex google)
        System.out.println("getClientRegistration: " + userRequest.getClientRegistration());
        // 해당 사이트의 Access Token
        System.out.println("getTokenValue: " + userRequest.getAccessToken().getTokenValue());
        // userRequest 정보 -> loadUser() 호출 -> 회원 프로필 정보
        System.out.println("getAttributes: " + super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);


        return super.loadUser(userRequest);
    }
}
