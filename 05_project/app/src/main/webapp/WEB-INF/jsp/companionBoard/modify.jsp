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
    <form action="update" method="post">
        <input name='boardNo' readonly type='text' style="display: none" value='${board.boardNo}'><br>
        제목: <input name='boardTitle' type='text' value='${board.boardTitle}'><br>
        내용: <textarea name='boardContent'>${board.boardContent}</textarea><br>
        작성일: <input readonly style="border:none" type='text' value='<fmt:formatDate value="${board.boardCreatedDate}" pattern="yyyy-MM-dd"/>'><br>
        작성자: <input readonly style="border:none" type='text' value='${board.writer.userNickname}'><br>
        태그 : <input name="boardTag" type='text' value='${board.boardTag}'>
        <button type="button" onclick="location.href='list'">목록</button>
        <button type="submit">변경</button>
    </form>
</c:if>

</body>
</html>