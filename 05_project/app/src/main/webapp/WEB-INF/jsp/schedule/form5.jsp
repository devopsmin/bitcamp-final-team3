<%@ page
        language="java"
        contentType="text/html;charset=UTF-8"
        pageEncoding="UTF-8"
        trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<jsp:include page="../header.jsp"/>
<h1>숙소 선택</h1>
<form action="form6" method="post">
    <ul>
        <c:forEach items="${hotelList}" var="hotel">
            <li><input name='hotelNos' value='${hotel.locationNo}' type='checkbox'>${hotel.locationName}</li>
        </c:forEach>
    </ul>
    <button type="submit">다음</button>
</form>
</body>
</html>
