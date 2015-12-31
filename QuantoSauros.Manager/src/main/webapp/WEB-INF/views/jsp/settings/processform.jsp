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
					<h1 class="page-header"> Process Registration </h1>
				</div>
			</div>
			<div class="row">			
				<c:choose>
					<c:when test="${processInfo['new']}">
						<h1>Add Process</h1>
					</c:when>
					<c:otherwise>
						<h1>Update Process</h1>
					</c:otherwise>
				</c:choose>
				<br />
			
				<spring:url value="/settings/process" var="processActionUrl" />
				<form:form class="form-horizontal" method="post" modelAttribute="processInfo" action="${processActionUrl}">

					<form:hidden path="procId" />
					
					<spring:bind path="scenarioId">
						<div class="form-group">
							<label class="col-sm-2 control-label"> Scenario ID </label>
							<div class="col-sm-5">
								<form:select path="scenarioId" class="form-control">									
									<form:options items="${scenarioList}" />
								</form:select>								
							</div>
						</div>
					</spring:bind>
					
					<spring:bind path="portfolioId">
						<div class="form-group">
							<label class="col-sm-2 control-label"> Portfolio ID </label>
							<div class="col-sm-10">
								<form:input path="portfolioId" type="text" class="form-control" id="portfolioId" placeholder="Portfolio ID" />
								<form:errors path="portfolioId" class="control-label" />
							</div>
						</div>
					</spring:bind>
					
					<spring:bind path="procNM">
						<div class="form-group">
							<label class="col-sm-2 control-label"> Process Name </label>
							<div class="col-sm-10">
								<form:input path="procNM" type="text" class="form-control" id="procNM" placeholder="Process Name" />
								<form:errors path="procNM" class="control-label" />
							</div>
						</div>
					</spring:bind>
					
					<spring:bind path="description">
						<div class="form-group">
							<label class="col-sm-2 control-label"> Description </label>
							<div class="col-sm-10">
								<form:input path="description" type="text" class="form-control" id="description" placeholder="Description" />
								<form:errors path="description" class="control-label" />
							</div>
						</div>
					</spring:bind>
					
					<form:hidden path="crtnTime" />
					
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<c:choose>
								<c:when test="${processInfo['new']}">
									<button type="submit" class="btn-lg btn-primary pull-right">Add</button>
								</c:when>
								<c:otherwise>
									<button type="submit" class="btn-lg btn-primary pull-right">Update</button>
								</c:otherwise>
							</c:choose>
						</div>
					</div> 
					
					
				</form:form>
			
			</div>
		</div><!-- /#page-wrapper -->
	</div>
	<!-- /#wrapper -->
	<jsp:include page="../fragments/footer.jsp" />
    
</body>
</html>