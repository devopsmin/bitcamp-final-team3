package project.tripMaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import project.tripMaker.vo.Board;
import project.tripMaker.dao.BoardDao;

import java.util.List;

public interface BoardService {

  void add(Board board) throws Exception;

  List<Board> list() throws Exception;

  void increaseViewCount(int boardNo) throws Exception;

}
