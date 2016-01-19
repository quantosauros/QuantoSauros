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
					<h1 class="page-header"> Product Registration </h1>
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
				<spring:url value="/register" var="productRegActionUrl" />
				<form:form clas="form-horizontal" method="post" name="formData" action="${productRegActionUrl}">
					<div class="form-group">
						<label class="col-sm-2 control-label"> Instrument Code </label>
						<div class="col-sm-10">
							<input type="text" class="form-control" name="instrumentCd" placeholder="Instrument Code" value="${productInfoModel.instrumentCd}">							
						</div>
					</div>										
					<div class="form-group">
						<label class="col-sm-2 control-label"> Issue Date </label>
						<div class="col-sm-10">
							<input type="text" class="form-control" name="issueDt" placeholder="Issue Date" value="${productInfoModel.issueDt}">							
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"> Maturity Date </label>
						<div class="col-sm-10">
							<input type="text" class="form-control" name="mrtyDt" placeholder="Maturity Date" value="${productInfoModel.mrtyDt}">							
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"> Currency Code </label>
						<div class="col-sm-10">
							<input type="text" class="form-control" name="ccyCd" placeholder="Currency Code" value="${productInfoModel.ccyCd}">							
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"> Principal Exchange </label>
						<div class="col-sm-10">
							<input type="text" class="form-control" name="principalExchCd" placeholder="Principal Exchange Code" value="${productInfoModel.principalExchCd}">							
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"> Option Type </label>
						<div class="col-sm-10">
							<input type="text" class="form-control" name="optionTypeCd" placeholder="Option Type Code" value="${productInfoModel.optionTypeCd}">							
						</div>
					</div>
					<button type="submit" class="btn-lg btn-primary pull-right"> SUBMIT </button>
				</form:form>
			</div>
		</div>		
	</div>
	
	<jsp:include page="../fragments/footer.jsp" />
		
</body>
</html>