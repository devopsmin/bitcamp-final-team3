package project.tripMaker.dao;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.tripMaker.vo.User;

import java.util.List;

@Mapper
public interface UserDao {

  boolean adminInsert(User user) throws Exception;

  boolean insert(User user) throws Exception;

  boolean insertSocialUser(User user) throws Exception;

  List<User> list(Map<String, Object> options) throws Exception;

  User findBy(Long userNo) throws Exception;

  User findByEmail(String userEmail) throws Exception;

  User findByEmailAndPassword(@Param("userEmail") String userEmail,
                              @Param("userPassword") String userPassword) throws Exception;

  User findByTel(String userTel) throws Exception;

  Integer findSnsNoByName(String snsName) throws Exception;

  boolean existsByEmail(String userEmail) throws Exception;

  boolean existsByNickname(String userNickname) throws Exception;

  boolean update(User user) throws Exception;

  void updateLastLogin(Long userNo) throws Exception;

  boolean delete(Long userNo) throws Exception;

  boolean realDelete(Long userNo, User user) throws Exception;

  int countAll(Map<String, Object> options) throws Exception;

  Long getUserNoByEmail(String userEmail) throws Exception;
}
