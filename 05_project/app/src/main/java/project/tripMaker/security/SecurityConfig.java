package project.tripMaker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.tripMaker.jwt.JwtAuthenticationFilter;
import project.tripMaker.jwt.JwtTokenProvider;
import project.tripMaker.user.UserRole; // UserRole을 import 합니다.

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;

  @Autowired
  public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

//  @Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http
//        .csrf().disable() // CSRF 보호 비활성화 (개발 시에만 권장)
//        .authorizeRequests()
//        .antMatchers("/", "/user/signup", "/user/login", "/css/**", "/js/**", "/images/**", "/app/**").permitAll()
//        // 허용된 경로
//        .antMatchers("/admin/**").hasRole(UserRole.ADMIN.name()) // ADMIN 역할을 가진 사용자만 접근 가능
//        .anyRequest().authenticated() // 인증이 필요한 요청
//        .and()
//        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가
//    return http.build();
//  }


}
