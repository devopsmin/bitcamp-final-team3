package project.tripMaker.service;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
  private final JavaMailSender javaMailSender;

  @Value("${mail.sender}")
  private String senderEmail;

  private final ConcurrentMap<String, String> verificationCodes = new ConcurrentHashMap<>();
  private final SecureRandom secureRandom = new SecureRandom(); // 암호학적으로 안전한 난수 생성기라고 함 math.random은 예측 가능성이 있어서 이걸로 구현

  public MimeMessage CreateMail(String mail) {
    String code = String.format("%06d", secureRandom.nextInt(900000) + 100000);
    verificationCodes.put(mail, code);

    MimeMessage message = javaMailSender.createMimeMessage();
    try {
      message.setFrom(senderEmail);
      message.setRecipients(MimeMessage.RecipientType.TO, mail);
      message.setSubject("모두의 여행 이메일 인증");
      String body = "";
      body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
      body += "<h1>" + code + "</h1>";
      body += "<h3>" + "감사합니다." + "</h3>";
      message.setText(body, "UTF-8", "html");
    } catch (MessagingException e) {
      e.printStackTrace();
    }
    return message;
  }

  public int sendMail(String mail) {
    MimeMessage message = CreateMail(mail);
    javaMailSender.send(message);
    return Integer.parseInt(verificationCodes.get(mail));
  }
}
