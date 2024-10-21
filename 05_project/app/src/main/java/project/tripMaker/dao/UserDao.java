package project.tripMaker.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.tripMaker.vo.User;

@Mapper
public interface UserDao {

  String login(String email, String password) throws Exception;

  Long signup(User user) throws Exception;

  boolean insert(User user) throws Exception;

  List<User> list() throws Exception;

  User findBy(long userNo) throws Exception;

  User findByEmailAndPassword(@Param("userEmail") String email,
      @Param("userPassword") String password) throws Exception;

  boolean existsUsersByEmail(String email) throws Exception;

  boolean update(User user) throws Exception;

  boolean delete(long userNo) throws Exception;

}
