package project.tripMaker.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.tripMaker.vo.User;

import java.util.List;

@Mapper
public interface UserDao {

  boolean insert(User user) throws Exception;

  List<User> list() throws Exception;

  User findBy(int userNo) throws Exception;

  User findByEmail(String userEmail) throws Exception;

  User findByEmailAndPassword(@Param("userEmail") String userEmail,
      @Param("userPassword") String userPassword) throws Exception;

  boolean existsByEmail(String userEmail) throws Exception;

  boolean update(User user) throws Exception;

  void updateLastLogin(long userNo) throws Exception;

  boolean delete(int userNo) throws Exception;
}

