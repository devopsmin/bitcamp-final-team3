package project.tripMaker.service;

import project.tripMaker.vo.User;

import java.util.List;

public interface UserService {
  String login(String email, String password) throws Exception;

  Long signup(User user) throws Exception;

  boolean checkEmailExists(String email) throws Exception;

  void add(User user) throws Exception;

  List<User> list() throws Exception;

  User findBy(long userNo) throws Exception;

  User findByEmailAndPassword(String email, String password) throws Exception;

  boolean update(User user) throws Exception;

  boolean delete(long userNo) throws Exception;
}
