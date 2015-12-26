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
						<h1 class="page-header"> Price Results </h1>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12">
						<div class="panel panel-primary form-group">
							<div class="panel-heading">
								<h3 class="panel-title"> Price Control Panel </h3>
							</div>
							<div class="panel-body">															
								<div class = "form-inline list-group-item">																							
									<label for="processIdInput"> Process ID </label>
									<input class="form-control" type="text" id="input-procId" name="procId" value ="101">																	
								</div>
								<div class = "form-inline list-group-item">
									<label for="idxIdInput"> Index ID </label>
									<input class="form-control" type="text" id="input-idxId" name="idxId" value ="101">
								</div>
								<div>
									<input class="btn btn-success" type ="submit" id ="executeButton" value ='Execute' onclick="generateTable();">	
								</div>								
							</div>							
						</div>											
					</div>
				</div>
				<div class="row">	
					<div class="col-lg-12" id="resultSection" hidden="true">
						<div class="panel panel-primary form-group">
							<div class="panel-heading">
								Result
							</div>
							<div class="panel-body">
								<div class="dataTable_wrapper">
									<div class="dataTables_wrapper form-inline dt-bootstrap no-footer">
										<div class="row">										
											<div class="col-sm-12">
												<table id="table1" class="table table-striped table-bordered table-hover dataTable no-footer">
												<thead>
													<tr>
														<th>Process Date</th>
														<th>Instrument Code</th>						
														<th>Price</th>
														<th>Pay Price</th>
														<th>Rcv Price</th>
														<th>NonCall Price</th>
													</tr>
												</thead>
											</table>																																			</div>			
										</div>
									</div>
								</div>			
							</div>							
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

    <!-- DataTables JavaScript -->
    <script src="resources/bower_components/datatables/media/js/jquery.dataTables.min.js"></script>
    <script src="resources/bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.min.js"></script>
	<script type="text/javascript" src="https://cdn.datatables.net/buttons/1.1.0/js/dataTables.buttons.min.js"></script>
	<script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/jszip/2.5.0/jszip.min.js"></script>	
	<script type="text/javascript" src="//cdn.datatables.net/buttons/1.1.0/js/buttons.html5.min.js"></script>
	<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/buttons/1.1.0/css/buttons.dataTables.min.css">
	
    <!-- Custom Theme JavaScript -->
    <script src="resources/dist/js/sb-admin-2.js"></script>
	
	<!-- java scripts -->
    <script type ="text/javascript">
    function createTable(tableVariable, ajaxStr){    	
    	tableVariable.DataTable({
			dom: 'Bfrtip',
			buttons: [
	            'copy',
	            'excel'		            
    		],
    		pageLength: 30,
			"order": [[1, "asc"]],
            ajax: ajaxStr,
            columns: [
                { data: 'processDt' },
                { data: 'instrumentCd' },
                { data: 'price'},
                { data: 'payPrice'},
                { data: 'rcvPrice'},
                { data: 'nonCallPrice'}                                          
            ]
        });
    }
    function generateTable(){
		var table1 = $('#table1');		
		
		var ajaxStr = './json/pricelist?procId=' 
				+ $('#input-procId').val() 
				+ '&idxId=' + $('#input-idxId').val();
		createTable(table1, ajaxStr);
		$('#resultSection')[0].hidden = false;
    };		
	</script>	
</head>
	
    
</body>
</html>