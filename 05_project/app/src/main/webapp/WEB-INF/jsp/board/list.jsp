<%@ page
    language="java" 
    contentType="text/html;charset=UTF-8" 
    pageEncoding="UTF-8"
    trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!-- Header 자리 -->
<!DOCTYPE html>
<html>
<head>
    <meta charset='UTF-8'>
    <title>Title</title>
</head>
<body>
<!-- Header 자리여기까지 -->

<h1>게시글 목록</h1>
<label>정렬</label>
<select name="sort" onchange="changeSort(this.value)">
    <option value="latest" ${param.sort == 'latest' || param.sort == null ? 'selected' : ''}>최신순</option>
    <option value="likes" ${param.sort == 'likes' ? 'selected' : ''}>좋아요</option>
    <option value="favorites" ${param.sort == 'favorites' ? 'selected' : ''}>즐겨찾기</option>
    <option value="views" ${param.sort == 'views' ? 'selected' : ''}>조회수</option>
</select>

<script>
    function changeSort(sortType){
      window.location.href = 'list?sort=' + sortType;
    }
</script>


<p><a href='form'>글쓰기</a></p>

<table>
  <thead>
    <tr><th>번호</th><th>제목</th>
        <th>작성자번호</th><th>작성자닉네임</th><th>작성일</th>
        <th>좋아요</th><th>즐겨찾기</th>
        <th>조회수</th><th>태그</th></tr>
  </thead>
  <tbody>
<c:forEach items="${list}" var="board">
    <tr>
        <td>${board.boardNo}</td>
        <td><a href='view?boardNo=${board.boardNo}'>${board.boardTitle}</a></td>
        <td>${board.writer.userNo}</td>
        <td>${board.writer.userNickname}</td>
        <td>${board.boardCreatedDate}</td>
        <td>${board.boardLike}</td>
        <td>${board.boardFavor}</td>
        <td>${board.boardCount}</td>
        <td>${board.boardTag}</td>
    </tr>
</c:forEach>
  </tbody>
</table>

[
<c:if test="${pageNo > 1}">
    <a href="list?pageNo=${pageNo - 1}&pageSize=${pageSize}">이전<a>
</c:if>
<c:if test="${pageNo <= 1}">
    이전
</c:if>
]

${pageNo}

[
<c:if test="${pageNo < pageCount}">
    <a href="list?pageNo=${pageNo + 1}&pageSize=${pageSize}">다음<a>
</c:if>
<c:if test="${pageNo >= pageCount}">
    다음
</c:if>
]

</body>
</html>
