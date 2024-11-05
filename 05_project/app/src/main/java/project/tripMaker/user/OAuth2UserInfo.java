package project.tripMaker.user;

public interface OAuth2UserInfo {
  String getProvider();
  String getEmail();
  String getName();
}
