<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
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
<title>Show Places By Creator</title>
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
		<h1 style="text-align: center">Here are the places created by you</h1>
			<div class="container">
			<div class="row">
			<c:forEach items="${sessionScope.createdPlaces }" var="currentPlace">

		<div class="col-lg-4 col-sm-6 mb-4">
			<!-- Portfolio item 1-->
			<div class="portfolio-item">
				<img class="img-fluid"
					src="${currentPlace.img }"
					alt="image not available" />
				<div class="portfolio-caption">
					<div class="portfolio-caption-heading">Name:
						${currentPlace.name}</div>
					<div class="portfolio-caption-subheading text-muted">Address:
						${currentPlace.address}</div>
					<div class="portfolio-caption-subheading text-muted">Overall
						Rating: ${currentPlace.overallRating}</div>
					<div class="portfolio-caption-subheading text-muted">
						Biases:
						<c:forEach items="${currentPlace.biases}" var="currentBias">
							<span>${ currentBias.name} </span>
						</c:forEach>
					</div>
						<div class="portfolio-caption-subheading text-muted"><a href="updatePlace?placeId=${ currentPlace.id}">Click here to update the place</a></div>
				</div>

			</div>
		</div>
		
	 
	 </c:forEach>
				</div>
				</div>
				</section>
	

<br>
<a href=<c:url value = "/"/>>Click here to go back home</a>
</body>
</html>