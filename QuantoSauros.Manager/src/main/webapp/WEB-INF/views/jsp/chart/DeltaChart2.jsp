<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
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
							<h1 class="page-header"> Delta Charts 2 </h1>
					</div>
				</div>	
				<div class="row">
					<div class="col-lg-12">
						<div class="panel panel-primary form-group">
							<div class="panel-heading">
								<h3 class="panel-title"> Delta Control Panel </h3>
							</div>
							<div class="panel-body">
								<div class="list-group">
									<div class = "list-group-item">
										<label for="processIdInput"> Process ID </label>
										<span><input class="form-control" type="text" id="input-procId" name="procId" value ="0"></span>
									</div>
									<div class = "list-group-item">
										<label for="idxIdInput"> Instrument Code </label>
										<span><input class="form-control" type="text" id="input-instrumentCd" name="instrumentCd" value ="APSSWAP001"></span>	
									</div>
									<div class = "list-group-item">
										<label for="idxIdInput"> Greek Code </label>
										<span>
											<select class="form-control" id = "input-greekCd" name="greekCd">
												<option value = "AAD"> AAD </option>
												<option value = "BUMP"> BUMP </option>											
											</select>
										</span>									
									</div>
									<div class = "list-group-item">
										<label for="idxIdInput"> IR Code </label>
										<span>
											<select class="form-control" id = "input-ircCd" name="ircCd">										
												<option value = "KRWIRS" selected> KRW IRS </option>
												<option value = "USDIRS"> USD IRS </option>
												<option value = "EURIRS"> EUR IRS </option>
												<option value = "1010000W"> KRW TBOND </option>
											</select>
										</span>
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
			var url = './deltaChart2/json?'
					+ 'procId='	+ $('#input-procId').val()					
					+ '&startDate=' + $('#input-startDate').val()
					+ '&endDate=' + $('#input-endDate').val()
					+ '&instrumentCd=' + $('#input-instrumentCd').val()
					+ '&greekCd=' + $('#input-greekCd').val()
					+ '&ircCd=' + $('#input-ircCd').val()
					+ '&nonCallCd=' + $('#input-nonCallCd').val()
					;
			
			$.getJSON(url, function(data) {
				var chart1;				
				chart1 = new Highcharts.Chart(data);					
			});
			$('#chartDiv')[0].hidden = false;
		};		
	</script>	
</body>
</html>