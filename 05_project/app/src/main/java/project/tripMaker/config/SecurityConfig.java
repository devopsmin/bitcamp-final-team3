package project.tripMaker.config;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import project.tripMaker.service.BenService;
import project.tripMaker.service.CustomOAuth2UserService;
import project.tripMaker.service.CustomUserDetailsService;
import project.tripMaker.service.UserService;
import project.tripMaker.user.UserRole;
import project.tripMaker.vo.Ben;
import project.tripMaker.vo.User;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomUserDetailsService customUserDetailsService;
  private final CustomOAuth2UserService customOAuth2UserService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, UserService userService, BenService benService) throws Exception {
    http
        .authorizeRequests(authorize -> authorize
            .antMatchers("/admin/**", "/auth/register/admin").hasAuthority("ROLE_ADMIN")
            .antMatchers("/", "/auth/**", "/oauth2/**", "/verify/**", "/css/**", "/js/**", "/images/**", "/schedule/**", "/user/**", "/home", "/board/**", "/comment/**", "/app/**", "/question/**", "/review/**", "/companion/**", "/mypage/**", "/notifications/**").permitAll()
            //            .antMatchers("/user/**").hasRole("USER")
            .anyRequest().authenticated()
        )
        .formLogin(login -> login
            .loginPage("/auth/form")
            .loginProcessingUrl("/auth/login")
            .usernameParameter("userEmail")
            .passwordParameter("userPassword")
            .successHandler((request, response, authentication) ->
                onAuthenticationSuccess(request, response, authentication, userService))
            .failureHandler((request, response, exception) ->
                onAuthenticationFailure(request, response, exception, userService, benService))
            .permitAll()
        )
        .oauth2Login(oauth2 -> oauth2
            .loginPage("/auth/form")
            .userInfoEndpoint(userInfo -> userInfo
                .userService(customOAuth2UserService)
            )
            .successHandler(new OAuth2SuccessHandler(userService, customOAuth2UserService, benService))
        )
        .logout(logout -> logout
            .logoutSuccessUrl("/auth/form")
            .permitAll()
        )
        .csrf(csrf -> csrf.disable());

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(customUserDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    authProvider.setHideUserNotFoundExceptions(false);
    return authProvider;
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication,
      UserService userService) throws IOException {

    String email = authentication.getName();
    try {
      User user = userService.getByEmail(email);
      if (user != null) {
        HttpSession session = request.getSession();
        session.setAttribute("loginUser", user);

        boolean saveEmail = "true".equals(request.getParameter("saveEmail"));
        Cookie cookie = new Cookie("userEmail", saveEmail ? email : "");
        cookie.setPath("/");

        if (saveEmail) {
          cookie.setMaxAge(60 * 60 * 24 * 7);
        } else {
          cookie.setMaxAge(0);
        }
        response.addCookie(cookie);

        if (user.getUserRole() == UserRole.ROLE_ADMIN) {
          response.sendRedirect("/home");
        } else {
          // 현재 위치 유지: sendRedirect 대신 작업 종료
          response.getWriter().write("Authentication successful!"); // 메시지 반환
          response.getWriter().flush();
        }
      } else {
        throw new Exception("유저를 찾을 수 없습니다.");
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void onAuthenticationFailure(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException exception,
      UserService userService,
      BenService benService) throws IOException {

    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    String errorMessage;
    try {
      if (exception instanceof DisabledException
          || exception.getCause() instanceof DisabledException) {
        User user = userService.getByEmail(request.getParameter("userEmail"));
        Ben ben = benService.getByUserNo(user.getUserNo());

        if (ben != null && ben.getUnbanDate() != null) {
          errorMessage = String.format("정지된 계정! 해제일은 : %s",
              ben.getUnbanDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))+ " 입니다");
        } else {
          errorMessage = "영구 정지된 계정입니다.";
        }
      } else {
        errorMessage = "아이디 또는 비밀번호가 올바르지 않습니다.";
      }

      String jsonResponse = String.format("{\"error\":\"%s\"}",
          errorMessage.replace("\"", "\\\""));

      response.getWriter().print(jsonResponse);
      response.getWriter().flush();

    } catch (Exception e) {
      response.getWriter().print("{\"error\":\"처리 중 오류가 발생했습니다.\"}");
      response.getWriter().flush();
    }
  }
}
