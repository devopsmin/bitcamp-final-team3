package project.tripMaker.dao;

import org.apache.ibatis.annotations.Mapper;
import project.tripMaker.vo.Companionrecruit;

import java.util.List;

@Mapper
public interface CompanionRecruitDao {

    void insertCompanionRecruit(Companionrecruit companionrecruit) throws Exception;

    List<Companionrecruit> selectAllRecruits() throws Exception;

    Companionrecruit selectCompanionRecruit(int companionRecruitNo) throws Exception;

    boolean updateCompanionRecruit(Companionrecruit companionrecruit) throws Exception;

    void deleteCompanionRecruit(int companionRecruitNo) throws Exception;
}