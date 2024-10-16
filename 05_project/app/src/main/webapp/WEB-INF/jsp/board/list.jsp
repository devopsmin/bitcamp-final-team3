<%@ page
    language="java" 
    contentType="text/html;charset=UTF-8" 
    pageEncoding="UTF-8"
    trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<h1>일정 질문 게시판</h1>
<p><a href='form'>글쓰기</a></p>
<table>
  <thead>
      <tr><th>제목</th><th>날짜</th></tr>
  </thead>
  <tbody>
<c:forEach items="${list}" var="board">
    <tr>
      <td><a href='view?boardNo=${board.boardNo}'>${board.boardTitle}</a></td>
      <td><fmt:formatDate value="${board.boardCreatedDate}" pattern="yyyy-MM-dd"/></td>
    </tr>
</c:forEach>
  </tbody>
</table>
</body>
</html>
