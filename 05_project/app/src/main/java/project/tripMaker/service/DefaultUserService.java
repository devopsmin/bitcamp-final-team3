package project.tripMaker.service;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.dao.UserDao;
import project.tripMaker.vo.User;
import project.tripMaker.user.UserRole;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DefaultUserService implements UserService {

  private final UserDao userDao;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public void add(User user) throws Exception {
    if (userDao.existsByEmail(user.getUserEmail())) {
      throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
    }

    if (user.getUserTel() != null && !user.getUserTel().trim().isEmpty()) {
      User existingUser = userDao.findByTel(user.getUserTel());
      if (existingUser != null) {
        throw new IllegalArgumentException("이미 존재하는 연락처입니다.");
      }
    }

    user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
    user.setUserRole(UserRole.ROLE_USER);
    userDao.insert(user);
  }

  @Transactional
  public void addAdmin(User user) throws Exception {
    if (userDao.existsByEmail(user.getUserEmail())) {
      throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
    }

    if (user.getUserTel() != null && !user.getUserTel().trim().isEmpty()) {
      User existingUser = userDao.findByTel(user.getUserTel());
      if (existingUser != null) {
        throw new IllegalArgumentException("이미 존재하는 연락처입니다.");
      }
    }

    user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
    user.setUserRole(UserRole.ROLE_ADMIN);
    userDao.adminInsert(user);
  }

  @Transactional
  public User addSocialUser(String email, String nickname, String provider) throws Exception {
    if (email == null || email.isEmpty()) {
      throw new IllegalArgumentException("이메일이 존재하지 않습니다.");
    }

    if (userDao.existsByEmail(email)) {
      throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
    }


    Integer snsNo = userDao.findSnsNoByName(provider.toLowerCase());
    if (snsNo == null) {
      throw new IllegalArgumentException("지원하지 않는 SNS 제공자입니다: " + provider);
    }

    User user = User.builder()
            .userEmail(email)
            .userNickname(nickname != null ? nickname : "소셜 사용자")
            .userPassword(passwordEncoder.encode("socialLoginPassword"))
            .snsNo(snsNo)
            .userRole(UserRole.ROLE_USER)
            .userTel("소셜로그인")
            .build();

    userDao.insertSocialUser(user);
    return user;
  }

  public List<User> list(Map<String, Object> options) throws Exception {
    return userDao.list(options);
  }

  public int countAll(Map<String, Object> options) throws Exception {
    return userDao.countAll(options);
  }

  public User get(Long userNo) throws Exception {
    return userDao.findBy(userNo);
  }

  public User exists(String userEmail, String userPassword) throws Exception {
    User user = userDao.findByEmail(userEmail);
    if (user != null && passwordEncoder.matches(userPassword, user.getUserPassword())) {
      return user;
    }
    return null;
  }

  @Transactional
  public boolean update(User user) throws Exception {
    User oldUser = userDao.findBy(user.getUserNo());
    if (oldUser != null && user.getUserPassword() == null) {
      user.setUserPassword(oldUser.getUserPassword());
    }
    return userDao.update(user);
  }

  @Transactional
  public boolean delete(Long userNo) throws Exception {
    return userDao.delete(userNo);
  }

  @Override
  public User getByEmail(String userEmail) throws Exception {
    return userDao.findByEmail(userEmail);
  }

  @Override
  public User findByTel(String userTel) throws Exception {
    User user = userDao.findByTel(userTel);
    if (user != null && user.getUserEmail() != null && !user.getUserEmail().trim().isEmpty()) {
      return user;
    }
    return null;
  }

  @Override
  public boolean existsByEmail(String userEmail) throws Exception {
    return userDao.existsByEmail(userEmail);
  }

  @Override
  public boolean existsByNickname(String userNickname) throws Exception {
    return userDao.existsByNickname(userNickname);
  }

  @Override
  @Transactional
  public void updateLastLogin(Long userNo) throws Exception {
    userDao.updateLastLogin(userNo);
  }

  @Transactional
  public boolean realDelete(Long userNo, User user) throws Exception {
    return userDao.realDelete(userNo, user);
  }

  @Override
  public Long getUserNoByEmail(String userEmail) throws Exception {
    return userDao.getUserNoByEmail(userEmail);
  }
}
