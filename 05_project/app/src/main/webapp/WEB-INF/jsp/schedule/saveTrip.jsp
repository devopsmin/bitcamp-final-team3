<%@ page
        language="java"
        contentType="text/html;charset=UTF-8"
        pageEncoding="UTF-8"
        trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<jsp:include page="../header.jsp"/>
${trip}
<form action='save' method="post">
    제목: <input name='tripTitle' type='text'><br>
    테마: <select name="themaNo">
                <option value="">테마 선택</option>
                <c:forEach items="${themas}" var="thema">
                    <option value="${thema.themaNo}">${thema.themaName}</option>
                </c:forEach>
            </select>
    <button type="submit">저장</button>
</form>

</body>
</html>
