<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<title>Register</title>
</head>
<body>
<div class="container">
	<h1><span class="badge badge-secondary">Register as a New User for iFile</span></h1>
	<br>
        <div class="col-4">
            <div class="card">
                <div class="card-body">
                    <form action="Registration" method="post">
                        <div class="form-group">
                        	<c:if test="${ not empty userExists }">
                        		<p class="alert alert-danger"> ${ userExists } </p>
                        	</c:if>
                        	<c:if test="${ not empty nameError }">
                        		<p class="alert alert-danger"> ${ nameError } </p>
                        	</c:if>
                            <input type="text" class="form-control" placeholder="Enter your username" name="name" value="${ username }">
                        </div>
                        	<c:if test="${ not empty passwordError }">
                        		<p class="alert alert-danger"> ${ passwordError } </p>
                        	</c:if>
                        <div class="form-group">
                            <input type="password" class="form-control" placeholder="Enter your password" name="password">
                        </div>
                        <div class="form-group">
                       	 	<c:if test="${ not empty passwordCError }">
                        		<p class="alert alert-danger"> ${ passwordCError } </p>
                        	</c:if>
                            <input type="password" class="form-control" placeholder="Confirm password" name="passwordC">
							<c:if test="${ not empty confirmationError }">
                        		<p class="alert alert-danger"> ${ confirmationError } </p>
                        	</c:if>
                        </div>
                        <input class="btn btn-outline-warning" type="submit" name="RegisterBtn" value="Register"/>
                    </form>
                </div>
            </div>
        </div>
    </div>

</body>
</html>