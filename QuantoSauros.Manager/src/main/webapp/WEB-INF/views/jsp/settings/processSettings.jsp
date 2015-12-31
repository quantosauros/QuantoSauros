<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<jsp:include page="../fragments/header.jsp" />
<body>
	<div id="wrapper">
		<jsp:include page="../fragments/navigator.jsp" />
		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header"> AAD Manager </h1>
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
				<table class="table table-striped">
					<thead>					
						<tr>
							<th>Process ID</th>
							<th>Process Name</th>
							<th>Scenario</th>							
							<th>Portfolio</th>							
							<th>Description</th>
							<th>Created Date</th>
						</tr>
					</thead>
					<c:forEach var="processInfo" items="${processInfo}">
						<tr>
							<td>${processInfo.procId}</td>
							<td>${processInfo.procNM}</td>
							<td>${processInfo.scenarioId}. ${processInfo.scenarioNM}</td>							
							<td>${processInfo.portfolioId}. ${processInfo.portfolioNM}</td>							
							<td>${processInfo.description}</td>
							<td>${processInfo.crtnTime}</td>
							<td>
								<spring:url value="/settings/process/${processInfo.procId}/update" var="updateUrl" />
								<spring:url value="/settings/process/${processInfo.procId}/delete" var="deleteUrl" />								
								<button class="btn btn-primary btn-xs" onclick="location.href='${updateUrl}'">
									UPDATE
								</button>
								<button class="btn btn-danger btn-xs" onclick="this.diabled=true;post('${deleteUrl}')">
									DELETE
								</button>
								
							</td>
						</tr>
					</c:forEach>
					
				</table>
				<div class="form-group">
					<spring:url value="/settings/process/add" var="urlAddProcess" />
					<button class="btn-lg btn-primary pull-right" onclick="location.href='${urlAddProcess}'">Add Process</button>
				</div>
				
			</div>
		</div>
		<!-- /#page-wrapper -->
	</div>
	<!-- /#wrapper -->
	<jsp:include page="../fragments/footer.jsp" />
	
    <script type="text/javascript">
    function post(path, params, method) {
    	method = method || "post"; 

    	var form = document.createElement("form");
    	form.setAttribute("method", method);
    	form.setAttribute("action", path);

    	for ( var key in params) {
    		if (params.hasOwnProperty(key)) {
    			var hiddenField = document.createElement("input");
    			hiddenField.setAttribute("type", "hidden");
    			hiddenField.setAttribute("name", key);
    			hiddenField.setAttribute("value", params[key]);

    			form.appendChild(hiddenField);
    		}
    	}

    	document.body.appendChild(form);
    	form.submit();
    }
    </script>
    
</body>
</html>