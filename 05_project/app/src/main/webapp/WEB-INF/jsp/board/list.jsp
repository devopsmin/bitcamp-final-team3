<%@ page
    language="java" 
    contentType="text/html;charset=UTF-8" 
    pageEncoding="UTF-8"
    trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset='UTF-8'>
    <title>Title</title>
</head>
<body>

<h1>게시글 목록</h1>
<table>
  <thead>
      <tr><th>번호</th></tr>
  </thead>
  <tbody>
<c:forEach items="${list}" var="board">
    <tr>
      <td>${board.boardNo}</td>
    </tr>
</c:forEach>
  </tbody>
</table>


</body>
</html>
