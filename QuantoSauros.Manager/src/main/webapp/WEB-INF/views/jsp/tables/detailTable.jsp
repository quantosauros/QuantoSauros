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
						<h1 class="page-header"> Detail Results </h1>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12">
						<div class="panel panel-primary form-group">
							<div class="panel-heading">
								<h3 class="panel-title"> Detail Control Panel </h3>
							</div>
							<div class="panel-body">
								<div class = "form-inline list-group-item">																							
									<label for="processIdInput"> Process ID </label>
									<form:select path="processList" items="${processList}" class="form-control" id="input-procId" name="procId" />																																				
								</div>															
								<div class = "form-inline list-group-item">	
									<label for="instrumentCdInput"> Instrument Code </label>
									<form:select path="instrumentList" items="${instrumentList}" class="form-control" id="input-instrumentCd" name="instrumentCd" />
								</div>
								
								<div class = "form-inline list-group-item">	
									<label for="nonCallCdInput"> NonCall </label>					
									<select class="form-control" id="input-nonCallCd" name="nonCallCd">
										<option value="N"> Original </option>
										<option value="Y"> NonCall </option>
									</select>
								</div>
								<div class = "form-inline list-group-item">	
									<label for="valueTypeInput"> Type Of Output </label>					
									<select class="form-control" id="input-valueType" name="valueType">					
										<option value="CPN_RATE"> COUPON RATE </option>
										<option value="TENOR"> TENOR </option>
										<option value="EX_IDX"> EXERCISE INDEX </option>
										<option value="REF_RATE"> REFERENCE RATE </option>
										<option value="DISC_RATE"> DISCOUNT RATE</option>
									</select>	
								</div>
								<div>
									<input class="btn btn-success" type ="submit" id ="executeButton" value ='Execute' onclick="generateTable();">	
								</div>	
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12" id="resultSectionPay" hidden="true">
						<div class="panel panel-primary form-group">
							<div class="panel-heading">
								Pay Leg Result
							</div>
							<div class="panel-body">
								<div class="dataTable_wrapper">
									<table id="tablePay" class="table table-striped table-bordered table-hover dataTable no-footer">
										<thead>
											<tr>
												<th>Process Date</th>	
												<th>Instrument Code</th>
												<th>Period Number</th>
												<th>Average</th>
												<th>Standard Deviation</th>		
											</tr>
										</thead>
									</table>											
								</div>
							</div>							
						</div>
					</div>
					<div class="col-lg-12" id="resultSectionRcv" hidden="true">
						<div class="panel panel-primary form-group">
							<div class="panel-heading">
								RCV Leg Result
							</div>
							<div class="panel-body">
								<div class="dataTable_wrapper">
									<table id="tableRcv" class="table table-striped table-bordered table-hover dataTable no-footer">
										<thead>
											<tr>
												<th>Process Date</th>	
												<th>Instrument Code</th>
												<th>Period Number</th>
												<th>Average</th>
												<th>Standard Deviation</th>		
											</tr>
										</thead>
									</table>
								</div>
							</div>							
						</div>
					</div>
					<div class="col-lg-12" id="resultSectionEtc" hidden="true">
						<div class="panel panel-primary form-group">
							<div class="panel-heading">
								Result
							</div>
							<div class="panel-body">
								<div class="dataTable_wrapper">
									<table id="tableEtc" class="table table-striped table-bordered table-hover dataTable no-footer">
										<thead>
											<tr>
												<th>Process Date</th>	
												<th>Instrument Code</th>
												<th>Period Number</th>
												<th>Average</th>
												<th>Standard Deviation</th>		
											</tr>
										</thead>
									</table>
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
    <spring:url value="/resources/bower_components/datatables/media/js/jquery.dataTables.min.js" var="dataTableJs" />
    <spring:url value="/resources/bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.min.js" var="dataTablesBootstrap" />
        
    <script src="${dataTableJs}"></script>
    <script src="${dataTablesBootstrap}"></script>
	<script src="//cdn.datatables.net/buttons/1.1.0/js/dataTables.buttons.min.js"></script>
	<script src="//cdnjs.cloudflare.com/ajax/libs/jszip/2.5.0/jszip.min.js"></script>	
	<script src="//cdn.datatables.net/buttons/1.1.0/js/buttons.html5.min.js"></script>
	<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/buttons/1.1.0/css/buttons.dataTables.min.css">

	<!-- java scripts -->
    <script type ="text/javascript">
    function createTable(tableName, ajaxStr){
    	
    	//tableVariable.DataTable({
    	$(tableName).DataTable({
			dom: 'Bfrtip',
			buttons: [
	            'copy',
	            'excel'		            
    		],
    		pageLength: 30,
			order: [[0, "asc"],[2, "asc"]],
            ajax: ajaxStr,
            columns: [
                { data: 'processDt'},
                { data: 'instrumentCd'},
                { data: 'periodNum'},
                { data: 'average'},
                { data: 'std'},                                            
            ]
        });
    }
    function generateTable(){
    	var valueType = $('#input-valueType').val();    	    	
    	if (valueType == "EX_IDX" || valueType == "DISC_RATE"){
    		
    		//var tableEtc = $('#tableEtc');
    		var tableEtc = $('#tableEtc').DataTable();
    		var ajaxStr = '../tables/detail/json?instrumentCd=' + $('#input-instrumentCd').val() 
				+ '&procId=' + $('#input-procId').val()				
				+ '&valueType=' + $('#input-valueType').val()
				+ '&nonCallCd=' + $('#input-nonCallCd').val()
				+ '&legType=';
    		
    		if (tableEtc) tableEtc.destroy();
    		
    		tableEtc = $('#tableEtc').DataTable({
    			dom: 'Bfrtip',
    			buttons: [
    	            'copy',
    	            'excel'		            
        		],
        		pageLength: 30,
    			order: [[0, "asc"],[2, "asc"]],
                ajax: ajaxStr,
                responsive: true,
                columns: [
                    { data: 'processDt'},
                    { data: 'instrumentCd'},
                    { data: 'periodNum'},
                    { data: 'average'},
                    { data: 'std'},                                            
                ]
            });
    		    		
    		var hiddenSection1 = "#resultSectionPay";
    		var hiddenSection2 = "#resultSectionRcv";
    		var noHiddenSection = "#resultSectionEtc";
    		$(hiddenSection1)[0].hidden = true;
    		$(hiddenSection2)[0].hidden = true;
    		$(noHiddenSection)[0].hidden = false;
    		
    	} else {
    		var tablePay = $('#tablePay').DataTable();		
        	var tableRcv = $('#tableRcv').DataTable();
        	
        	var ajaxStr1 = '../tables/detail/json?instrumentCd=' + $('#input-instrumentCd').val() 
				+ '&procId=' + $('#input-procId').val()				
				+ '&valueType=' + $('#input-valueType').val()
				+ '&legType=P' 
				+ '&nonCallCd=' + $('#input-nonCallCd').val();
        	var ajaxStr2 = '../tables/detail/json?instrumentCd=' + $('#input-instrumentCd').val() 
				+ '&procId=' + $('#input-procId').val()				
				+ '&valueType=' + $('#input-valueType').val()
				+ '&legType=R' 
				+ '&nonCallCd=' + $('#input-nonCallCd').val();
        	
        	if(tablePay) tablePay.destroy();
        	if(tableRcv) tableRcv.destroy();
        	
        	tablePay = $('#tablePay').DataTable({
    			dom: 'Bfrtip',
    			buttons: [
    	            'copy',
    	            'excel'		            
        		],
        		pageLength: 30,
    			order: [[0, "asc"],[2, "asc"]],
                ajax: ajaxStr1,
                responsive: true,
                columns: [
                    { data: 'processDt'},
                    { data: 'instrumentCd'},
                    { data: 'periodNum'},
                    { data: 'average'},
                    { data: 'std'},                                            
                ]
            });
        	
        	tableRcv = $('#tableRcv').DataTable({
    			dom: 'Bfrtip',
    			buttons: [
    	            'copy',
    	            'excel'		            
        		],
        		pageLength: 30,
    			order: [[0, "asc"],[2, "asc"]],
                ajax: ajaxStr2,
                responsive: true,
                columns: [
                    { data: 'processDt'},
                    { data: 'instrumentCd'},
                    { data: 'periodNum'},
                    { data: 'average'},
                    { data: 'std'},                                            
                ]
            });
        	
        	var hiddenSection = "#resultSectionEtc";
        	var noHiddenSection1 = "#resultSectionPay";
        	var noHiddenSection2 = "#resultSectionRcv";
    		$(hiddenSection)[0].hidden = true;
    		$(noHiddenSection1)[0].hidden = false;
    		$(noHiddenSection2)[0].hidden = false;
    	}		
    };		
	</script>	    
</body>
</html>