package project.tripMaker.vo;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Ben {
  private Long userNo;
  private Integer bentypeNo;
  private String benDesc;
  private LocalDateTime benDate;
  private LocalDateTime unbanDate;
}
