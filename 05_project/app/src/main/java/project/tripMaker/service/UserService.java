package project.tripMaker.service;



import java.util.List;
import project.tripMaker.vo.User;

public interface UserService {

  void add(User user) throws Exception;

  List<User> list() throws Exception;

  User get(int userNo) throws Exception;

  User exists(String userEmail, String userPassword) throws Exception;

  boolean update(User user) throws Exception;

  boolean delete(int userNo) throws Exception;
}
