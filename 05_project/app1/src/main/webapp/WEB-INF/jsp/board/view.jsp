<%@ page
    language="java" 
    contentType="text/html;charset=UTF-8" 
    pageEncoding="UTF-8"
    trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

       <h1>일정 질문 게시판</h1>

       <c:if test="${empty board}">
         <p>없는 게시글입니다.</p>
       </c:if>

       <c:if test="${not empty board}">
         <form action='update' method="post" enctype="multipart/form-data">
             <input type="hidden" name="boardNo" value="${board.boardNo}">
             제목: <input name='boardTitle' type='text' value='${board.boardTitle}'><br/>
             작성일: <input readonly type='text'
                            value='<fmt:formatDate value="${board.boardCreatedDate}" pattern="yyyy-MM-dd HH:mm:ss"/>'><br>
             조회수: <input readonly type='text' value='${board.boardCount}'><br>
             내용: <textarea name='boardContent'>${board.boardContent}</textarea><br>

             <button>수정</button>
             <button type='button' onclick='location.href="delete?boardNo=${board.boardNo}"'>삭제</button>
         </form>
       </c:if>

       </body>
       </html>