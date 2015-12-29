<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<!DOCTYPE html>
<html>
<head>    
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="author" content="JIHOON LEE">

    <title>AAD Manager</title>

   <!-- Bootstrap Core CSS -->
    <link href="resources/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="resources/bower_components/metisMenu/dist/metisMenu.min.css" rel="stylesheet">

    <!-- Timeline CSS -->
    <link href="resources/dist/css/timeline.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="resources/dist/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="resources/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    
</head>

<body>
	<div id="wrapper">
		<!-- Navigator -->
		<jsp:include page="../navigator.jsp" />	       
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
										<span><input class="form-control" type="text" id="input-procId" name="procId" value ="0"></span>
									</div>
									<div class = "list-group-item">
										<label for="idxIdInput"> Instrument Code </label>
										<span><input class="form-control" type="text" id="input-instrumentCd" name="instrumentCd" value ="APSSWAP001"></span>	
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
	
	<!-- jQuery -->
    <script src="resources/bower_components/jquery/dist/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="resources/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="resources/bower_components/metisMenu/dist/metisMenu.min.js"></script>
	
	<!-- Custom Theme JavaScript -->
    <script src="resources/dist/js/sb-admin-2.js"></script>
	
	<!-- HIGH CHARTS -->
	<script src="http://code.highcharts.com/highcharts.js"></script>
	
    <!-- java scripts -->
	<script type="text/javascript">
		function generateChart(){
			var url = "./json/priceChart?procId="
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
		};		
	</script>	
</body>
</html>