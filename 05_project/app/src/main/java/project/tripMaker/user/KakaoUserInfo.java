package project.tripMaker.user;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {
  private Map<String, Object> attributes;
  private Map<String, Object> kakaoAccount;
  private Map<String, Object> profile;

  public KakaoUserInfo(Map<String, Object> attributes) {
    this.attributes = attributes;
    this.kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    this.profile = (Map<String, Object>) kakaoAccount.get("profile");
  }

  @Override
  public String getProvider() {
    return "kakao";
  }

  @Override
  public String getEmail() {
    return (String) kakaoAccount.get("email");
  }

  @Override
  public String getName() {
    return (String) profile.get("nickname");
  }
}
