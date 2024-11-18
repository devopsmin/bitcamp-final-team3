package project.tripMaker.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import project.tripMaker.config.OAuth2SuccessHandler;
import project.tripMaker.service.CustomOAuth2UserService;
import project.tripMaker.service.CustomUserDetailsService;
import project.tripMaker.service.UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomUserDetailsService customUserDetailsService;
  private final CustomOAuth2UserService customOAuth2UserService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, UserService userService) throws Exception {
    http
        .authorizeRequests(authorize -> authorize
            .antMatchers("/admin/**", "/", "/auth/**", "/oauth2/**", "/verify/**", "/css/**", "/js/**", "/images/**", "/schedule/**", "/user/**", "/home", "/board/**", "/comment/**", "/app/**", "/question/**", "/review/**", "/companion/**", "/mypage/**").permitAll()
            //            .antMatchers("/admin/**").hasRole("ADMIN")
            //            .antMatchers("/user/**").hasRole("USER")
            .anyRequest().authenticated()
        )
        .formLogin(login -> login
            .loginPage("/auth/form")
            .defaultSuccessUrl("/home", true)
            .permitAll()
        )
        .oauth2Login(oauth2 -> oauth2
            .loginPage("/auth/form")
            .userInfoEndpoint(userInfo -> userInfo
                .userService(customOAuth2UserService)
            )
            .successHandler(new OAuth2SuccessHandler(userService, customOAuth2UserService))
        )
        .logout(logout -> logout
            .logoutSuccessUrl("/auth/form")
            .permitAll()
        )
        .csrf(csrf -> csrf.disable());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
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
    return authProvider;
  }
}
