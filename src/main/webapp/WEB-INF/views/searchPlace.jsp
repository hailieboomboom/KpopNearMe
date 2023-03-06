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
<title>Search for Places</title>
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

	<div style="text-align: center; display: block;">
		<h1>Look for places based on your bias</h1>

		<form action="processSearchPlace" method="GET">
			<label for="biasInput">Bias Name: </label>
			<!-- id is name of textbox, name refers to the value in textbox -->
			<input type="text" name="biasName" id="biasInput"> <input
				type="submit" value="search">

		</form>

	</div>

	<br>
	
<c:choose>
	<c:when test="${not empty sessionScope.foundSearchPlaces}">
	<section class="page-section bg-light" id="portfolio">
	
			<div class="container">
			<div class="row">
		<c:forEach items="${sessionScope.foundSearchPlaces }" var = "currentPlace">
			<div class="col-lg-4 col-sm-6 mb-4">
			<!-- Portfolio item 1-->
			<div class="portfolio-item">
				<img class="img-fluid"
					src="${currentPlace.img }"
					alt="Image not available" />
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
										<div class="portfolio-caption-subheading text-muted">

											<c:choose>
												<c:when
													test="${not empty sessionScope.currentUser.favoritePlaces}">

													<c:choose>
														<c:when
															test="${sessionScope.currentUser.favoritePlaces.contains(currentPlace)}">
															<p>Place saved</p>
														</c:when>
														<c:otherwise>
															<a href="saveSearchPlace?placeId=${currentPlace.id}">Save
																this place to my list</a>
														</c:otherwise>
													</c:choose>

												</c:when>

												<c:otherwise>
													<a href="saveSearchPlace?placeId=${currentPlace.id}">Save
														this place to my list</a>
												</c:otherwise>

											</c:choose>

										</div>


									</div>

			</div>
		</div>
		
		
		<%-- <h3>Place</h3>
	 	<p>Name: ${currentPlace.name}</p>
	 	<p>Address: ${currentPlace.address}</p>
	 	<p>Overall Rating: ${currentPlace.overallRating}</p>
	 	<p>Img: ${currentPlace.img}</p>
	 	<p>Biases: </p>
	 	<c:forEach items="${currentPlace.biases}" var="currentBias">
	 		<p>${ currentBias.name}</p>
	 	</c:forEach> --%>
	 	<%-- <a href="createReview?placeId=${currentPlace.id}">See reviews</a>
	 	
	 	<c:if test="${currentPlace.creator.id eq sessionScope.currentUser.id}">
	 	
	 	<a href="createBias?placeId=${currentPlace.id}">Add a bias</a>
	 		
	 	</c:if> --%>
	 	
      
      
      <%-- <c:choose>
      <c:when test="${not empty sessionScope.currentUser.favoritePlaces}">
        	
         		<c:choose>
	 				<c:when test="${sessionScope.currentUser.favoritePlaces.contains(currentPlace)}">
	 		 			<p>Place saved</p>
	 				</c:when>
	 				<c:otherwise>
	 	    			<a href="saveSearchPlace?placeId=${currentPlace.id}">Save this place to my list</a>
	 				</c:otherwise>
	      		</c:choose> 
        
      </c:when>
      
      <c:otherwise>
       		<a href="saveSearchPlace?placeId=${currentPlace.id}">Save this place to my list</a>  
      </c:otherwise>
      
      </c:choose> --%>
		</c:forEach>
		</div>
		</div>
		</section>
	</c:when>
	<c:otherwise>
		<p></p>
	</c:otherwise>
</c:choose>

<br>
<a href=<c:url value = "/"/>>Click here to go back home</a>
</body>
</html>