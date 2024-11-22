package project.tripMaker.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
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
import project.tripMaker.service.BenService;
import project.tripMaker.service.CustomOAuth2UserService;
import project.tripMaker.service.UserService;
import project.tripMaker.user.OAuth2UserInfo;
import project.tripMaker.vo.Ben;
import project.tripMaker.vo.User;

@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

  private final UserService userService;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final BenService benService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
    String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();

    OAuth2UserInfo userInfo = customOAuth2UserService.getOAuth2UserInfo(provider, oauth2User.getAttributes());

    log.info("OAuth2 로그인 성공 - 이메일: {}, 이름: {}, 제공자: {}",
        userInfo.getEmail(), userInfo.getName(), userInfo.getProvider());

    try {
      User user = processOAuth2User(userInfo);

      if (user.getUserBlock() == 1 || user.getUserBlock() == 2) {
        Ben ben = benService.getByUserNo(user.getUserNo());

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(403);

        if (ben != null && ben.getUnbanDate() != null) {
          response.getWriter().write("정지된 계정입니다. 해제일: " +
              ben.getUnbanDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } else {
          response.getWriter().write("영구 정지된 계정입니다.");
        }
        return;
      }


      if ("소셜로그인".equals(user.getUserTel())) {
        HttpSession session = request.getSession();
        session.setAttribute("tempLoginUser", user);

        response.setHeader("Location", "/verify/phone?email=" + user.getUserEmail());
        response.setStatus(302);
        return;
      }

      HttpSession session = request.getSession();
      session.setAttribute("loginUser", user);

      response.setStatus(200);
      response.setContentType("text/plain;charset=UTF-8");
      response.getWriter().write("SUCCESS");

    } catch (Exception e) {
      log.error("소셜 로그인 처리 중 오류 발생", e);

      response.setStatus(500);
      response.getWriter().write("ERROR");
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
