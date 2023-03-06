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
</head>
<title>Index Page</title>
</head>
<body id="page-top">
  <!-- Masthead-->
        <header class="masthead">
            <div class="container">
                <div class="masthead-subheading">Find and Share Best Places for Kpop Stans</div>
                <div class="masthead-heading text-uppercase">Welcome to KpopNearMe</div>
                <a class="btn btn-primary btn-xl text-uppercase" href="showAllPlaces">See All Places</a>
            </div>
        </header>
<h1></h1>

	
	<c:if test="${empty sessionScope.currentUser }">
	<div class="container" style="text-align:center;display:block;">
	<br>
<br>
<br>
<br>
<a class="btn btn-primary btn-xl text-uppercase" href="registerUser">Click here to register a new user</a>
<br>
<br>
<a class="btn btn-primary btn-xl text-uppercase" href="login">Click here to log in</a>
<br>
</div>
</c:if>

<c:if test="${not empty sessionScope.currentUser }">
<div class="container" style="text-align:center;display:block;">
<!-- <a class="btn btn-primary btn-xl text-uppercase" href="updateUser">Update Profile</a> -->
<!-- <button onclick="window.location.href='updateUser';">Update User Details</button>
<a href="updateUser" class="button">Click here to update your user details</a> -->
<br>
<!-- <a class="btn btn-primary btn-xl text-uppercase" href="logout">Log Out</a> -->
<br>
<a class="btn btn-primary btn-xl text-uppercase" href="displayUser">Check Profile</a>
<!-- <a class="btn btn-primary btn-xl text-uppercase" href="deleteUser">Delete User Account</a> -->
<br>
<br>
<a class="btn btn-primary btn-xl text-uppercase" href="createPlace">Create A Place</a>
<br>
<br>

<a class="btn btn-primary btn-xl text-uppercase" href="showPlacesByCurrentUser">Check My Created Places</a>
<br>
<br>
<a class="btn btn-primary btn-xl text-uppercase" href="showFavoritePlaces">Check My Saved Places</a>
<br>
<br>
<a class="btn btn-primary btn-xl text-uppercase" href="searchPlace">Search by Bias</a>
<br>
<br>
</div>
</c:if>

<c:if test="${not empty requestScope.errorMessage}">
        <p>${requestScope.errorMessage}</p>
    </c:if>


  <!-- Bootstrap core JS-->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
        <!-- Core theme JS-->
        <script src="resources/js/scripts.js"></script>
        <!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *-->
        <!-- * *                               SB Forms JS                               * *-->
        <!-- * * Activate your form at https://startbootstrap.com/solution/contact-forms * *-->
        <!-- * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *-->
        <script src="https://cdn.startbootstrap.com/sb-forms-latest.js"></script>
</body>
</html>