<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Domino show result</title>
</head>
<body>
<a href="/index.html">Home page</a>
<p><b>Current set: <c:out value="${current_set}" /></b></p>
<form action="/getComb" method="get" >
    <p><input type="radio" name="type" value="max" checked> Show only max lenght sequence</p>
    <p><input type="radio" name="type" value="all" > Show all sequences </p>

    <button type="submit">Show</button>
    <p><c:out value="${message}" /></p>
    ${result}
</form>

<form action="/getComb" method="post" >
    <button type="submit">Show history</button><br>
    <p>${history}</p>
</form>

<a href="/Create.jsp"><button type="button">Create new set</button></a>
</body>
</html>