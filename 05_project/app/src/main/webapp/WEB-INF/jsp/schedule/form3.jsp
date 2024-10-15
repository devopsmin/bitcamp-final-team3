<%@ page
        language="java"
        contentType="text/html;charset=UTF-8"
        pageEncoding="UTF-8"
        trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset='UTF-8'>
    <title>Title</title>
</head>
<body>
<h1>날짜 선택</h1>
<form action="form4" method="post">
    기간: <input name='startDate' type='date'> ~
    <input name='endDate' type='date'><br>
    <button>다음</button>
</form>
</body>
</html>
