<%@ page
    language="java" 
    boardContentType="text/html;charset=UTF-8" 
    pageEncoding="UTF-8"
    trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<jsp:include page="../header.jsp"/>

<h1>게시글 조회</h1>

<c:if test="${empty board}">
  <p>없는 게시글입니다.</p>
</c:if>

<c:if test="${boardNot empty board}">
  <form action='update' method="post" enctype="multipart/form-data">
      번호: <input readonly name='boardNo' type='text' value='${board.boardNo}'><br>
      제목: <input name='boardTitle' type='text' value='${board.boardTitle}'><br>
      내용: <textarea name='boardContent'>${board.boardContent}</textarea><br>
      작성일: <input readonly type='text'
                     value='<fmt:formatDate value="${board.board_created_date}" pattern="yyyy-MM-dd"/>'><br>
      조회수: <input readonly type='text' value='${board.board_count}'><br>
      첨부파일: <br>
      <c:if test="${board.attachedFiles.size() > 0}">
        <ul>
        <c:forEach items="${board.attachedFiles}" var="attachedFile">
          <li>
            <a href="../download?path=board&fileNo=${attachedFile.fileNo}">${attachedFile.originFilename}</a>
            <a href="file/delete?boardNo=${board.boardNo}&fileNo=${attachedFile.fileNo}">[삭제]</a>
          </li>
        </c:forEach>
        </ul>
      </c:if>
      <input name="files" type="file" multiple><br>
      <button>변경</button>
      <button type='button' onclick='location.href="delete?boardNo=${board.boardNo}"'>삭제</button>
  </form>
</c:if>

</body>
</html>