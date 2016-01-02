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
		<!-- Navigator -->
		<jsp:include page="../fragments/navigator.jsp" />	       
		<div id="page-wrapper">
			<div id="container-fluid">
				<div class="row">
					<div class="col-lg-12">
							<h1 class="page-header"> Pricer </h1>
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
								<h3 class="panel-title"> Pricer Control Panel </h3>
							</div>
							<div class="panel-body">
								<spring:url value="/pricer/list" var="pricerExecuteUrl" />
								<form action="${pricerExecuteUrl}" method="get">								
									<div class="list-group">
										<div class = "list-group-item form-group">
											<label> Instrument Code </label>												
											<%-- <form:select path="instrumentList" items="${instrumentList}" class="form-control" id="input-instrumentCd" name="instrumentCd" value="${selectedInstrumentCd}" /> --%>											
											<select class="form-control" name="instrumentCd">
												<c:forEach items="${instrumentList}" var="list">													
													<c:choose>													
														<c:when test="${list.value == selectedInstrumentCd}">
															<option value="${list.value}" selected> ${list.value}</option>
														</c:when>													
														<c:otherwise>
															<option value="${list.value}"> ${list.value}</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
										</div>
										<div class = "list-group-item form-group">
											<label> Process Date </label>												
											<input class="form-control" type="date" name="processDt" value="${processDt}">
										</div>
									</div>
									<div>
										<input class="btn btn-success" type ="submit" id ="executeButton" value ='Execute'>	
									</div>									
								</form>								
							</div>						
						</div>						
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12">
						<div class="panel panel-primary form-group">
							<div class="panel-heading">
								<h3 class="panel-title"> Pricer Result Panel </h3>
							</div>
							<div class="panel-body">
								<div class="list-group">
									<div class = "list-group-item">
										<label> Product Price </label>
										${resultPrice}		
									</div>
									<div class = "list-group-item">
										<label> Leg Price 1 </label>
										${resultLegPrice0}		
									</div>
									<div class = "list-group-item">
										<label> Leg Price 2 </label>
										${resultLegPrice1}		
									</div>
								</div>
								<spring:url value="/pricer" var="pricerActionUrl" />
								<form:form class="form-horizontal" method="post" name="formData" action="${pricerActionUrl}">
									<div class="form-group">
										<input type="text" class="form-control" name="issueDt" value="${productInfoModel.issueDt}">
									</div>
									<div class="form-group">
										<input type="text" class="form-control" name="issueDt" value="${productInfoModel.mrtyDt}">
									</div>
									<div class="form-group">
										<input type="text" class="form-control" name="issueDt" value="${productInfoModel.hasPrincipalExchange}">
									</div>
									<div class="form-group">
										<input type="text" class="form-control" name="issueDt" value="${productInfoModel.currency}">
									</div>		
									
									<button type="submit" class="btn-lg btn-primary pull-right">Add</button>
																	
								</form:form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- /#page-wrapper -->
	</div>
	<!-- /#wrapper -->	
	
	<jsp:include page="../fragments/footer.jsp" />
	
</body>
</html>