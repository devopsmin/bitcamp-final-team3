<%@ page
        language="java"
        contentType="text/html;charset=UTF-8"
        pageEncoding="UTF-8"
        trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<jsp:include page="../header.jsp"/>

<form action='selectHotel' method="post">
    숙소: <input name='locationName' type='text'><br>
    숙소설명 : <textarea name='locationDesc'></textarea><br>
    <button type="submit">저장</button>
</form>
</body>
</html>
