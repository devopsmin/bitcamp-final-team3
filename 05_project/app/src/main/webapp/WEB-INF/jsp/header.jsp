<%@ page 
    language="java" 
    contentType="text/html;charset=UTF-8" 
    pageEncoding="UTF-8"
    trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset='UTF-8'>
    <title>Title</title>
    <link href='/css/common.css' rel='stylesheet'>
</head>
<body>

<header>
  <a href='/'><img src='/images/home.png'></a>
  <h1>프로젝트 관리 시스템</h1>
  <nav>
    <ul>
      <li class='btn btn-default'><a href='/app/user/list'>회원</a></li>
      <li class='btn btn-default'><a href='/app/project/list'>프로젝트</a></li>
      <li class='btn btn-default'><a href='/app/board/list'>게시글</a></li>
    </ul>
  </nav>
  <div class='login-state pos-right'>
<c:if test="${empty loginUser}">
    <a href='/app/auth/form' class='btn btn-primary'>로그인</a>
</c:if>
<c:if test="${not empty loginUser}">
    <a href='/app/user/view?no=${loginUser.no}' class='btn btn-light'>${loginUser.name}</a>
    <a href='/app/auth/logout' class='btn btn-secondary'>로그아웃</a>
</c:if>
  </div>

</header>