package project.tripMaker.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import project.tripMaker.dto.ReviewDto;
import project.tripMaker.service.CommentService;
import project.tripMaker.service.ReviewService;
import project.tripMaker.service.ScheduleService;
import project.tripMaker.service.StorageService;
import project.tripMaker.vo.Board;
import project.tripMaker.vo.BoardImage;
import project.tripMaker.vo.Comment;
import project.tripMaker.vo.Schedule;
import project.tripMaker.vo.Trip;
import project.tripMaker.vo.User;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;
  private final CommentService commentService;
  private final ScheduleService scheduleService;

  private final StorageService storageService;

  private String folderName = "review/";

  // ENUM : 리뷰용 게시판 타입
  private static final int BOARD_TYPE_REVIEW = 3;

  @GetMapping("list")
  public String list(
      // 페이지 설정
      // pageNo (페이지번호)      Default 1 = 1페이지
      // pageSize (화면출력 갯수) Default 9 = 9개의 게시글
      @RequestParam(defaultValue = "1") int pageNo,
      @RequestParam(defaultValue = "9") int pageSize,
      Model model,
      @RequestParam(required = false, defaultValue = "latest") String sort,
      HttpSession session
  ) throws Exception {

    User loginUser = (User) session.getAttribute("loginUser");
    boolean isLoggedIn = loginUser != null;

    List<Board> topBoards = reviewService.getTopRecommendedBoards(3,4); // 베스트 게시글 상위 4개 가져오기

    List<Board> boardList;

    // 게시물 목록 조회
    //   1. 최신      list [Default]
    //   2. 좋아요    Like
    //   3. 즐겨찾기  Favor
    //   4. 조회수    View
    switch (sort) {
      case "likes":
        boardList = reviewService.listByLikes(pageNo, pageSize, BOARD_TYPE_REVIEW);
        break;
      case "favorites":
        boardList = reviewService.listByFavorites(pageNo, pageSize, BOARD_TYPE_REVIEW);
        break;
      case "views":
        boardList = reviewService.listByViews(pageNo, pageSize, BOARD_TYPE_REVIEW);
        break;
      default:
        boardList = reviewService.list(pageNo, pageSize, BOARD_TYPE_REVIEW);
        break;
    }

    // 전체 갯수 확인
    // 페이지 처리
    int length = reviewService.countAll(BOARD_TYPE_REVIEW);
    int pageCount = length / pageSize;

    // 페이지 처리 최소 1 이상
    if (pageNo < 1) {
      pageNo = 1;
    }

    // 갯수가 size 초과시 페이지 1칸더
    if (length % pageSize > 0) {
      pageCount++;
    }

    // 페이지 처리 최대 PageCount 까지
    if (pageNo > pageCount) {
      pageNo = pageCount;
    }


    // 베스트 게시물 목록
    for (Board board : topBoards) {
      // 직접 첫번째 이미지 Board객체 추가
      if (board.getBoardImages() != null && !board.getBoardImages().isEmpty()) {
        board.setFirstImageName(board.getBoardImages().get(0).getBoardimageName());
      } else {
        board.setFirstImageName("default.png");
      }
    }

    // 일반 게시물 목록
    for (Board board : boardList) {
      // 직접 첫번째 이미지 Board객체 추가
      if (board.getBoardImages() != null && !board.getBoardImages().isEmpty()) {
        board.setFirstImageName(board.getBoardImages().get(0).getBoardimageName());
      } else {
        board.setFirstImageName("default.png");
      }

      // 댓글 갯수
      int commentCount = reviewService.getCommentCount(board.getBoardNo());
      board.setCommentCount(commentCount); // 댓글 개수 설정
    }

    model.addAttribute("topBoards", topBoards);
    model.addAttribute("sort", sort);
    model.addAttribute("list", boardList);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("pageCount", pageCount);
    model.addAttribute("isLoggedIn", isLoggedIn);
    return "review/list";
  }

  @GetMapping("form")
  public String form(Model model, HttpSession session) throws Exception {
    // 로그인 유저 확인
    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }

    // 로그인 유저의 Trip List 찾아서 List 전달
    List<Trip> tripList = scheduleService.getSchedulesByTripNoExcludeBoard(loginUser.getUserNo());
    model.addAttribute("trips", tripList);

    return "review/form";
  }

  @PostMapping("add")
  public String add(Board board,
      @RequestParam("imageFiles")MultipartFile[] files,
      HttpSession session) throws Exception {
    // 로그인 유저 확인
    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }

    List<BoardImage> attachedFiles = new ArrayList<>();

    for (MultipartFile file : files) {
      if (file.getSize() == 0) {
        continue;
      }

      BoardImage boardImage = new BoardImage();
      boardImage.setBoardimageName(UUID.randomUUID().toString());
      boardImage.setBoardimageDefaultName(file.getOriginalFilename());

      // 첨부 파일을 Object Storage에 올린다.
      HashMap<String, Object> options = new HashMap<>();
      options.put(StorageService.CONTENT_TYPE, file.getContentType());

      storageService.upload(
          folderName + boardImage.getBoardimageName(), // 업로드 파일의 경로(폴더 경로 포함)
          file.getInputStream(), // 업로드 파일 데이터를 읽어 들일 입력스트림
          options
      );

      attachedFiles.add(boardImage);
    }

    board.setBoardImages(attachedFiles);

    int tripNo = board.getTripNo();
    System.out.println(tripNo);

    board.setTripNo(tripNo);
    board.setWriter(loginUser);
    board.setBoardtypeNo(BOARD_TYPE_REVIEW); // 게시판 타입 설정
    reviewService.add(board);
    return "redirect:list";
  }

  @GetMapping("view")
  public String view(
      @RequestParam int boardNo,
      // @SessionAttribute(value = "loginUser") User user,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
      @RequestParam(value = "sort", defaultValue = "registered") String sort,
      Model model,
      HttpSession session) throws Exception {

    // 로그인 유저가 아닌 경우 처리 (나중에 처리해야지)
    // if (user == null) {
    //   model.addAttribute("errorMessage", "로그인을 해야 합니다.");
    //   return "redirect:/home";
    // }

    Board board = reviewService.get(boardNo);
    if (board == null) {
      throw new Exception("게시글이 존재하지 않습니다.");
    }

    // 특정 Board의 Comment, Trip, Schedule 리스트 전달
    List<Comment> commentList;
    if ("likes".equals(sort)) {
      commentList = commentService.listByLikes(boardNo, page, pageSize);
    } else {
      commentList = commentService.listByPage(boardNo, page, pageSize);
    }

    List<Trip> tripList = reviewService.getTripsByBoardNo(board.getTripNo());
    List<Schedule> scheduleList = scheduleService.getSchedulesByTripNo(board.getTripNo());

    int totalComments = commentService.list(boardNo).size();
    int totalPages = (int) Math.ceil((double) totalComments / pageSize);

    User loginUser = (User) session.getAttribute("loginUser");
    boolean isLoggedIn = loginUser != null;

    Map<Integer, Boolean> commentLikedMap = new HashMap<>();
    Map<Integer, Boolean> isUserAuthorizedMap = new HashMap<>();

    if (isLoggedIn) {
      Long sessionUserNo = loginUser.getUserNo();

      for (Comment comment : commentList) {
        int commentLikeCount = commentService.getCommentLikeCount(comment.getCommentNo());
        comment.setCommentLike(commentLikeCount);

        Map<String, Object> params = new HashMap<>();
        params.put("commentNo", comment.getCommentNo());
        params.put("userNo", loginUser.getUserNo());

        boolean isCommentLiked = commentService.isCommentLiked(params);
        commentLikedMap.put(comment.getCommentNo(), isCommentLiked);

        boolean isCommentOwner = sessionUserNo.equals(comment.getWriter().getUserNo()) || loginUser.getUserRole().name().equals("ROLE_ADMIN");
        isUserAuthorizedMap.put(comment.getCommentNo(), isCommentOwner);
      }
    } else {
      // 비로그인 상태에서도 commentLikedMap을 초기화
      for (Comment comment : commentList) {
        int commentLikeCount = commentService.getCommentLikeCount(comment.getCommentNo());
        comment.setCommentLike(commentLikeCount);
        commentLikedMap.put(comment.getCommentNo(), false);
      }
    }

    // 조회수 증가
    reviewService.increaseBoardCount(board.getBoardNo());

    boolean isUserAuthorized = loginUser != null && (loginUser.getUserNo() == board.getUserNo() || loginUser.getUserRole().name().equals("ROLE_ADMIN"));
    // 로그인 유저만 좋아요/즐겨찾기 여부 확인
    boolean isLiked = false;
    boolean isFavored = false;

    if (isLoggedIn) {
      Long userNo = loginUser.getUserNo();
      isLiked = reviewService.isLiked(boardNo, userNo);
      isFavored = reviewService.isFavored(boardNo, userNo);
    }

    // 기본 이미지 설정
    List<BoardImage> images = board.getBoardImages();
    if (images == null || images.isEmpty()) {
      images = new ArrayList<>();
      BoardImage defaultImage = new BoardImage();
      defaultImage.setBoardimageName("default.png"); // 기본 이미지 파일명
      images.add(defaultImage);
    }

    board.setBoardImages(images);

    Map<Integer, List<Schedule>> groupedSchedules = scheduleList.stream()
        .collect(Collectors.groupingBy(Schedule::getScheduleDay));
    model.addAttribute("groupedSchedules", groupedSchedules);



    model.addAttribute("board", board);
    model.addAttribute("commentList", commentList);
    model.addAttribute("trip", tripList);
    model.addAttribute("schedule", scheduleList);
    model.addAttribute("isUserAuthorized", isUserAuthorized);
    model.addAttribute("isLiked", isLiked);
    model.addAttribute("isFavored", isFavored);

    model.addAttribute("commentList", commentList);
    model.addAttribute("commentLikedMap", commentLikedMap);
    model.addAttribute("currentPage", page);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("sort", sort);
    model.addAttribute("isLoggedIn", isLoggedIn);
    model.addAttribute("loginUserNo", isLoggedIn ? loginUser.getUserNo() : null); // 로그인 유저의 번호 추가

    model.addAttribute("isUserAuthorizedMap", isUserAuthorizedMap);
    return "review/view";
  }

  // 삭제
  @GetMapping("delete")
  public String delete(int boardNo) throws Exception {
    Board board = reviewService.get(boardNo);
    if (board == null) {
      throw new Exception("없는 게시글입니다.");
    }

    reviewService.delete(boardNo);
    return "redirect:list";
  }

  // 파일삭제
  @DeleteMapping("file/delete")
  public String deleteFile(
      @RequestParam("fileNo") int fileNo,
      @RequestParam("boardNo") int boardNo,
      HttpSession session) throws Exception {

    User loginUser = (User) session.getAttribute("loginUser");

    if (loginUser == null) {
      throw new Exception("로그인하지 않았습니다.");
    }

    BoardImage attachedFile = reviewService.getAttachedFile(fileNo);
    if (attachedFile == null) {
      throw new Exception("없는 첨부파일입니다.");
    }

    Board board = reviewService.get(boardNo);
    if (board.getWriter().getUserNo() != loginUser.getUserNo()) {
      throw new Exception("삭제 권한이 없습니다.");
    }

    // 파일 삭제
    try {
      storageService.delete(folderName + attachedFile.getBoardimageName());
      reviewService.deleteAttachedFile(fileNo);
    } catch (Exception e) {
      System.out.printf("파일 삭제 실패: %s\n", folderName + attachedFile.getBoardimageName());
    }

    return "redirect:view?boardNo=" + boardNo;
  }

  // 수정 페이지 정보전달
  @PostMapping("update")
  public String update(
      @RequestParam("boardNo") Integer boardNo,
      @RequestParam("title") String title,
      @RequestParam("content") String content,
      @RequestParam(value = "deletedImages", required = false) String deletedImages,
      @RequestParam(value = "imageFiles", required = false) MultipartFile[] files
  ) throws Exception {

    try {
      Board board = reviewService.get(boardNo);
      board.setBoardTitle(title);
      board.setBoardContent(content);

      if (files == null) {
        files = new MultipartFile[0];
      }

      // 기존 이미지를 모두 삭제
      reviewService.deleteAllFiles(boardNo);

      // 삭제된 이미지 파일 처리
      List<BoardImage> existingImages = board.getBoardImages();
      if (deletedImages != null && !deletedImages.isEmpty()) {
        String[] imageIds = deletedImages.split(",");
        for (String imageId : imageIds) {
          int imageIdInt = Integer.parseInt(imageId);
          reviewService.deleteAttachedFile(imageIdInt);

          // existingImages 리스트에서 삭제된 이미지를 제거
          existingImages.removeIf(image -> image.getBoardimageNo() == imageIdInt);
        }
      }

      // 새로 추가된 이미지 파일 처리
      List<BoardImage> attachedFiles = new ArrayList<>();
      if (files.length > 0) {
        for (MultipartFile file : files) {
          if (!file.isEmpty()) { // 파일이 존재하는 경우
            BoardImage boardImage = new BoardImage();
            boardImage.setBoardimageName(UUID.randomUUID().toString());
            boardImage.setBoardimageDefaultName(file.getOriginalFilename());

            // 파일 업로드 서비스 호출
            HashMap<String, Object> options = new HashMap<>();
            options.put(StorageService.CONTENT_TYPE, file.getContentType());
            storageService.upload("review/" + boardImage.getBoardimageName(), file.getInputStream(), options);

            attachedFiles.add(boardImage);
          }
        }
      }

      // 삭제되지 않은 기존 이미지와 새로 추가된 이미지를 결합하여 설정
      attachedFiles.addAll(existingImages); // 기존 이미지는 삭제된 이미지를 제외한 상태
      board.setBoardImages(attachedFiles);  // 결합된 파일들을 게시글에 설정

      reviewService.update(board); // 변경 사항 저장
    } catch (Exception e) {
      e.printStackTrace();
    }

    return "redirect:view?boardNo=" + boardNo;
  }


  // 수정 후 SQL Update 실행
  @PostMapping("modify")
  public String modify(@RequestParam("boardNo") int boardNo, Model model, HttpSession session) throws Exception {

    User loginUser = (User) session.getAttribute("loginUser");

    Board board = reviewService.get(boardNo);

    if (board == null) {
      throw new Exception("없는 게시글입니다.");
    } else if (loginUser.getUserNo() > 10 && board.getWriter().getUserNo() != loginUser.getUserNo()) {
      throw new Exception("변경 권한이 없습니다.");
    }
    List<Trip> tripList = reviewService.getTripsByBoardNo(board.getTripNo());
    List<Schedule> scheduleList = scheduleService.getSchedulesByTripNo(board.getTripNo());



    model.addAttribute("board", board);
    model.addAttribute("trips", tripList);
    model.addAttribute("schedule", scheduleList);
    return "review/modify";
  }

  // 좋아요 실행
  @PostMapping("like/{boardNo}")
  @ResponseBody
  public Map<String, Object> toggleLike(
      @PathVariable int boardNo,
      @SessionAttribute("loginUser") User loginUser) {

    Long userNo = loginUser.getUserNo();
    boolean isLiked = reviewService.toggleLike(boardNo, userNo);
    int likeCount = reviewService.getLikeCount(boardNo);

    Map<String, Object> response = new HashMap<>();
    response.put("isLiked", isLiked);
    response.put("likeCount", likeCount);

    return response;
  }

  // 즐겨찾기 실행
  @PostMapping("favor/{boardNo}")
  @ResponseBody
  public Map<String, Object> toggleFavor(
      @PathVariable int boardNo,
      @SessionAttribute("loginUser") User loginUser) {

    Long userNo = loginUser.getUserNo();
    boolean isFavored = reviewService.toggleFavor(boardNo, userNo);
    int favorCount = reviewService.getFavorCount(boardNo);

    Map<String, Object> response = new HashMap<>();
    response.put("isFavored", isFavored);
    response.put("favorCount", favorCount);

    return response;
  }

  // user_no를 통해 trip_no 리스트를 가져오는 메서드
  @GetMapping("getTripList")
  public String getTripList(@RequestParam("userNo") Long userNo, Model model) {
    List<Integer> tripNos = scheduleService.getTripNosByUserNo(userNo);
    model.addAttribute("tripNos", tripNos);
    return "review/form"; // form에 리스트 표시
  }

  // 특정 trip_no를 통해 schedule 리스트를 가져오는 메서드
  @GetMapping("getScheduleList")
  @ResponseBody
  public List<Schedule> getScheduleList(@RequestParam("tripNo") int tripNo) {
    System.out.println(tripNo);
    return scheduleService.getSchedulesByTripNo(tripNo);
  }

  // 검색
  // 1. 제목   title
  // 2. 작성자 writer
  // 3. 시도   city
  // 4. 태그   tag
  // 5. 테마   themaName
  @GetMapping("search")
  public String search(
      @RequestParam(value = "option", required = false, defaultValue = "title") String option,
      @RequestParam("query") String query,
      @RequestParam(defaultValue = "1") int pageNo,
      @RequestParam(defaultValue = "9") int pageSize,
      Model model
  ) {
    String decodedQuery = URLDecoder.decode(query, StandardCharsets.UTF_8);
    List<Board> searchResults;

    switch (option) {
      case "title":
        searchResults = reviewService.findByTitle(decodedQuery);
        break;
      case "writer":
        searchResults = reviewService.findByWriter(decodedQuery);
        break;
      case "city":
        searchResults = reviewService.findByCity(decodedQuery);
        break;
      case "tag":
        searchResults = reviewService.findByTag(decodedQuery);
        break;
      case "themaName":
        searchResults = reviewService.findByThema(decodedQuery);
        break;
      default:
        searchResults = List.of();
    }

    // 페이지 처리
    int totalResults = searchResults.size();
    int pageCount = (int) Math.ceil((double) totalResults / pageSize);

    // 시작 인덱스와 끝 인덱스 계산
    int startIndex = (pageNo - 1) * pageSize;
    int endIndex = Math.min(startIndex + pageSize, totalResults);

    // 페이지 번호에 따른 검색 결과 목록 슬라이싱
    List<Board> pagedResults = searchResults.subList(startIndex, endIndex);

    model.addAttribute("option", option);
    model.addAttribute("query", decodedQuery);
    model.addAttribute("list", pagedResults);
    model.addAttribute("pageNo", pageNo);  // 기본 페이지 번호
    model.addAttribute("pageSize", pageSize);  // 페이지 크기
    model.addAttribute("pageCount", pageCount);
    return "review/list"; // 게시글 목록을 출력하는 Thymeleaf 템플릿 파일 이름
  }

  // @GetMapping("api/list")
  // public ResponseEntity<List<ReviewDto>> getReviewList(@RequestParam int page) {
  //   try {
  //     List<ReviewDto> reviews = reviewService.getReviews(page);
  //     return new ResponseEntity<>(reviews, HttpStatus.OK);
  //   } catch (Exception e) {
  //     e.printStackTrace();
  //     return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  //   }
  // }

  @GetMapping("api/list")
  public ResponseEntity<List<ReviewDto>> getTopRecommendedBoards(@RequestParam int page) {
    try {
      int limit = 4; // 가져올 상위 게시글 수
      List<Board> topBoards = reviewService.getTopRecommendedBoards(BOARD_TYPE_REVIEW, limit);

      // Board 객체를 ReviewDto로 변환
      List<ReviewDto> topBoardDtos = topBoards.stream().map(board -> {
        LocalDate createdDate = board.getBoardCreatedDate().toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();

        String themaName = board.getTrip().getThema() != null ? board.getTrip().getThema().getThemaName() : "No Thema";

        return ReviewDto.builder()
            .boardNo(board.getBoardNo())
            .boardTitle(board.getBoardTitle())
            .firstImageName(board.getFirstImageName() != null ? board.getFirstImageName() : "default.png")
            .cityName(board.getTrip().getCity().getCityName())
            .stateName(board.getTrip().getCity().getState().getStateName())
            .themaName(themaName)
            .boardLike(board.getBoardLike())
            .boardFavor(board.getBoardFavor())
            .boardCount(board.getBoardCount())
            .writerNickname(board.getWriter().getUserNickname())
            .writerPhoto(board.getWriter().getUserPhoto())
            .boardCreatedDate(createdDate)
            .build();
      }).collect(Collectors.toList());

      return new ResponseEntity<>(topBoardDtos, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
