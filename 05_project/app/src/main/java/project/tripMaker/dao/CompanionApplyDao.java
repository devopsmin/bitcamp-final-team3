package project.tripMaker.dao;

import org.apache.ibatis.annotations.Mapper;
import project.tripMaker.vo.Companionapply;

@Mapper
public interface CompanionApplyDao {

    void insertCompanionApply(Companionapply companionapply) throws Exception;

    Companionapply selectCompanionApply(int companionApplyNo) throws Exception;

    boolean updateCompanionApply(Companionapply companionapply) throws Exception;

    void deleteCompanionApply(int companionApplyNo) throws Exception;
}