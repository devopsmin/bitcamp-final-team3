<%@ page
    language="java" 
    contentType="text/html;charset=UTF-8" 
    pageEncoding="UTF-8"
    trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="../header.jsp"/>

<h1>회원 목록</h1>
<p><a href='users/form'>새 회원</a></p>
<table>
  <thead>
      <tr>
          <th>번호</th>
          <th>이메일</th>
          <th>닉네임</th>
          <th>전화번호</th>
          <th>권한</th>
          <th>블록 여부</th>
          <th>SNS 번호</th>
      </tr>
  </thead>
  <tbody>

<c:forEach items="${list}" var="user">
<tr>
  <td>${user.userNo}</td>
  <td>${user.userEmail}</td>
  <td>
    <a href='users/${user.userNo}'>${user.userNickname}</a>
  </td>
  <td>${user.userTel}</td>
  <td>${user.userRole}</td>
  <td>${user.userBlock == 0 ? '미차단' : '차단'}</td>
  <td>${user.snsNo}</td>
</tr>
</c:forEach>

  </tbody>
</table>

</body>
</html>
