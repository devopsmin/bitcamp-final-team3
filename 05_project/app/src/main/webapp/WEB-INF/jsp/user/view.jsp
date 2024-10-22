<%@ page
    language="java" 
    contentType="text/html;charset=UTF-8" 
    pageEncoding="UTF-8"
    trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../header.jsp"/>

<h1>회원 조회</h1>

<c:if test="${empty user}">
    <p>없는 회원입니다.</p>
</c:if>

<c:if test="${not empty user}">
    <form method="post" action="/users/${user.userNo}">
        번호: <input readonly type='text' value='${user.userNo}'><br>
        이메일: <input name='userEmail' type='email' value='${user.userEmail}' required><br>
        닉네임: <input name='userNickname' type='text' value='${user.userNickname}' required><br>
        암호: <input name='userPassword' type='password'><br>
        연락처: <input name='userTel' type='tel' value='${user.userTel}' required><br>
        <button type="submit">변경</button>
        <button type='button' onclick='deleteUser(${user.userNo})'>삭제</button>
    </form>
</c:if>

<script>
function deleteUser(no) {
    if (confirm("정말 삭제하시겠습니까?")) {
        const xhr = new XMLHttpRequest();
        xhr.open("DELETE", "/users/" + no, true); // URL 수정
        xhr.onload = function () {
            if (xhr.status === 200) {
                location.href = "../users";
            } else {
                window.alert("삭제 실패입니다!");
            }
        };
        xhr.send();
    }
}
</script>

</body>
</html>
