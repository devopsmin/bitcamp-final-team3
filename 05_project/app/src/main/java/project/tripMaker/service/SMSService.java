package project.tripMaker.service;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SMSService {

  @Value("${coolsms.api.key}")
  private String apiKey;

  @Value("${coolsms.api.secret}")
  private String apiSecret;

  @Value("${coolsms.sender.number}")
  private String senderNumber;

  private DefaultMessageService messageService;

  private final ConcurrentMap<String, String> verificationCodes = new ConcurrentHashMap<>();
  private final SecureRandom secureRandom = new SecureRandom();

  @PostConstruct
  public void init() {
    this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
  }

  public String sendVerificationMessage(String to) throws Exception {
    String verificationCode = String.format("%06d", secureRandom.nextInt(900000) + 100000);
    verificationCodes.put(to, verificationCode);

    Message message = new Message();
    message.setFrom(senderNumber);
    message.setTo(to);
    message.setText("[모두의 여행] 인증번호는 [" + verificationCode + "] 입니다.");

    try {
      SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));

      if (response.getStatusCode().equals("2000")) {
        return verificationCode;
      } else {
        throw new Exception("SMS 전송 실패: " + response.getStatusCode());
      }
    } catch (Exception e) {
      throw new Exception("SMS 전송 중 오류 발생: " + e.getMessage());
    }
  }

  public boolean verifyCode(String phoneNumber, String code) {
    String savedCode = verificationCodes.get(phoneNumber);
    if (savedCode != null && savedCode.equals(code)) {
      verificationCodes.remove(phoneNumber);
      return true;
    }
    return false;
  }

  public boolean isPhoneVerified(String phoneNumber) {
    return true;
  }
}
