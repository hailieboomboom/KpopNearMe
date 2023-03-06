<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<title>Display User Details</title>
</head>
<body style="background-color: grey">
<c:if test="${not empty sessionScope.currentUser }">
		<!-- Navigation-->
		<nav class="navbar navbar-expand-lg navbar-dark fixed-top"
			id="mainNav">
			<div class="container" id="navContainer">
			<!-- 	<a class="navbar-brand" href="#page-top"><img
					src="resources/assets/img/navbar-logo.svg" alt="..." /></a> -->
				<button class="navbar-toggler" type="button"
					data-bs-toggle="collapse" data-bs-target="#navbarResponsive"
					aria-controls="navbarResponsive" aria-expanded="false"
					aria-label="Toggle navigation">
					Menu <i class="fas fa-bars ms-1"></i>
				</button>
				<div class="collapse navbar-collapse" id="navbarResponsive">
					<ul class="navbar-nav text-uppercase ms-auto py-4 py-lg-0">
						<li class="nav-item"><a class="nav-link" href=<c:url value = "/"/>>HOME</a></li>
						<li class="nav-item"><a class="nav-link" href="showFavoritePlaces">Fav Places</a></li>
						<li class="nav-item"><a class="nav-link" href="showPlacesByCurrentUser">Created Places</a></li>
						<li class="nav-item"><a class="nav-link" href="showAllPlaces">All Places</a></li>
						<li class="nav-item"><a class="nav-link" href="searchPlace">Search</a></li>
						<li class="nav-item"><a class="nav-link" href="displayUser">Profile</a></li>
					</ul>
				</div>
			</div>
		</nav>

	</c:if>
<p id="hello">KPOP NEAR ME</p>
<section class="page-section bg-light" id="portfolio">
<div style="text-align:center;display:block;">
<h3>Welcome, user ${sessionScope.currentUser.username}</h3>
<p>Username: ${sessionScope.currentUser.username}</p>
<p>Email: ${sessionScope.currentUser.email}</p>
<p>First Name: ${sessionScope.currentUser.fName}</p>
<p>Last Name: ${sessionScope.currentUser.lName}</p>
</div>
</section>
<div style="text-align:center;display:block;">
<!-- <h3><a  href="updateUser">Update Profile</a></h3> -->
<br>
<br>
<!-- <a class="btn btn-primary btn-xl text-uppercase" href="createPlace">Click here create a place</a>
<br> -->
<!-- <a href="showAllPlaces">Click here see all places</a>
<br> -->
<a class="btn btn-primary btn-xl text-uppercase" href="updateUser">Update Profile</a>
<br>
<br>
<!-- <a class="btn btn-primary btn-xl text-uppercase" href="showFavoritePlaces">My Saved Places</a>
<br> -->
<a class="btn btn-primary btn-xl text-uppercase" href="logout">Log Out</a>
<br>
<br>
<a class="btn btn-primary btn-xl text-uppercase" href="deleteUser">Delete My Profile</a>
<br>
<!-- <a class="btn btn-primary btn-xl text-uppercase" href=<c:url value = "/"/>>Click here to go back home</a> -->
</div>
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