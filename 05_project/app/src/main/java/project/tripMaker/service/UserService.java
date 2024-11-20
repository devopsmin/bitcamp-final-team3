package project.tripMaker.service;

import java.util.List;
import java.util.Map;
import project.tripMaker.vo.User;

public interface UserService {

  void addAdmin(User user) throws Exception;

  void add(User user) throws Exception;

  User addSocialUser(String email, String nickname, String snsName) throws Exception;

  List<User> list(Map<String, Object> options) throws Exception;

  User get(Long userNo) throws Exception;

  User exists(String userEmail, String userPassword) throws Exception;

  User findByTel(String userTel) throws Exception;

  boolean update(User user) throws Exception;

  boolean delete(Long userNo) throws Exception;

  User getByEmail(String userEmail) throws Exception;

  boolean existsByEmail(String userEmail) throws Exception;

  boolean existsByNickname(String userNickname) throws Exception;

  void updateLastLogin(Long userNo) throws Exception;

  boolean realDelete(Long userNo, User user) throws Exception;

  int countAll(Map<String, Object> options) throws Exception;

  Long getUserNoByEmail(String userEmail) throws Exception;
}
