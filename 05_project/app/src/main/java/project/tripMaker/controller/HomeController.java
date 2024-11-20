package project.tripMaker.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

  @Value("${spring.web.resources.static-locations}")
  private String resourceLocation;

  private static final String DEFAULT_IMAGE =
      "https://dgg7dnk35523.edge.naverncp.com/HZiW9aEJy7/review/default.png?type=u&w=600&h=400";

  @GetMapping("/home")
  public String home(Model model) {
    model.addAttribute("welcome", "<b>안녕</b>하세요!");

    try {
      // resourceLocation에서 이미 file: 포함되어 있으므로 static은 제거
      Path bannerPath = Paths.get(resourceLocation.replace("file:", ""), "images", "banner");

      // 디버깅용 로그
      System.out.println("Resource Location: " + resourceLocation);
      System.out.println("Banner Path: " + bannerPath);
      System.out.println("Directory exists: " + Files.exists(bannerPath));

      if (Files.exists(bannerPath)) {
        List<String> imageFiles = Files.list(bannerPath)
            .filter(file -> {
              String fileName = file.getFileName().toString().toLowerCase();
              return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")
                  || fileName.endsWith(".png") || fileName.endsWith(".gif");
            })
            .sorted()
            .map(path -> {
              System.out.println("Found image: " + path.getFileName());
              return "/images/banner/" + path.getFileName().toString();
            })
            .collect(Collectors.toList());

        System.out.println("Total images found: " + imageFiles.size());
        model.addAttribute("bannerImages", imageFiles);
      }

    } catch (IOException e) {
      System.out.println("Error reading banner directory: " + e.getMessage());
      e.printStackTrace();
    }

    model.addAttribute("defaultImage", DEFAULT_IMAGE);

    return "home";
  }
}