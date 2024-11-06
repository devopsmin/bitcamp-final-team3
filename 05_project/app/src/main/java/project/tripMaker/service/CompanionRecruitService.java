package project.tripMaker.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.dao.CompanionRecruitDao;
import project.tripMaker.vo.Companionrecruit;

import java.util.List;

@Data
@Service
public class CompanionRecruitService {

    private final CompanionRecruitDao companionRecruitDao;

    // 동행 모집 추가
    @Transactional
    public void addRecruit(Companionrecruit companionrecruit) throws Exception {
        companionRecruitDao.insertCompanionRecruit(companionrecruit);
    }

    // 동행 모집 목록 조회
    public List<Companionrecruit> listRecruits() throws Exception {
        return companionRecruitDao.selectAllRecruits();
    }

    // 특정 동행 모집 조회
    public Companionrecruit findRecruitBy(int companionRecruitNo) throws Exception {
        return companionRecruitDao.selectCompanionRecruit(companionRecruitNo);
    }

    // 동행 모집 수정
    @Transactional
    public boolean updateRecruit(Companionrecruit companionrecruit) throws Exception {
        return companionRecruitDao.updateCompanionRecruit(companionrecruit);
    }

    // 동행 모집 삭제
    @Transactional
    public void deleteRecruit(int companionRecruitNo) throws Exception {
        companionRecruitDao.deleteCompanionRecruit(companionRecruitNo);
    }
}
