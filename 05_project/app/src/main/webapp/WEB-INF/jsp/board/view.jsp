<%@ page
        language="java"
        contentType="text/html;charset=UTF-8"
        pageEncoding="UTF-8"
        trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1>게시글 조회</h1>

<c:if test="${empty board}">
    <p>없는 게시글입니다.</p>
</c:if>

<c:if test="${not empty board}">
        번호: <input readonly name='no' type='text' value='${board.boardNo}'><br>
        제목: <input readonly name='title' type='text' value='${board.boardTitle}'><br>
        내용: <textarea readonly name='content'>${board.boardContent}</textarea><br>
        작성일: <input readonly type='text'
                    value='<fmt:formatDate value="${board.boardCreatedDate}" pattern="yyyy-MM-dd"/>'><br>
        조회수: <input readonly type='text' value='${board.boardCount}'><br>
        작성자: <input readonly type='text' value='${board.writer.userNickname}'><br>
    <button type="button" onclick="location.href='list'">목록</button>
    <button type="button" onclick="location.href='modify?boardNo=${board.boardNo}'">수정</button>
    <button type='button' onclick='location.href="delete?boardNo=${board.boardNo}"'>삭제</button>
    <br> 댓글 : <br>
    <c:if test="${board.comments.size() > 0}">
        <ul>
            <c:forEach items="${board.comments}" var="comments">
                <li>
                    <input readonly type='text' value='${comments.commentContent}'>
                    <input readonly type='text' value='${comments.userNo}'>
                    <input readonly type='text' value='${comments.commentCreatedDate}'>
                    <br>
                </li>
            </c:forEach>
        </ul>
    </c:if>
</c:if>

</body>
</html>