package project.tripMaker.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.dao.CompanionApplyDao;
import project.tripMaker.vo.Companionapply;

@Data
@Service
public class CompanionApplyService {

    private final CompanionApplyDao companionApplyDao;

    // 동행 신청 추가
    @Transactional
    public void addApply(Companionapply companionapply) throws Exception {
        companionApplyDao.insertCompanionApply(companionapply);
    }

    // 특정 동행 신청 조회
    public Companionapply findApplyBy(int companionApplyNo) throws Exception {
        return companionApplyDao.selectCompanionApply(companionApplyNo);
    }

    // 동행 신청 수정
    @Transactional
    public boolean updateApply(Companionapply companionapply) throws Exception {
        return companionApplyDao.updateCompanionApply(companionapply);
    }

    // 동행 신청 삭제
    @Transactional
    public void deleteApply(int companionApplyNo) throws Exception {
        companionApplyDao.deleteCompanionApply(companionApplyNo);
    }
}
