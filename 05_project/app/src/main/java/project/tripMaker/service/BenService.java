package project.tripMaker.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.dao.BenDao;
import project.tripMaker.vo.Ben;
import project.tripMaker.vo.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class BenService {

  private final BenDao benDao;
  private final UserService userService;

  @Transactional
  public boolean add(Ben ben) throws Exception {
    Ben existingBen = benDao.findByUserNo(ben.getUserNo());
    if (existingBen != null) {
      return benDao.update(ben);
    } else {
      benDao.insert(ben);
      return true;
    }
  }

  public Ben getByUserNo(Long userNo) throws Exception {
    return benDao.findByUserNo(userNo);
  }

  @Transactional
  public boolean update(Ben ben) throws Exception {
    Ben existingBen = benDao.findByUserNo(ben.getUserNo());
    return benDao.update(ben);
  }

  @Scheduled(cron = "0 0 0 * * *")
  public void checkAndUnbanUsers() {
    try {
      List<Ben> expiredBans = benDao.findExpiredBans(LocalDateTime.now());
      for (Ben ban : expiredBans) {
        try {
          User user = userService.get(ban.getUserNo());
          if (user != null && user.getUserBlock() == 1) {
            user.setUserBlock(0);
            userService.update(user);
            log.info("사용자 {}의 차단이 자동으로 해제되었습니다.", user.getUserNo());
          }
        } catch (Exception e) {
          log.error("사용자 {}의 차단 해제 중 오류가 발생했습니다.", ban.getUserNo(), e);
        }
      }
    } catch (Exception e) {
      log.error("만료된 차단 확인 중 오류가 발생했습니다.", e);
    }
  }
}
