<%@ page
        language="java"
        contentType="text/html;charset=UTF-8"
        pageEncoding="UTF-8"
        trimDirectiveWhitespaces="true"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<jsp:include page="../header.jsp"/>

        <form action="modify" method="post">
            <h1>${board.boardTitle}</h1>
            <a href="form">동행 게시판 - 상세 글조회</a><br><br>
            글번호 : <input readonly name='BoardNo' style="border:none; display: none" type="text" value="${board.boardNo}">
            <p>작성자: ${board.writer.userNickname}</p>
            <p>작성일: ${board.boardCreatedDate}</p>
            <p>조회수: ${board.boardCount}</p>
            <p>내용: ${board.boardContent}</p>
            <button type="button" onclick="location.href='list'">목록</button>
            <button type="submit">수정</button>
            <button type='button' onclick='location.href="delete?boardNo=${board.boardNo}"'>삭제</button>
        </form>
            <br>
                <h3>댓글</h3>
                <c:forEach items="${board.comments}" var="comments">
                    <p>${comments.commentContent}</p>
                </c:forEach>
            <br>

    </body>
</html>