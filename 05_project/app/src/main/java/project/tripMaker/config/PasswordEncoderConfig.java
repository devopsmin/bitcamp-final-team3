package project.tripMaker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {
  // 소셜 로그인 기능을 구현 했더니 바로 순환 참조 문제가 발생함
  // 순환 참조 문제 해결을 위해서 일단 Class를 따로 분리 했음 SecurityConfig에 다시 넣으면 바로 순환 참조 문제가 발생함....
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
