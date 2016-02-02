<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>

<jsp:include page="../fragments/header.jsp" />

<body>
	<div id="wrapper">
		<jsp:include page="../fragments/navigator.jsp" />
		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header"> Price Process </h1>
				</div>
			</div>
			<div class="row">
				<c:if test="${not empty msg}">
					<div class="alert alert-${css} alert-dismissible" role="alert">
						<button type="button" class="close" data-dismiss="alert" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<strong>${msg}</strong>
					</div>
				</c:if>
				<div class="col-lg-12">
					<div class="panel panel-primary form-group">
						<div class="panel-heading">
							<h3 class="panel-title"> Price Process Control Panel </h3>
						</div>
						<div class="panel-body">
							<spring:url value="/process/price" var="processExecuteUrl" />
							<form action="${processExecuteUrl}" method="post">
								<div class="list-group">
									<div class = "list-group-item">
										<label for="processIdInput"> Process ID </label>										
										<form:select path="processList" items="${processList}" class="form-control" id="input-procId" name="procId" />
									</div>
									<div class = "list-group-item">
										<label for="idxIdInput"> Process Start Date </label>
										<span><input class="form-control" type="date" id="input-startDate" name="startDt" value ="2013-12-02"></span>
									</div>
									<div class = "list-group-item">
										<label for="idxIdInput"> Process End Date </label>
										<span><input class="form-control" type="date" id="input-endDate" name="endDt" value ="2013-12-30"></span>
									</div>
									
									<input class="btn btn-success pull-right" type ="submit" id ="executeButton" value ='Execute' onclick="generateChart();">	
									
								</div>															
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<jsp:include page="../fragments/footer.jsp" />
	
</body>
</html>