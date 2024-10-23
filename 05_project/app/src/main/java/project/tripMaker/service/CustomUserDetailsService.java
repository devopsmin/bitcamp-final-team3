package project.tripMaker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.tripMaker.dao.UserDao;
import project.tripMaker.vo.User;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserDao userDao;

  @Override
  public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
    try {
      User user = userDao.findByEmail(userEmail);
      if (user == null) {
        throw new UsernameNotFoundException("이메일을 통해 사용자를 찾을 수 없습니다.: " + userEmail);
      }

      return org.springframework.security.core.userdetails.User.builder()
          .username(user.getUserEmail())
          .password(user.getUserPassword())
          .authorities(Collections.singleton(new SimpleGrantedAuthority(user.getUserRole().name())))
          .build();
    } catch (Exception e) {
      throw new UsernameNotFoundException("사용자 로드 중 오류 발생", e);
    }
  }
}
