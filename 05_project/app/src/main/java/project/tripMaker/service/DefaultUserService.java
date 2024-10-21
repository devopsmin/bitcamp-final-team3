package project.tripMaker.service;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.dao.UserDao;
import project.tripMaker.jwt.JwtTokenProvider;
import project.tripMaker.vo.User;

@Service
public class DefaultUserService implements UserService {

  private final UserDao userDao;
  private final PasswordEncoder encoder;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final JwtTokenProvider jwtTokenProvider;

  @Autowired
  public DefaultUserService(UserDao userDao, PasswordEncoder encoder,
      AuthenticationManagerBuilder authenticationManagerBuilder,
      JwtTokenProvider jwtTokenProvider) {
    this.userDao = userDao;
    this.encoder = encoder;
    this.authenticationManagerBuilder = authenticationManagerBuilder;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  public String login(String email, String password) throws Exception{
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

    // 검증
    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    // 검증된 인증 정보로 JWT 토큰 생성
    String token = jwtTokenProvider.generateToken(authentication);

    return token;
  }
  public Long signup(User user) throws Exception {
    String email = user.getUserEmail();
    String password = user.getUserPassword();

    boolean check = checkEmailExists(email);
    if (check) {
      throw new IllegalArgumentException("이미 존재하는 유저입니다.");
    }

    String encPwd = encoder.encode(password);
    user.setUserPassword(encPwd);

    userDao.insert(user);

    return user.getUserNo();
  }

  public boolean checkEmailExists(String email) throws Exception {
    return userDao.existsUsersByEmail(email);
  }

  @Transactional
  @Override
  public void add(User user) throws Exception {
    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
    user.setUserCreatedDate(currentTime);
    user.setUserLastestLogin(currentTime);
    userDao.insert(user);
  }

  @Override
  public List<User> list() throws Exception {
    return userDao.list();
  }

  @Override
  public User findBy(long userNo) throws Exception {
    return userDao.findBy(userNo);
  }

  @Override
  public User findByEmailAndPassword(String email, String password) throws Exception {
    return userDao.findByEmailAndPassword(email, password);
  }

  @Transactional
  @Override
  public boolean update(User user) throws Exception {
    return userDao.update(user);
  }

  @Transactional
  @Override
  public boolean delete(long userNo) throws Exception {
    return userDao.delete(userNo);
  }
}
