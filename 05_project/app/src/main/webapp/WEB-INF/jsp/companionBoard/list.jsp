<%@ page
        language="java"
        contentType="text/html;charset=UTF-8"
        pageEncoding="UTF-8"
        trimDirectiveWhitespaces="true"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset='UTF-8'>
        <title>Title</title>
        <link href='/css/common.css' rel='stylesheet'>
    </head>
    <body>
        <h1>게시글 목록</h1>
        <a href="form">게시글 작성하기</a>
        <table>
            <thead>
                <tr>
                    <th>번호</th>
                    <th>제목</th>
                    <th>작성일</th>
                    <th>조회수</th>
                    <th>태그</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${list}" var="board">
                    <tr>
                        <td>${board.boardNo}</td>
                        <td><a href="view?boardNo=${board.boardNo}">${board.boardTitle}</a></td>
                        <td>${board.boardCreatedDate}</td>
                        <td>${board.boardCount}</td>
                        <td>${board.boardTag}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>