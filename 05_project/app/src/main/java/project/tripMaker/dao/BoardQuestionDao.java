package project.tripMaker.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.tripMaker.vo.Board;
import project.tripMaker.vo.Trip;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardQuestionDao extends BoardDao{

  // 게시물 목록 조회
  List<Board> list(Map<String, Object> options) throws Exception;

  // 좋아요 순으로 정렬된 목록 조회
  List<Board> listLike(Map<String, Object> options) throws Exception;

  // 즐겨찾기 순으로 정렬된 목록 조회
  List<Board> listFavor(Map<String, Object> options) throws Exception;

  // 조회수 순으로 정렬된 목록 조회
  List<Board> listView(Map<String, Object> options) throws Exception;

  List<Board> searchBoards(Map<String, Object> params) throws Exception;

  // 게시물 등록
  boolean insert(Board board) throws Exception;

  // 게시물 상세 조회
  Board findBy(@Param("boardNo") int boardNo) throws Exception;

  // 게시물 업데이트
  boolean update(Board board) throws Exception;

  // 게시물 삭제
  boolean delete(@Param("boardNo") int boardNo) throws Exception;

  // 게시물 조회수 업데이트
  void updateBoardCount(@Param("boardNo") int boardNo, @Param("boardCount") int boardCount) throws Exception;

  // 전체 게시물 수 조회
  List<Board> listBoard(@Param("boardtypeNo") int boardtypeNo) throws Exception;

  Integer getLikeCount(int boardNo); // 좋아요 수 조회
  boolean isLiked(Map<String, Object> params); // 좋아요 여부 확인
  void addLike(Map<String, Object> params); // 좋아요 추가
  void removeLike(Map<String, Object> params); // 좋아요 삭제

  Integer getFavorCount(int boardNo); // 좋아요 수 조회
  boolean isFavored(Map<String, Object> params); // 좋아요 여부 확인
  void addFavor(Map<String, Object> params); // 좋아요 추가
  void removeFavor(Map<String, Object> params); // 좋아요 삭제

  List<Trip> findTripsByBoardNo(int boardNo);

  int searchBoardCount(@Param("search") String search) throws Exception;

  List<Board> listUser(Long userNo) throws Exception;
}
