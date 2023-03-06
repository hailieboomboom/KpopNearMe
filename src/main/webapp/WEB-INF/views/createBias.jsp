<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf"%>
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
<title>Add a Bias</title>
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

<h1>Biases Marked For ${sessionScope.currentPlace.name}</h1>

<c:choose>
	<c:when test="${not empty sessionScope.currentPlace.biases}">
	<div class="container">
				<div class="row">
		<c:forEach items="${sessionScope.currentPlace.biases }" var="currentBias">

						<div class="col-lg-4 col-sm-6 mb-4">
							<!-- Portfolio item 1-->
							<div class="portfolio-item">
								<div class="portfolio-caption">
									<div class="portfolio-caption-heading">Bias</div>
									<div class="portfolio-caption-subheading text-muted">Name:
										${currentBias.name}</div>
									<div class="portfolio-caption-subheading text-muted">
										<c:if test="${sessionScope.currentUser.username eq sessionScope.currentPlace.creator.username}">
												<a href="deleteBias?biasId=${currentBias.id}">Delete</a>
									</c:if>
									</div>
								</div>
							</div>
						</div>

			<%-- 			<p>Bias</p>
		<p>Name: ${currentBias.name}</p>
		<c:if test="${sessionScope.currentUser.username eq sessionScope.currentPlace.creator.username}">
			<a href="deleteBias?biasId=${currentBias.id}">Delete</a>
		</c:if> --%>
		</c:forEach>
		</div>
		</div>
	</c:when>
	<c:otherwise>
		<p>Add the first bias tag</p>
	</c:otherwise>
</c:choose>
<br><br>
<h3>Add your bias here</h3>
<sf:form action="processCreateBias" method="POST" modelAttribute="bindBias">
<sf:label path="name">Bias Name </sf:label>
<sf:input type="text" path="name"/>

<input type="submit" value="Create">

</sf:form>

<br>
<a href="showAllPlaces">Go Back</a>
<br>
<a href=<c:url value = "/"/>>Home</a>
<c:if test="${not empty requestScope.errorMessage}">
        <p>${requestScope.errorMessage}</p>
</c:if>
</div>
</section>
</body>



</body>
</html>