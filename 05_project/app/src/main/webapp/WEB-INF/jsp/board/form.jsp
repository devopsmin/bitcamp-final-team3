<%@ page
        language="java"
        contentType="text/html;charset=UTF-8"
        pageEncoding="UTF-8"
        trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Title</title>
</head>
<body>
<h1>게시글 등록</h1>

<!-- 데이터 확인용 -->
<c:if test="${not empty trips}">
  <p>데이터가 존재합니다. 개수: ${trips.size()}</p>
</c:if>
<c:if test="${empty trips}">
  <p>데이터가 없습니다.</p>
</c:if>

<table>
  <thead>
  <tr>
    <th>여행번호</th>
    <th>여행제목</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach items="${trips}" var="trip">
    <tr>
      <td>${trip.tripNo}</td>
      <td>${trip.tripTitle}</td>
    </tr>
  </c:forEach>
  </tbody>
</table>

<form action='add' method="post">

  <br>
  제목: <input name='boardTitle' type='text'><br>
  내용: <textarea name='boardContent'></textarea><br>
  태그: <input name='boardTag' type='text'><br>
  <input type='submit' value='등록'>
</form>



</body>
</html>