package project.tripMaker.service;

import java.util.List;
import project.tripMaker.vo.User;

public interface UserService {

  void addAdmin(User user) throws Exception;

  void add(User user) throws Exception;

  User addSocialUser(String email, String nickname, String snsName) throws Exception;

  List<User> list() throws Exception;

  User get(Long userNo) throws Exception;

  User exists(String userEmail, String userPassword) throws Exception;

  User findByTel(String userTel) throws Exception;

  boolean update(User user) throws Exception;

  boolean delete(Long userNo) throws Exception;

  User getByEmail(String userEmail) throws Exception;

  boolean existsByEmail(String userEmail) throws Exception;

  boolean existsByNickname(String userNickname) throws Exception;

  void updateLastLogin(Long userNo) throws Exception;
}
