<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">    
    <meta name="author" content="JIHOON LEE">

    <title>AAD Manager</title>

    <!-- Bootstrap Core CSS -->
    <link href="resources/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="resources/css/sb-admin.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="resources/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
	<!-- JQUERY -->
	<script type="text/javascript" language="javascript" src="//code.jquery.com/jquery-1.11.3.min.js"></script>
	
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
			<form action = "./calculator/execute" method = "post">
				<div class="form-date-params">
					<label for="startDateInput"> Start Date </label>
					<input type="date" id="input-start-date" name="startDt" value ="2014-09-01">
					<label for="startDateInput"> End Date </label>
					<input type="date" id="input-end-date" name="endDt" value ="2014-09-30">	
				</div>
				<div class="form-sim-params">
					<label for="simNumInput"> Simulation Number </label>
					<input = type="text" id="input-simNum" name="simNum" value ="1000">
					<label for="monitorFreqInput"> Monitor Frequency </label>
					<input = type="text" id="input-monitorFreq" name="monitorFreq" value ="1">
				</div>
				<div class="form-other-params">
					<label for="procIdInput"> Proc ID </label>
					<input = type="text" id="input-proc-id" name="procId" value ="0">
					<label for="idxIdInput"> Index ID </label>
					<input = type="text" id="input-idx-id" name="idxId" value ="0">
				</div>
				
				<input type = "submit" value ='Execute'>
			</form>
		</div>
		<!-- /#page-wrapper -->
	</div>
	<!-- /#wrapper -->
	
	 <!-- jQuery -->
    <script src="resources/js/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="resources/js/bootstrap.min.js"></script>
    
</body>
</html>