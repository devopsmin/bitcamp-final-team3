<%@ page
        language="java"
        contentType="text/html;charset=UTF-8"
        pageEncoding="UTF-8"
        trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<jsp:include page="../header.jsp"/>
<h1>일정 편집</h1>
<form action="checkSchedule" method="post">
    <ul>
        <c:forEach items="${selectedLocation}" var="location" varStatus="status">
            <li>
                <label>
                        ${location.locationName}
                    <!-- locationNo를 숨겨진 필드로 전송 -->
                    <input type="hidden" name="scheduleList[${status.index}].location.locationNo" value="${location.locationNo}" />
                    <!-- scheduleDay를 숫자 입력으로 전송 -->
                    <input name="scheduleList[${status.index}].scheduleDay" type="number" min="1" placeholder="Enter day" required />
                    <!-- scheduleRoute를 숫자 입력으로 전송 -->
                    <input name="scheduleList[${status.index}].scheduleRoute" type="number" min="1" placeholder="Enter route" required />
                </label>
            </li>
        </c:forEach>
    </ul>
    <button type="submit">다음</button>
</form>
</body>
</html>
