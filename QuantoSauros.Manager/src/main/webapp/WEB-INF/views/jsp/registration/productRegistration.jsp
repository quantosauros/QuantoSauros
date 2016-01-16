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
				<spring:url value="/registration/product" var="productRegActionUrl" />
				<form:form clas="form-horizontal" method="post" name="formData" action="${productRegActionUrl}">
					<div class="form-group">
						<label class="col-sm-2 control-label"> Instrument Code </label>
						<div class="col-sm-10">
							<input type="text" class="form-control" name="instrumentCd" placeholder="Instrument Code" value="${productInfo.instrumentCd}">
						</div>
					</div>										
				</form:form>
			</div>
		</div>		
	</div>
	
	<jsp:include page="../fragments/footer.jsp" />
		
</body>
</html>