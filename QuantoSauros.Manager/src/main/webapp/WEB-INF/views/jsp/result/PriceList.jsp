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
									<input class="form-control" type="text" id="input-procId" name="procId" value ="0">																	
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
														<th>NonCall Code</th>
														<th>Price</th>
														<th>Pay Price</th>
														<th>Rcv Price</th>
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
	<jsp:include page="../fragments/footer.jsp" />

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
			"order": [[0, "asc"], [1, "asc"], [2, "asc"]],
            ajax: ajaxStr,
            columns: [
                { data: 'processDt' },
                { data: 'instrumentCd' },
                { data: 'nonCallCd' },
                { data: 'price'},
                { data: 'payPrice'},
                { data: 'rcvPrice'},                                     
            ]
        });
    }
    function generateTable(){
		var table1 = $('#table1');		
		
		var ajaxStr = './priceTable/json?procId=' 
				+ $('#input-procId').val();
				
		createTable(table1, ajaxStr);
		$('#resultSection')[0].hidden = false;
    };		
	</script>	
</head>
	
    
</body>
</html>