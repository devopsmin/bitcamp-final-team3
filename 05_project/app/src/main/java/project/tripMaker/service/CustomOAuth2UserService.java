package project.tripMaker.service;

import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import project.tripMaker.dao.UserDao;
import project.tripMaker.user.GoogleUserInfo;
import project.tripMaker.user.KakaoUserInfo;
import project.tripMaker.user.NaverUserInfo;
import project.tripMaker.user.OAuth2UserInfo;
import project.tripMaker.vo.User;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserDao userDao;

  @Autowired
  private ObjectProvider<UserService> userServiceProvider;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oauth2User = super.loadUser(userRequest);

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
            .getUserInfoEndpoint().getUserNameAttributeName();

    OAuth2UserInfo userInfo = getOAuth2UserInfo(registrationId, oauth2User.getAttributes());

    String email = userInfo.getEmail();
    String name = userInfo.getName();

    if (email == null || email.isEmpty()) {
      throw new OAuth2AuthenticationException(
              new OAuth2Error("이메일을 찾을 수 없습니다."),
              "이메일을 가져올 수 없습니다.");
    }

    User user;
    try {
      user = userDao.findByEmail(email);
      if (user == null) {
        user = userServiceProvider.getObject().addSocialUser(email, name, registrationId);
      }
    } catch (Exception e) {
      throw new OAuth2AuthenticationException(
              new OAuth2Error("사용자 처리 에러"),
              "사용자 정보 처리 중 오류가 발생했습니다: " + e.getMessage());
    }

    return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(user.getUserRole().name())),
            oauth2User.getAttributes(),
            userNameAttributeName
    );
  }

  public OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
    if ("naver".equals(registrationId)) {
      return new NaverUserInfo((Map<String, Object>) attributes.get("response"));
    } else if ("google".equals(registrationId)) {
      return new GoogleUserInfo(attributes);
    } else if ("kakao".equals(registrationId)) {
      return new KakaoUserInfo(attributes);
    }
    throw new IllegalArgumentException("지원하지 않는 소셜 로그인 타입입니다: " + registrationId);
  }
}
