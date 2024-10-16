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
<p><a href='form'>글쓰기</a></p>

<table>
  <thead>
    <tr><th>번호</th><th>제목</th><th>작성자번호</th><th>작성자닉네임</th><th>작성일</th><th>조회수</th><th>태그</th></tr>
  </thead>
  <tbody>
<c:forEach items="${list}" var="board">
    <tr>
        <td>${board.boardNo}</td>
        <td><a href='view?boardNo=${board.boardNo}'>${board.boardTitle}</a></td>
        <td>${board.writer.userNo}</td>
        <td>${board.writer.userNickname}</td>
        <td>${board.boardCreatedDate}</td>
        <td>${board.boardCount}</td>
        <td>${board.boardTag}</td>
    </tr>
</c:forEach>
  </tbody>
</table>


</body>
</html>
