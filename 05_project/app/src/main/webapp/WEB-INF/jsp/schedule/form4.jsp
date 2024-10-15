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
<h1>여행지 선택</h1>
<form action="form5" method="post">
    <ul>
        <c:forEach items="${locationList}" var="location">
            <li><input name='locationName' value='${location.locationNo}' type='checkbox'>${location.locationName}</li>
        </c:forEach>
    </ul>
    <button type="submit">다음</button>
</form>
</body>
</html>
