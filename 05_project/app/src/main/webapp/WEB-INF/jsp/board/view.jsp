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
    <button type="button" onclick="location.href='list'">좋아요</button> <input readonly name="boardLike" type="text" value="${board.boardLike}"><br>
    <button type="button" onclick="location.href='list'">즐겨찾기</button> <input readonly name="boardLike" type="text" value="${board.boardFavor}"><br>
    <form action="modify" method="post">
        번호: <input readonly name='no' type='text' value='${board.boardNo}'><br>
        제목: <input readonly name='title' type='text' value='${board.boardTitle}'><br>
        내용: <textarea readonly name='content'>${board.boardContent}</textarea><br>
        작성일: <input readonly type='text'
                    value='<fmt:formatDate value="${board.boardCreatedDate}" pattern="yyyy-MM-dd"/>'><br>
        조회수: <input readonly type='text' value='${board.boardCount}'><br>
        작성자: <input readonly type='text' value='${board.writer.userNickname}'><br>
    <button type="button" onclick="location.href='list'">목록</button>
    <button type="submit">수정</button>
    <button type='button' onclick='location.href="delete?boardNo=${board.boardNo}"'>삭제</button>
    </form>
    <br> 댓글 : <br>
    <c:if test="${empty commentList}">
        <p>댓글이 없습니다.</p>
    </c:if>

    <c:if test="${not empty commentList}">
        <ul>
            <c:forEach items="${commentList}" var="comment">
                <li>
                    댓글 번호 : <input readonly type='text' value='${comment.commentNo}' size=3>
                    작성자 번호 : <input readonly type='text' value='${comment.userNo}'>
                    작성일 : <input readonly type='text' value='${comment.commentCreatedDate}'> <br>
                    댓글 : <input readonly type='text' value='${comment.commentContent}' size=50><br>
                    <button type='button' onclick='deleteComment(${comment.commentNo})'>삭제</button>
                </li>
            </c:forEach>
        </ul>
    </c:if>
</c:if>

<script>
  function deleteComment(commentNo) {
    if (confirm('댓글을 삭제하시겠습니까?')) {
      location.href = '/app/comment/delete?commentNo=' + commentNo + '&boardNo=${board.boardNo}';
    }
  }
</script>

<hr />

<form id="commentForm" action="../comment/add" method="post">
    게시글 번호 <input type="text" readonly name="boardNo" value="${board.boardNo}">
    작성자 번호 <input type="text" readonly name="userNo" value="5"> <br>
    댓글 내용 작성 <textarea name="commentContent" placeholder="댓글을 입력하세요"></textarea>
    <button type="submit">댓글 작성</button>
</form>

</body>
</html>