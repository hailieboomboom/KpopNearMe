<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<meta charset="UTF-8">
<!-- Favicon-->
        <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
        <!-- Font Awesome icons (free version)-->
       <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <!-- Google fonts-->
        <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css" />
        <link href="https://fonts.googleapis.com/css?family=Roboto+Slab:400,100,300,700" rel="stylesheet" type="text/css" />
        <!-- Core theme CSS (includes Bootstrap)-->
        <link href="resources/css/styles.css" rel="stylesheet" />
<title>User Login</title>
</head>
<body style="background-color: grey">
<p id="hello">KPOP NEAR ME</p>

<section class="page-section bg-light" id="portfolio">
<div style="text-align:center;display:block;">
<h1>User Login</h1>
<p>Welcome, please enter your username and password</p>
    <form action="processLogin" method="POST">
	<label for="usernameTextbox">Username: </label>
	<input type="text" name="username" id="usernameTextbox"/>
	<br>
	<label for="passwordTextbox">Password: </label>
	<input type="password" name="password" id="passwordTextbox"/>
	<br><br>
	<input type="submit" value="Click here to login"/>
	</form>
	<br><br>
	<c:if test="${not empty requestScope.errorMessage}">
        <p>${requestScope.errorMessage}</p>
    </c:if>
	<br><br>
<a href="registerUser">Do not have an account yet? Register here</a><br>
<a href=<c:url value = "/"/>>Click here to go back home</a>

</div>
</section>

</body>
</html>