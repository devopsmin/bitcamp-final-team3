package project.tripMaker.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.dao.UserDao;
import project.tripMaker.vo.User;

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

    user.setUserPassword(passwordEncoder.encode(user.getUserPassword())); // 비밀번호 암호화
    userDao.insert(user);
  }

  public List<User> list() throws Exception {
    return userDao.list();
  }

  public User get(long userNo) throws Exception {
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
    // 비밀번호가 변경되었을 경우에만 암호화
    if (user.getUserPassword() != null && !user.getUserPassword().trim().isEmpty()) {
      user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
    }
    return userDao.update(user);
  }

  @Transactional
  public boolean delete(long userNo) throws Exception {
    return userDao.delete(userNo);
  }

  @Override
  public User getByEmail(String userEmail) throws Exception {
    return userDao.findByEmail(userEmail);
  }

  @Override
  public boolean existsByEmail(String userEmail) throws Exception {
    return userDao.existsByEmail(userEmail);
  }

  @Override
  @Transactional
  public void updateLastLogin(long userNo) throws Exception {
    userDao.updateLastLogin(userNo);
  }
}
