<%@ page
    language="java" 
    contentType="text/html;charset=UTF-8" 
    pageEncoding="UTF-8"
    trimDirectiveWhitespaces="true"%>

<h1>일정 질문 작성하기</h1>
<form action='add' method="post" enctype="multipart/form-data">
    제목: <input name='boardTitle' type='text'><br>
    내용: <textarea name='boardContent' rows="10" cols="100" placeholder="내용을 입력하세요"></textarea><br>
    태그: <input name='boardTag' type='text'><br>
    <input type='submit' value='등록'>
    <button type='button' onclick='location.href="list"'>취소</button>

</form>

</body>
</html>