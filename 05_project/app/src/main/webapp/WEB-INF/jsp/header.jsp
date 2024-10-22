<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page
    language="java" 
    contentType="text/html;charset=UTF-8" 
    pageEncoding="UTF-8"
    trimDirectiveWhitespaces="true"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>header</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<c:url value="/css/var.css"/>">
    <link rel="stylesheet" href="<c:url value="/css/style.css"/>">
</head>
<body>

<header>
<div class="fram-topbanner">
    <div class="button-03-s6">
        <div class="div2"><a href="/schedule/form1">여행시작</a></div>
    </div>
    <div class="button-03-s">
        <div class="div2">여행추천</div>
    </div>
    <div class="button-03-s2">
        <div class="div2">여행일정</div>
    </div>
    <div class="button-03-s3">
        <div class="div2">여행후기</div>
    </div>
    <div class="button-03-s4">
        <div class="div2">동행구하기</div>
    </div>

    <div class="button-03-s5">
        <div class="div2"><a href="/users">마이페이지인데 임시회원관리</a></div>
    </div>


       <div class="button-02-s3">
           <c:if test="${empty loginUser}">
               <div class="div"><a href="/auth/form">로그인</a></div>
           </c:if>
           <c:if test="${not empty loginUser}">
               <div class="div"><a href="/auth/logout">로그아웃</a></div>
           </c:if>
       </div>

    <div class="frame-427318169">
        <div class="div3">모두의 여행</div>
    </div>
</div>

</header>
