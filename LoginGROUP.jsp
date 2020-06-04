<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
<title>Login</title>
</head>
<body>
	<div class="container">
		<h1>
			<span class="badge badge-secondary">Login to iFile</span>
		</h1>
		<br>
		<div class="col-4">
			<div class="card">
				<div class="card-body">
					<c:if test="${ not empty loginError }">
						<p class="alert alert-danger">${ loginError }</p>
					</c:if>
					<c:if test="${ not empty userError }">
						<p class="alert alert-danger">${ userError }</p>
					</c:if>
					
					<!-- Login form -->
					<form action="Login" method="post">
						<div class="form-group">
							<c:if test="${ not empty nameError }">
								<p class="alert alert-danger">${ nameError }</p>
							</c:if>
							<input type="text" class="form-control"
								placeholder="Enter your username" name="name" value="${ user }">
						</div>
						<div class="form-group">
							<c:if test="${ not empty passwordError }">
								<p class="alert alert-danger">${ passwordError }</p>
							</c:if>
							<input type="password" class="form-control"
								placeholder="Enter your password" name="password">
						</div>
						<input class="btn btn-outline-success" type="submit"
							name="loginBtn" value="Login" /> <a
							class="btn btn-outline-danger" href="RegisterGROUP.jsp">Register</a>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>