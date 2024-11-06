package project.tripMaker.vo;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
// 동행게시판 - form.html 에서 받은 복합객체 처리를 위한 vo 객체 
public class AddRequest {
	private Board board;
	private List<Companionrecruit> companionRecruits;

	public void setBoard(Board board) {
		this.board = board;
	}

	public void setCompanionRecruits(List<Companionrecruit> companionRecruits) {
		this.companionRecruits = companionRecruits;
	}
}
