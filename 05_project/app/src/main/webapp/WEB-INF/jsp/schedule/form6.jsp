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
<h1>일정 편집</h1>
    <ul>
        <c:forEach items="${selectedLocation}" var="location">
            <li>${location.locationName}</li>
        </c:forEach>
    </ul>
    <p>-----------------------</p>
    <ul>
        <c:forEach items="${selectedHotels}" var="hotel">
            <li>${hotel.locationName}</li>
        </c:forEach>
    </ul>
    <button type="submit">다음</button>
</body>
</html>
