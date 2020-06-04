<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<c:if test="${ empty sessionScope.user }">
	<c:redirect url="LoginGROUP.jsp" />
</c:if>

<sql:setDataSource driver="com.mysql.jdbc.Driver"
	url="jdbc:mysql://cs3.calstatela.edu/cs3220stu91" user="cs3220stu91"
	password="Xy7DRTV*" />

<!-- select all files in the database that match current user id -->
<c:if test="${empty filename || not empty fileName }">
	<sql:query var="files"
		sql="SELECT id, filename, upload_date FROM files WHERE user_id='${sessionScope.userid}'" />
</c:if>
<c:if test="${not empty searchSuccess}">
	<sql:query var="files"
		sql="SELECT id, filename, upload_date FROM files WHERE user_id='${sessionScope.userid}' AND filename LIKE '%${filename}%'" />
</c:if>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
<title>Upload</title>
</head>
<body>
	<h1>
		<span class="badge badge-secondary">Upload</span>
	</h1>
	<h3>Welcome, ${sessionScope.user}!</h3>
	<br>
	<form action="Upload" method="post" enctype="multipart/form-data">
		File: <input type="file" name="Choose File" /><span>
	</form>
	<input class="btn btn-outline-success" type="submit" name="upload"
		value="Upload" />
	</span>
	<br>
	
	<!-- Display some messages regarding the actions have been done  -->
	<c:if test="${not empty uploadError}">
		<p class="alert alert-warning">${ uploadError }</p>
	</c:if>
	<c:if test="${not empty uploadSuccess}">
		<p class="alert alert-success">${ uploadSuccess }</p>
	</c:if>
	<c:if test="${not empty uploadDB}">
		<p class="alert alert-success">${ uploadDB }</p>
	</c:if>
	<c:if test="${not empty fileDownloadError}">
		<p class="alert alert-danger">${ fileDownloadError }</p>
	</c:if>
	<c:if test="${not empty downloaded}">
		<p class="alert alert-success">${ downloaded }</p>
	</c:if>
	<c:if test="${not empty NotDownloaded}">
		<p class="alert alert-danger">${ NotDownloaded }</p>
	</c:if>
	<c:if test="${not empty fileRenameSuccess}">
		<p class="alert alert-success">${ fileRenameSuccess }</p>
	</c:if>
	<c:if test="${not empty fileRenameError}">
		<p class="alert alert-danger">${ fileRenameError }</p>
	</c:if>
	<c:if test="${not empty fileRemoveError}">
		<p class="alert alert-danger">${ fileRemoveError }</p>
	</c:if>
	<c:if test="${not empty fileRemoveSuccess}">
		<p class="alert alert-success">${ fileRemoveSuccess }</p>
	</c:if>

	<br>
	<br>
	<!-- No files uploaded -->
	<c:if test="${files.rowCount == 0 && empty filename}">
		<p class="alert alert-warning">No files uploaded yet!</p>
	</c:if>

	<!-- Message regarding search-->
	<c:if test="${files.rowCount == 0 && not empty filename}">
		<p class="alert alert-danger">${searchMessage }</p>
		<a class="btn btn-outline-warning" href="UploadViewGROUP.jsp">Go
			back to my files</a>
	</c:if>

	<!-- Show the view and search field only if more then one file has been uploaded -->
	<c:if test="${files.rowCount > 0 }">
		<form action="SearchFiles">
			<div class="form-group">
				<c:if test="${ not empty nameError }">
					<p class="alert alert-danger">${ nameError }</p>
				</c:if>
				<input type="text" class="form-control"
					placeholder="Search for a file" name="search" value="${filename}">
			</div>
			<input class="btn btn-outline-success" type="submit" name="Search"
				value="Search" /></span>
		</form>


		<c:if test="${not empty searchSuccess && not empty filename}">
			<p class="alert alert-warning">${searchMessage }</p>
			<a class="btn btn-outline-danger" href="UploadViewGROUP.jsp">Go
				back to my files</a>
		</c:if>

		<br>
		<br>

		<table class="table table-bordered table-hover">
			<tr>
				<th><c:out value="Filename" /></th>
				<th><c:out value="Date of upload" /></th>
				<th><c:out value="Download" /></th>
				<th><c:out value="Rename" /></th>
				<th><c:out value="Remove" /></th>
			</tr>
			<c:forEach var="row" items="${files.rows}">
				<tr>
					<td><c:out value="${row.filename}" /></td>
					<td><c:out value="${row.upload_date}" /></td>
					<td><a class="btn btn-success"
						href="Download?fileName=${ row.filename }">Download</a></td>
						
					<td><c:if test="${row.filename == param.tempName}">
							<form action="Rename" method="get">
								<input type="hidden" name="oldname" value="${ row.filename }">
								<input type="text" class="form-control" name="newname"
									placeholder="New name with extension"> <br> 
									<input type="submit" class="btn btn-warning" name="submitBtn"
									value="Rename">
							</form>
						</c:if> 
						<c:if test="${row.filename != param.tempName}">
							<a class="btn btn-warning"
								href="UploadViewGROUP.jsp?tempName=${ row.filename }">Rename</a>
						</c:if></td>
					<td><a class="btn btn-danger"
						href="Remove?fileName=${ row.filename }&id=${row.id}">Remove</a></td>
				</tr>
			</c:forEach>
		</table>
	</c:if>
	<a class="btn btn-outline-danger" href="Logout">Logout</a>
</body>
</html>