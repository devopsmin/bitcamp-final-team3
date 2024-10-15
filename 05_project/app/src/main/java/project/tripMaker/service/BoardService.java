package project.tripMaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.vo.Board;
import project.tripMaker.dao.BoardDao;

import java.util.List;

@Service
public class BoardService {

  @Autowired
  BoardDao boardDao;

  public List<Board> list() throws Exception {
    return boardDao.list();
  }

  @Transactional
  public void add(Board board) throws Exception {
    boardDao.insert(board);
  }
}
