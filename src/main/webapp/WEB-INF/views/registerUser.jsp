<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<meta charset="UTF-8">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
<!-- Favicon-->
        <link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
        <!-- Font Awesome icons (free version)-->
       <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <!-- Google fonts-->
        <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css" />
        <link href="https://fonts.googleapis.com/css?family=Roboto+Slab:400,100,300,700" rel="stylesheet" type="text/css" />
        <!-- Core theme CSS (includes Bootstrap)-->
        <link href="resources/css/styles.css" rel="stylesheet" />
<title>User Registration</title>
</head>
<body style="background-color: grey">
<p id="hello">KPOP NEAR ME</p>


<section class="page-section bg-light" id="portfolio">
<div style="text-align:center;display:block;">
<h2>User Registration</h2>
<br><br>
<sf:form action="processUser" method="POST" modelAttribute="bindUser">

<sf:label path="username">Username: </sf:label>
<sf:input type="text" path="username"/>
<br>
<sf:label path="fName">First Name: </sf:label>
<sf:input type="text" path="fName"/>
<br>
<sf:label path="lName">Last Name: </sf:label>
<sf:input type="text" path="lName"/>
<br>
<sf:label path="password">Password: </sf:label>
<sf:input type="password" path="password"/>
<br>
<sf:label path="email">Email: </sf:label>
<sf:input type="text" path="email"/>
<br><br>
<input type="submit" value="Register Me Now">
<br>
<br>
<a  href="login">Have an account already? Login Instead</a>
<br>
<a href=<c:url value = "/"/>>Click here to go back home</a>
</sf:form>
</div>
</section>


<c:if test="${not empty requestScope.errorMessage}">
        <p>${requestScope.errorMessage}</p>
</c:if>

</body>
</html>