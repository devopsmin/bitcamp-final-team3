package project.tripMaker.user;

import lombok.Getter;
import lombok.Setter;
import project.tripMaker.vo.User;

@Getter
@Setter
public class SignupForm {
  private String name;
  private String email;
  private String password;

  public User toEntity() {
    User user = new User();
    user.setUserNickname(this.name);
    user.setUserEmail(this.email);
    user.setUserPassword(this.password);
    return user;
  }
}
