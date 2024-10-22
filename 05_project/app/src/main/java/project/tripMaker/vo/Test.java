package project.tripMaker.vo;



import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class Test {
  public static void main(String[] args) {
    // java.sql.Date로부터 LocalDate로 변환
    Date startDate = Date.valueOf("2024-01-01");
    Date endDate = Date.valueOf("2024-03-01");

    // java.sql.Date에서 java.time.LocalDate로 변환
    LocalDate start = startDate.toLocalDate();
    LocalDate end = endDate.toLocalDate();

    long days = ChronoUnit.DAYS.between(start, end);

    System.out.println("Start Date: " + start);
    System.out.println("End Date: " + end);
    System.out.println("Days between: " + days);
  }
}
