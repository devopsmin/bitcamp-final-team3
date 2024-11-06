package project.tripMaker.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.tripMaker.vo.Board;
import project.tripMaker.vo.BoardImage;
import project.tripMaker.vo.Trip;

@Mapper
public interface BoardReviewDao extends BoardDao{

  // 게시물 목록 조회
  //   1. 최신      list [Default]
  //   2. 좋아요    Like
  //   3. 즐겨찾기  Favor
  //   4. 조회수    View
  List<Board> list(Map<String, Object> options) throws Exception;
  List<Board> listLike(Map<String, Object> options) throws Exception;
  List<Board> listFavor(Map<String, Object> options) throws Exception;
  List<Board> listView(Map<String, Object> options) throws Exception;

  // 게시글 쓰기
  boolean insert(Board board) throws Exception;

  // 게시글 찾기
  Board findBy(int boardNo) throws Exception;

  // 게시글 삭제
  boolean delete(@Param("boardNo")int boardNo) throws Exception;

  // 게시글 수정
  boolean update(Board board) throws Exception;

  // 조회수 증가
  void updateViewCount(@Param("boardNo")int boardNo, @Param("boardCount")int boardCount) throws Exception;

  // 전체 게시글 카운트
  int countAll(int no) throws Exception;

  // TripList 찾기
  List<Trip> findTripsByBoardNo(int boardNo);

  // 좋아요 관련
  // 1. 갯수
  // 2. 여부확인 (0/1)
  // 3. 추가
  // 4. 삭제
  int getLikeCount(int boardNo);
  boolean isLiked(Map<String, Object> params);
  void addLike(Map<String, Object> params);
  void removeLike(Map<String, Object> params);

  // 즐겨찾기 관련
  // 1. 갯수
  // 2. 여부확인 (0/1)
  // 3. 추가
  // 4. 삭제
  int getFavorCount(int boardNo);
  boolean isFavored(Map<String, Object> params);
  void addFavor(Map<String, Object> params);
  void removeFavor(Map<String, Object> params);

  // 검색용
  // 1. 제목    Title
  // 2. 작성자  Writer(Nickname)
  // 3. 시도    City
  // 4. 태그    Tag
  List<Board> findByTitle(String title);
  List<Board> findByWriter(String writer);
  List<Board> findByCity(String city);
  List<Board> findByTag(String tag);

  // 첨부파일용
  // 1. 첨부
  // 2. 가져오기
  // 3. 삭제
  // 4. 전체삭제
  void insertFiles(Board board) throws Exception;

  BoardImage getFile(int fileNo) throws Exception;

  boolean deleteFile(int fileNo) throws Exception;

  boolean deleteFiles(int boardNo) throws Exception;

  List<BoardImage> findImagesByBoardNo(int boardNo);
}
