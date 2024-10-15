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
        제목: <input  name='title' type='text' value='${board.boardTitle}'><br>
        내용: <textarea  name='content'>${board.boardContent}</textarea><br>
        작성일: <input readonly type='text'
                    value='<fmt:formatDate value="${board.boardCreatedDate}" pattern="yyyy-MM-dd"/>'><br>
    <button type="button" onclick="location.href=view?boardNo=${board.boardNo}">취소</button>
    <button type="button" onclick="location.href='list'">목록</button>
</c:if>

</body>
</html>