package project.tripMaker.dao;


import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.tripMaker.vo.User;

@Mapper
public interface UserDao {

  boolean insert(User user) throws Exception;

  List<User> list() throws Exception;

  User findBy(int userNo) throws Exception;

  User findByEmailAndPassword(@Param("userEmail") String userEmail, @Param("userPassword") String userPassword) throws Exception;

  boolean update(User user) throws Exception;

  boolean delete(int userNo) throws Exception;

}
