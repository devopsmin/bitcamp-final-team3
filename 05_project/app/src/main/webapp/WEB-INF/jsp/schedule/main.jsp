<%@ page
        language="java"
        contentType="text/html;charset=UTF-8"
        pageEncoding="UTF-8"
        trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<jsp:include page="../header.jsp"/>

<h1>여행일정만들기</h1>
<form action="createTrip" method="post" style="display:inline;">
    <input type="hidden" name="tripType" value="selectLocation" />
    <button type="submit">일정생성</button>
</form>

<form action="createTrip" method="post" style="display:inline;">
    <input type="hidden" name="tripType" value="selectTripList" />
    <button type="submit">일정가져오기</button>
</form>
</body>
</html>
