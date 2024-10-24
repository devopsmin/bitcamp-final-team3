package project.tripMaker.service;



import java.util.List;
import project.tripMaker.vo.User;

public interface UserService {

  void addAdmin(User user) throws Exception;

  void add(User user) throws Exception;

  List<User> list() throws Exception;

  User get(long userNo) throws Exception;

  User exists(String userEmail, String userPassword) throws Exception;

  boolean update(User user) throws Exception;

  boolean delete(long userNo) throws Exception;

  User getByEmail(String userEmail) throws Exception;

  boolean existsByEmail(String userEmail) throws Exception;

  void updateLastLogin(long userNo) throws Exception;
}
