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
<h1>시군구 선택</h1>
<form action="form3" method="post">
    <select name="stateCode">
        <option value="">시군구 선택</option>
        <c:forEach items="${stateList}" var="state">
            <option value='${state.stateCode}'>${state.stateName}</option>
        </c:forEach>
    </select>
    <button type="submit">다음</button>
</form>

</body>
</html>
