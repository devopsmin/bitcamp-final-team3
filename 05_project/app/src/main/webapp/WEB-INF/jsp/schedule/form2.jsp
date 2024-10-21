<%@ page
        language="java"
        contentType="text/html;charset=UTF-8"
        pageEncoding="UTF-8"
        trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<jsp:include page="../header.jsp"/>
<h1>시군구 선택</h1>
<form action="form3" method="post">
    <select name="cityCode">
        <option value="">시군구 선택</option>
        <c:forEach items="${cityList}" var="city">
            <option value='${city.cityCode}'>${city.cityName}</option>
        </c:forEach>
    </select>
    <button type="submit">다음</button>
</form>

</body>
</html>
