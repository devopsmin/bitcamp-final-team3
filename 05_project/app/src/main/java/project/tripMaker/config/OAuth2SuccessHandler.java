package project.tripMaker.config;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import project.tripMaker.service.CustomOAuth2UserService;
import project.tripMaker.user.GoogleUserInfo;
import project.tripMaker.user.KakaoUserInfo;
import project.tripMaker.user.NaverUserInfo;
import project.tripMaker.user.OAuth2UserInfo;
import project.tripMaker.service.UserService;
import project.tripMaker.vo.User;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

  private final UserService userService;
  private final CustomOAuth2UserService customOAuth2UserService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws ServletException, IOException {

    OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
    String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();

    OAuth2UserInfo userInfo = customOAuth2UserService.getOAuth2UserInfo(provider, oauth2User.getAttributes());

    log.info("OAuth2 로그인 성공 - 이메일: {}, 이름: {}, 제공자: {}",
        userInfo.getEmail(), userInfo.getName(), userInfo.getProvider());

    try {
      User user = processOAuth2User(userInfo);

      if ("소셜로그인".equals(user.getUserTel())) {
        HttpSession session = request.getSession();
        session.setAttribute("tempLoginUser", user);
        response.sendRedirect("/auth/phone-verification?email=" + user.getUserEmail());
        return;
      }

      HttpSession session = request.getSession();
      session.setAttribute("loginUser", user);
      response.sendRedirect("/home");

    } catch (Exception e) {
      log.error("소셜 로그인 처리 중 오류 발생", e);
      throw new ServletException("소셜 로그인 처리 중 오류가 발생했습니다.", e);
    }
  }

  private User processOAuth2User(OAuth2UserInfo userInfo) throws Exception {
    User user = userService.getByEmail(userInfo.getEmail());
    if (user == null) {
      user = userService.addSocialUser(
          userInfo.getEmail(),
          userInfo.getName(),
          userInfo.getProvider()
      );
      log.info("새 사용자 생성: {}", user);
    } else {
      log.info("기존 사용자 로그인: {}", user);
    }
    return user;
  }
}
