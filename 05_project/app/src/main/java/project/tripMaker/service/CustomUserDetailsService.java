package project.tripMaker.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.tripMaker.dao.UserDao;
import project.tripMaker.vo.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

  private final UserDao userDao;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    try {
      User user = userDao.findByEmail(email);
      if (user == null) {
        throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email);
      }

      if (user.getUserBlock() == 1 || user.getUserBlock() == 2) {
        throw new DisabledException("차단된 계정입니다.");
      }

      return new org.springframework.security.core.userdetails.User(
          user.getUserEmail(),
          user.getUserPassword(),
          Collections.singletonList(new SimpleGrantedAuthority(user.getUserRole().name()))
      );

    } catch (DisabledException e) {
      throw e;
    } catch (Exception e) {
      throw new UsernameNotFoundException("사용자 로드 중 오류 발생", e);
    }
  }
}
