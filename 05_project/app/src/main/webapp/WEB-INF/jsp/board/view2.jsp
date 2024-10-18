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
             내용: <br>
             <textarea name='boardContent' rows="10" cols="100">${board.boardContent}</textarea><br>
             태그: <input name='boardTag' value='${board.boardTag}'><br/>

             <button>완료</button>
             <button type='button' onclick='location.href="view?boardNo=${board.boardNo}"'>취소</button>
         </form>
       </c:if>

       </body>
       </html>