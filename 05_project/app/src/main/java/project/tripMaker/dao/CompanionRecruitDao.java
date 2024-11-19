package project.tripMaker.dao;

import org.apache.ibatis.annotations.Mapper;
import project.tripMaker.vo.Companionrecruit;

import java.util.List;

@Mapper
public interface CompanionRecruitDao {

    void insertCompanionRecruit(Companionrecruit companionrecruit) throws Exception;

    List<Companionrecruit> selectAllRecruits() throws Exception;

    List<Companionrecruit> selectCompanionRecruit(int boardNo) throws Exception;

    List<Companionrecruit> selectCompanionRecruitByScheduleNo(int scheduleNo) throws Exception;

    boolean updateCompanionRecruit(Companionrecruit companionrecruit) throws Exception;

    void deleteCompanionRecruitByBoard(int boardNo) throws Exception;

    void deleteCompanionRecruitByScheduleNo(int scheduleNo) throws Exception;
}