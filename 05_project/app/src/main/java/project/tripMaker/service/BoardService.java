package project.tripMaker.service;

import project.tripMaker.vo.Board;

public interface BoardService {
    public int countAll(int BOARD_TYPE_REVIEW) throws Exception;

    public void add(Board board) throws Exception;

    public void increaseBoardCount(int boardNo) throws Exception;

    public Board get(int boardNo) throws Exception;

    public void delete(int boardNo) throws Exception;

    public boolean update(Board board) throws Exception;

}
