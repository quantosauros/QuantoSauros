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
							<h1 class="page-header"> Price Charts </h1>
					</div>
				</div>	
				<div class="row">
					<div class="col-lg-12">
						<div class="panel panel-primary form-group">
							<div class="panel-heading">
									<h3 class="panel-title"> Price Control Panel </h3>
								</div>
							<div class="panel-body">
								<div class="list-group">
									<div class = "list-group-item">									
										<label for="processIdInput"> Process ID </label>										
										<form:select path="processList" items="${processList}" class="form-control" id="input-procId" name="procId" />
									</div>
									<div class = "list-group-item">
										<label for="idxIdInput"> Instrument Code </label>
										<form:select path="instrumentList" items="${instrumentList}" class="form-control" id="input-instrumentCd" name="instrumentCd" />	
									</div>
									<div class = "list-group-item">
										<label for="idxIdInput"> Start Date </label>
										<span><input class="form-control" type="date" id="input-startDate" name="startDate" value ="2013-12-02"></span>
									</div>
									<div class = "list-group-item">
										<label for="idxIdInput"> End Date </label>
										<span><input class="form-control" type="date" id="input-endDate" name="endDate" value ="2013-12-30"></span>
									</div>
									<div class = "list-group-item">
										<label for="idxIdInput"> NonCall Type </label>
										<span>
											<select class="form-control" id="input-nonCallCd" name="nonCallCd">
												<option value="N"> Original </option>
												<option value="Y"> NonCall</option>
											</select>
										</span>
									</div>
									<div>
										<input class="btn btn-success" type ="submit" id ="executeButton" value ='Execute' onclick="generateChart();">	
									</div>	
								</div>								
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12" id="chartDiv" hidden="true">
						<div class="panel panel-primary form-group">
							<div id="chart_1" style="width:100%; height:400px;"></div>
						</div>
					</div>
				</div>	
			</div>
		</div>
		<!-- /#page-wrapper -->
	</div>
	<!-- /#wrapper -->	
	
	<jsp:include page="../fragments/footer.jsp" />
	
	<!-- HIGH CHARTS -->
	<script src="http://code.highcharts.com/highcharts.js"></script>
	
    <!-- java scripts -->
	<script type="text/javascript">
		function generateChart(){
			var url = "./priceChart/json?procId="
					+ $('#input-procId').val()					
					+ '&startDate=' + $('#input-startDate').val()
					+ '&endDate=' + $('#input-endDate').val()
					+ '&instrumentCd=' + $('#input-instrumentCd').val()
					+ '&nonCallCd=' + $('#input-nonCallCd').val();
			
			$.getJSON(url, function(data) {
				var chart1;				
				chart1 = new Highcharts.Chart(data);					
			});
			$('#chartDiv')[0].hidden = false;			
			location.href = "#chartDiv";
			
		};		
	</script>	
</body>
</html>