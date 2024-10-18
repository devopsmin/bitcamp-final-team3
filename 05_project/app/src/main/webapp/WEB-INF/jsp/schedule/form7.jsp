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
<h1>일정 확정</h1>
<table>
    <thead>
    <tr><th>번호</th><th>여행지</th><th>여행일차</th><th>여행순서</th></tr>
    </thead>

    <tbody>
    <c:forEach items="${scheduleList}" var="schedule">
        <tr>
            <td>${schedule.location.locationNo}</td>
            <td>${schedule.location.locationName}</td>
            <td>${schedule.scheduleDay}</td>
            <td>${schedule.scheduleRoute}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
