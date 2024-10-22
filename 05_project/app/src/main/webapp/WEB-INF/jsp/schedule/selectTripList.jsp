<%@ page
        language="java"
        contentType="text/html;charset=UTF-8"
        pageEncoding="UTF-8"
        trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<jsp:include page="../header.jsp"/>
<h1>여정 선택</h1>
    <form action="editSchedule" method="post">
        <table border="1">
            <thead>
            <tr><th>선택</th>
                <th>작성자</th>
                <th>테마</th>
                <th>여행지</th>
                <th>여행일자</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach items="${tripList}" var="trip">
                <tr>
                    <td><input type="checkbox" name="selectedTripNo" value="${trip.tripNo}"></td>
                    <td>${trip.userNo}</td>
                    <td>${trip.thema.themaName}</td>
                    <td>${trip.city.cityName}</td>
                    <td>${trip.startDate} ~ ${trip.endDate}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <button type="submit">다음</button>
    </form>

</body>
</html>
