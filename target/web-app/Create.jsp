<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ua.ibt.*, java.util.*, java.io.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Domino create set</title>
</head>
<body>
<a href="/index.html">Home page</a>
<form action="/getBones" method="get" >
    <p><b>How many bones to give out?</b></p>

    <p><input type="radio" name="type" value="random"> Random</p>
    <p><input type="radio" name="type" value="set" checked> To set up:
        <input type="number" name="count" style="width: 40pt" value="4"> </p>

    <button type="submit">Let's go</button>
    <p><c:out value="${message}" /></p>
    <p><c:out value="${result}" /></p>
</form>
<form action="/getBones" method="post" >
    <button type="submit">Show combinations</button>
</form>

</body>
</html>