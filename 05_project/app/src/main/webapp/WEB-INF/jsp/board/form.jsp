<%@ page
        language="java"
        contentType="text/html;charset=UTF-8"
        pageEncoding="UTF-8"
        trimDirectiveWhitespaces="true"%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Title</title>
</head>
<body>
<h1>게시글 등록</h1>
<form action='add' method="post">
  제목: <input name='boardTitle' type='text'><br>
  내용: <textarea name='boardContent'></textarea><br>
  태그: <input name='boardTag' type='text'><br>
  <input type='submit' value='등록'>
</form>



</body>
</html>