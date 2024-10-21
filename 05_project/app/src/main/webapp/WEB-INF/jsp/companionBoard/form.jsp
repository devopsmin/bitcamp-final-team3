<%@ page
        language="java"
        contentType="text/html;charset=UTF-8"
        pageEncoding="UTF-8"
        trimDirectiveWhitespaces="true"
%>
<jsp:include page="../header.jsp"/>

        <h1>게시글 작성</h1>
        <form action="add" method="post">
            <label>제목</label>
                <input name="boardTitle" type="text"><br><br>
            <label >내용</label><br>
                <textarea name="boardContent"></textarea><br><br>
            <label >태그</label><br>
                <input name="boardTag" type="text"><br><br>
            <input type="submit" value="글등록">
        </form>
        <a href="/app/comanionBoard/list">목록</a>
    </body>
</html>