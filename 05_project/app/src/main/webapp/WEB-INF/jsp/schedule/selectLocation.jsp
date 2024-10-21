<%@ page
        language="java"
        contentType="text/html;charset=UTF-8"
        pageEncoding="UTF-8"
        trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<jsp:include page="../header.jsp"/>
<h1>여행지 선택</h1>
<form action="selectHotel" method="post">
    <ul>
        <c:forEach items="${locationList}" var="location">
            <li><input name='locationNos' value='${location.locationNo}' type='checkbox'>${location.locationName}</li>
        </c:forEach>
    </ul>

    <p>---내가추가한 여행지 </p>
    <ul>
        <c:forEach items="${myLocations}" var="location">
        <li><input checked name='locationNos' value='${location.locationNo}' type='checkbox'> ${location.locationName}</li>
        </c:forEach>
    </ul>
    <button type="submit"><a href="addLocation">여행지추가하기</a></button>
    <button type="submit">다음</button>
</form>
</body>
</html>
