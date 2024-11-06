package project.tripMaker.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import project.tripMaker.service.CustomOAuth2UserService;
import project.tripMaker.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final CustomUserDetailsService customUserDetailsService;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final PasswordEncoder passwordEncoder;
  private final OAuth2SuccessHandler oAuth2SuccessHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/admin/**", "/", "/auth/**", "/oauth2/**", "/verify/**", "/css/**", "/js/**", "/images/**", "/schedule/**", "/user/**", "/home", "/board/**", "/comment/**", "/app/**", "/question/**", "/review/**", "/companion/**").permitAll()
//        .antMatchers("/admin/**").hasRole("ADMIN")
        // .antMatchers("/user/**").hasRole("USER")  // 추후 USER 마이페이지 USER권한만 접근 가능하게 하기 위해서 만들어 놓음
        .anyRequest().authenticated();

    http.formLogin()
        .loginPage("/auth/form")
        .defaultSuccessUrl("/home", true)
        .permitAll();

    http.oauth2Login()
        .loginPage("/auth/form")
        .userInfoEndpoint()
        .userService(customOAuth2UserService)
        .and()
        .successHandler(oAuth2SuccessHandler);

    http.logout()
        .logoutSuccessUrl("/auth/form")
        .permitAll();

    http.csrf().disable();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(customUserDetailsService)
        .passwordEncoder(passwordEncoder);
  }
}
