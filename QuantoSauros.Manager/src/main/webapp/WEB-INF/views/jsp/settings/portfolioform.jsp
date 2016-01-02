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
					<h1 class="page-header"> Portfolio Registration </h1>
				</div>
			</div>
			<div class="row">			
				<c:choose>
					<c:when test="${portfolioInfo['new']}">
						<h1>Add Portfolio</h1>
					</c:when>
					<c:otherwise>
						<h1>Update Portfolio</h1>
					</c:otherwise>
				</c:choose>
				<br />
							
				<spring:url value="/settings/portfolio" var="portfolioActionUrl" />								
				<form:form class="form-horizontal" method="post" name="formData" modelAttribute="portfolioDataForm"  action="${portfolioActionUrl}">
					<input type="hidden" name="portfolioId" value="${portfolioInfo.portfolioId}">
					
					<div class="form-group">
						<label class="col-sm-2 control-label"> Portfolio Name </label>
						<div class="col-sm-10">
							<input type="text" class="form-control" name="portfolioNM" placeholder="Portfolio Name" value="${portfolioInfo.portfolioNM}">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"> Description </label>
						<div class="col-sm-10">
							<input type="text" class="form-control" name="description" placeholder="Description" value="${portfolioInfo.description}">
						</div>
					</div>

					<table class="table table-striped">
						<thead>
							<tr>
								<th><input type="checkbox" onClick="toggle(this)"></th>
								<th>Instrument Code</th>
								<th>Pay Leg Type</th>
								<th>Rcv Leg Type</th>
								<th>Issue Date</th>
								<th>Maturity Date</th>
								<th>Currency Code</th>
								<th>Option Type</th>							
							</tr>
						</thead>					
						<c:forEach var="portfolioData" items="${portfolioDataForm.portfolioDatas}" varStatus="status">
						<tr>	
							<td>				
								<c:choose>																
									<c:when test="${portfolioData.flag eq 'Y'}">										
										<!-- <input type="checkbox" name="flagCheckBox" checked> -->
										<input type="checkbox" id="dataCheckBox" name="portfolioDatas[${status.index}].flag" value="Y" checked>										
									</c:when>
									<c:otherwise>										
										<input type="checkbox" id="dataCheckBox" name="portfolioDatas[${status.index}].flag" value="Y">									
									</c:otherwise>									
								</c:choose>							
							</td>
							<td>
								<input type="text" class="form-control" name="portfolioDatas[${status.index}].instrumentCd" placeholder="Instrument Code" value="${portfolioData.instrumentCd}"/>
							</td>							
							<td>${portfolioData.rcvLegTypeCd}</td>
							<td>${portfolioData.payLegTypeCd}</td> 
							<td>${portfolioData.issueDt}</td>
							<td>${portfolioData.mrtyDt}</td>
							<td>${portfolioData.ccyCd}</td>
							<td>${portfolioData.optionTypeCd}</td>							
						</tr>						
						</c:forEach>					
					</table>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<c:choose>
								<c:when test="${portfolioInfo['new']}">
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
    
    <script type="text/javascript">
    function toggle(source){    	
    	//checkboxes = document.getElementsByName("flagCheckBox");
    	var checkboxes = document.getElementsByTagName('input');
    	for (var i=0, n=checkboxes.length; i <n;i++){
    		if (checkboxes[i].type == 'checkbox'){
    			checkboxes[i].checked = source.checked;	
    		}    		
    	}
    }    
    </script>    
</body>
</html>