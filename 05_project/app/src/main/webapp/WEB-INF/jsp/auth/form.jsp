<%@ page
    language="java" 
    contentType="text/html;charset=UTF-8" 
    pageEncoding="UTF-8"
    trimDirectiveWhitespaces="true"%>

<jsp:include page="../header.jsp"/>

<h1>로그인</h1>
<form action='login' method="post">
    이메일: <input name='userEmail' type='email' value="${cookie.email.value}"><br>
    암호: <input name='userPassword' type='password'><br>
    <input type="checkbox" name="saveEmail" ${empty cookie.email.value ? "" : "checked"}> 이메일 저장<br>
    <input type='submit' value='로그인'>
</form>

</body>
</html>
