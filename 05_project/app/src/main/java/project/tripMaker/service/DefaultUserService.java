package project.tripMaker.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.dao.UserDao;
import project.tripMaker.vo.User;

@RequiredArgsConstructor
@Service
public class DefaultUserService implements UserService {

  private final UserDao userDao;

  @Transactional
  public void add(User user) throws Exception {
    userDao.insert(user);
  }

  public List<User> list() throws Exception {
    return userDao.list();
  }

  public User get(int userNo) throws Exception {
    return userDao.findBy(userNo);
  }

  public User exists(String userEmail, String userPassword) throws Exception {
    return userDao.findByEmailAndPassword(userEmail, userPassword);
  }

  @Transactional
  public boolean update(User user) throws Exception {
    return userDao.update(user);
  }

  @Transactional
  public boolean delete(int userNo) throws Exception {
    return userDao.delete(userNo);
  }
}
