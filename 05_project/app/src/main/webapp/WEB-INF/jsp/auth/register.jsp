<%@ page
    language="java"
    contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"
    trimDirectiveWhitespaces="true"%>

<jsp:include page="../header.jsp"/>

<h1>회원 가입</h1>
<form action="/auth/register" method="post">
    이메일: <input name='userEmail' type='email' required><br>
    닉네임: <input name='userNickname' type='text' required><br>
    암호: <input name='userPassword' type='password' required><br>
    연락처: <input name='userTel' type='tel' required><br>
    <input type='submit' value='등록'>
</form>

</body>
</html>
