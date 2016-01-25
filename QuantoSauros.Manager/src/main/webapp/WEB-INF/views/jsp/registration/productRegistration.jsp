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
			<div id="container-fluid">
				<div class="row">
					<div class="col-lg-12">
						<h1 class="page-header"> Product Registration </h1>
					</div>
				</div>
				<div class="row">
					<form class="form-horizontal" method="post" action="${productRegActionUrl}">
						<div class="col-lg-12">
							<c:if test="${not empty msg}">
								<div class="alert alert-${css} alert-dismissible" role="alert">
									<button type="button" class="close" data-dismiss="alert" aria-label="Close">
										<span aria-hidden="true">&times;</span>
									</button>
									<strong>${msg}</strong>
								</div>
							</c:if>
							<spring:url value="/register" var="productRegActionUrl" />
							<button type="submit" class="btn btn-success pull-right"> SUBMIT </button>						
							<ul class="nav nav-pills">
								<li class="active">
									<a href="#productInfoTab" data-toggle="tab" aria-expanded="true">BASIC</a>
								</li>
								<li>
									<a href="#legInfoTab" data-toggle="tab" aria-expanded="true">LEG</a>
								</li>																					
								<li>								
									<a href="#exerciseInfoTab" data-toggle="tab" aria-expanded="true">EXERCISE</a>								
								</li>							
							</ul>					
						</div>
						<div class="tab-content">
							<div class="tab-pane fade active in" id="productInfoTab">
								<div class="col-lg-12">							
									<div class="panel panel-primary form-group">
										<div class="panel-heading">
											<h3 class="panel-title"> Product Information </h3>
										</div>
										<div class="panel-body">
											<div class="table-responsive">
												<table class="table table-striped">
													<tbody>
														<tr>
															<td><label class="control-label"> Instrument Code </label></td>
															<td>
																<spring:bind path="productModel.productInfoModel.instrumentCd">				
																	<input type="text" class="form-control col-sm-6" name="${status.expression}" placeholder="Instrument Code" id="${status.expression}">
																</spring:bind>
															</td>
														</tr>														
														<tr>														
															<td><label class="control-label"> Issue Date </label></td>
															<td>
																<spring:bind path="productModel.productInfoModel.issueDt">
																	<input type="date" class="form-control" name="${status.expression}" id="issueDt" placeholder="Issue Date" value="2014-12-02">
																</spring:bind>	
															</td>															
														</tr>
														<tr>														
															<td><label class="control-label"> Maturity Date </label></td>
															<td>
																<spring:bind path="productModel.productInfoModel.mrtyDt">
																	<input type="date" class="form-control" name="${status.expression}" id="mrtyDt" placeholder="Maturity Date" value="2029-12-03">
																</spring:bind>
															</td>															
														</tr>
														<tr>														
															<td><label class="control-label"> Currency Code </label></td>
															<td>
																<spring:bind path="productModel.productInfoModel.ccyCd">
																	<select class="form-control" name="${status.expression}" id="${status.expression}">
																		<option value="KRW"> KRW </option>
																		<option value="USD"> USD </option>
																		<option value="EUR"> EUR </option>
																	</select>			
																</spring:bind>	
															</td>															
														</tr>
														<tr>														
															<td><label class="control-label"> Principal Exchange </label></td>
															<td>
																<spring:bind path="productModel.productInfoModel.principalExchCd">
																	<select class="form-control" name="${status.expression}" id="${status.expression}">
																		<option value="Y"> Exchange </option>
																		<option value="N"> NonExchange </option>
																	</select>							
																</spring:bind>
															</td>															
														</tr>
														<tr>														
															<td><label class="control-label"> Option Type </label></td>
															<td>
																<spring:bind path="productModel.productInfoModel.optionTypeCd">
																	<select class="form-control" name="${status.expression}" id="${status.expression}">
																		<option value="NONE"> None </option>
																		<option value="C"> Call </option>
																		<option value="P"> Put </option>
																	</select>													
																</spring:bind>
															</td>															
														</tr>
													</tbody>
												</table>	
											</div>	
										</div>								
									</div>
								</div>
							</div>
							<div class="tab-pane fade" id="legInfoTab">
								<div class="col-lg-12">
									<div class="panel panel-primary form-group">
										<div class="panel-heading">
											<h3 class="panel-title"> Pay Leg </h3>
										</div>
										<div class="panel-body">
											<div class="table-responsive">
												<table class="table table-striped">
													<thead>
														<tr>
															<th style="width:14%">  </th>
															<th style="width:43%"> Pay Leg </th>
															<th style="width:43%"> Receive Leg </th>
														</tr>
													</thead>
													<tbody>
														<form:hidden path="productModel.productLegModels[0].payRcvCd" value="P" />
														<form:hidden path="productModel.productLegModels[1].payRcvCd" value="R" />
														<tr>
															<td><label class="control-label"> Leg Type Code </label></td>
															<td>
																<spring:bind path="productModel.productLegModels[0].legTypeCd">				
																	<input type="text" class="form-control" name="${status.expression}" placeholder="Leg Type Code" id="${status.expression}">											
																</spring:bind>
															</td>
															<td>
																<spring:bind path="productModel.productLegModels[1].legTypeCd">				
																	<input type="text" class="form-control" name="${status.expression}" placeholder="Leg Type Code" id="${status.expression}">											
																</spring:bind>
															</td>															
														</tr>
														<tr>
															<td>
																<label class="control-label"> Notional </label>
															</td>
															<td>
																<spring:bind path="productModel.productLegModels[0].ccyCd">				
																	<select class="form-control" name="${status.expression}" id="${status.expression}" style="width:25%; display:inline">
																		<option value="KRW">KRW</option>
																		<option value="USD">USD</option>
																		<option value="EUR">EUR</option>
																	</select>
																</spring:bind>															
																<spring:bind path="productModel.productLegModels[0].notionalPrincipal">				
																	<input type="number" class="form-control" style="width:70%; display:inline" name="${status.expression}" placeholder="Pay Notional Principal" id="${status.expression}" value="10000000000">											
																</spring:bind>
															</td>
															<td>
																<spring:bind path="productModel.productLegModels[1].ccyCd">				
																	<select class="form-control" name="${status.expression}" id="${status.expression}" style="width:25%; display:inline">
																		<option value="KRW">KRW</option>
																		<option value="USD">USD</option>
																		<option value="EUR">EUR</option>
																	</select>
																</spring:bind>															
																<spring:bind path="productModel.productLegModels[1].notionalPrincipal">				
																	<input type="number" class="form-control" style="width:70%; display:inline" name="${status.expression}" placeholder="Pay Notional Principal" id="${status.expression}" value="10000000000">											
																</spring:bind>
															</td>
														</tr>														
														<tr>
															<td><label class="control-label"> DCF </label></td>
															<td>
																<spring:bind path="productModel.productLegModels[0].dayCountConvention">				
																	<select class="form-control" name="${status.expression}" id="${status.expression}">
																		<option value = "1"> ACTUAL/365 </option>
																		<option value = "2"> ACTUAL/360 </option>
																		<option value = "3"> ACTUAL/ACTUAL </option>
																		<option value = "4"> 30/360</option>
																		<option value = "5"> 30E/360</option>
																	</select>									
																</spring:bind>
															</td>
															<td>
																<spring:bind path="productModel.productLegModels[1].dayCountConvention">				
																	<select class="form-control" name="${status.expression}" id="${status.expression}">
																		<option value = "1"> ACTUAL/365 </option>
																		<option value = "2"> ACTUAL/360 </option>
																		<option value = "3"> ACTUAL/ACTUAL </option>
																		<option value = "4"> 30/360</option>
																		<option value = "5"> 30E/360</option>
																	</select>									
																</spring:bind>
															</td>
														</tr>
														<tr>
															<td><label class="control-label"> Underlying </label></td>
															<td>
																<spring:bind path="productModel.productLegModels[0].underlyingType">				
																	<select class="form-control" name="${status.expression}" id="payUndType" onchange="chgCondiType(this);">
																		<option value ="0"> None </option>
																		<option value ="1"> R1 </option>
																		<option value ="2"> R1 - R2 </option> 
																		<option value ="3"> R1 & R2 </option>
																		<option value ="4"> R1 & (R2 - R3) </option>
																	</select>									
																</spring:bind>
															</td>
															<td>
																<spring:bind path="productModel.productLegModels[1].underlyingType">				
																	<select class="form-control" name="${status.expression}" id="rcvUndType" onchange="chgCondiType(this);">
																		<option value ="0"> None </option>
																		<option value ="1"> R1 </option>
																		<option value ="2"> R1 - R2 </option> 
																		<option value ="3"> R1 & R2 </option>
																		<option value ="4"> R1 & (R2 - R3) </option>
																	</select>									
																</spring:bind>
															</td>
														</tr>
														<tr>
															<td><label class="control-label"> Condition Type </label></td>
															<td>
																<spring:bind path="productModel.productLegModels[0].conditionType">				
																	<select class="form-control" name="${status.expression}" id="payConditionType">
																		<option value ="0"> None </option>
																		<option value ="1" disabled> R1 </option>
																		<option value ="2" disabled> R2 </option> 
																		<option value ="3" disabled> R3 </option>
																		<option value ="4" disabled> R1 - R2 </option>			
																		<option value ="5" disabled> R2 - R3 </option>
																		<option value ="6" disabled> R1 and R2 </option>
																		<option value ="7" disabled> R1 and R3 </option>
																		<option value ="8" disabled> R2 and R3 </option>
																		<option value ="9" disabled> R1 and R2 - R3 </option>
																	</select>									
																</spring:bind>
															</td>
															<td>
																<spring:bind path="productModel.productLegModels[1].conditionType">				
																	<select class="form-control" name="${status.expression}" id="rcvConditionType">
																		<option value ="0"> None </option>
																		<option value ="1" disabled> R1 </option>
																		<option value ="2" disabled> R2 </option> 
																		<option value ="3" disabled> R3 </option>
																		<option value ="4" disabled> R1 - R2 </option>			
																		<option value ="5" disabled> R2 - R3 </option>
																		<option value ="6" disabled> R1 and R2 </option>
																		<option value ="7" disabled> R1 and R3 </option>
																		<option value ="8" disabled> R2 and R3 </option>
																		<option value ="9" disabled> R1 and R2 - R3 </option>
																	</select>									
																</spring:bind>
															</td>
														</tr>
														<tr>
															<td><label class="control-label"> Cap/Floor Type </label></td>
															<td>
																<spring:bind path="productModel.productLegModels[0].capFloorCd">				
																	<select class="form-control" name="${status.expression}" id="payCapFloorCd">
																		<option value = "0"> None </option>
																		<option value = "1"> Cap Only </option>
																		<option value = "2"> Floor Only </option>
																		<option value = "3"> Cap & Floor </option>
																	</select>									
																</spring:bind>
															</td>
															<td>
																<spring:bind path="productModel.productLegModels[1].capFloorCd">				
																	<select class="form-control" name="${status.expression}" id="rcvCapFloorCd">
																		<option value = "0"> None </option>
																		<option value = "1"> Cap Only </option>
																		<option value = "2"> Floor Only </option>
																		<option value = "3"> Cap & Floor </option>
																	</select>									
																</spring:bind>
															</td>
														</tr>
														<tr>
															<td><label class="control-label"> IR Code 1 </label></td>
															<td id="payIrInfo1">
																<spring:bind path="productModel.productLegModels[0].couponIrcCd1">				
																	<select class="form-control" name="${status.expression}" disabled style="width:25%; display:inline">
																		<option value = "NONE"> None </option>
																		<option value = "KRWIRS" selected> KRWIRS </option>
																		<option value = "USDIRS"> USDIRS </option>
																		<option value = "EURIRS"> EURIRS </option>
																		<option value = "1010000W"> KRW TBond </option>
																	</select>									
																</spring:bind>
																<spring:bind path="productModel.productLegModels[0].couponIrcMrtyCd1">				
																	<input type="text" class="form-control" name="${status.expression}" disabled value="M3" style="width:15%; display:inline">							
																</spring:bind>
																<spring:bind path="productModel.productLegModels[0].couponIrcTypeCd1">				
																	<select class="form-control" name="${status.expression}" disabled style="width:25%; display:inline">
																		<option value = "SPOT"> ZERO </option>
																		<option value = "SWAP"> SWAP </option>
																		<option value = "RMS"> RMS </option>
																	</select>								
																</spring:bind>
																<spring:bind path="productModel.productLegModels[0].couponIrcCouponFreqCd1">				
																	<select class="form-control" name="${status.expression}" disabled style="width:30%; display:inline">
																		<option value = "Q"> Quarterly </option>
																		<option value = "S"> Semi-Annual </option>
																		<option value = "A"> Annual </option>
																	</select>								
																</spring:bind>
															</td>
															<td id="rcvIrInfo2">
																<spring:bind path="productModel.productLegModels[1].couponIrcCd1">				
																	<select class="form-control" name="${status.expression}" disabled style="width:25%; display:inline">
																		<option value = "NONE"> None </option>
																		<option value = "KRWIRS" selected> KRWIRS </option>
																		<option value = "USDIRS"> USDIRS </option>
																		<option value = "EURIRS"> EURIRS </option>
																		<option value = "1010000W"> KRW TBond </option>
																	</select>									
																</spring:bind>
																<spring:bind path="productModel.productLegModels[1].couponIrcMrtyCd1">				
																	<input type="text" class="form-control" name="${status.expression}" disabled value="M3" style="width:15%; display:inline">							
																</spring:bind>
																<spring:bind path="productModel.productLegModels[1].couponIrcTypeCd1">				
																	<select class="form-control" name="${status.expression}" disabled style="width:25%; display:inline">
																		<option value = "SPOT"> ZERO </option>
																		<option value = "SWAP"> SWAP </option>
																		<option value = "RMS"> RMS </option>
																	</select>								
																</spring:bind>
																<spring:bind path="productModel.productLegModels[1].couponIrcCouponFreqCd1">				
																	<select class="form-control" name="${status.expression}" disabled style="width:30%; display:inline">
																		<option value = "Q"> Quarterly </option>
																		<option value = "S"> Semi-Annual </option>
																		<option value = "A"> Annual </option>
																	</select>								
																</spring:bind>
															</td>
														</tr>
														<tr>
															<td><label class="control-label"> IR Code 2 </label></td>
															<td id="payIrInfo2">
																<spring:bind path="productModel.productLegModels[0].couponIrcCd2">				
																	<select class="form-control" name="${status.expression}" disabled style="width:25%; display:inline">
																		<option value = "NONE"> None </option>
																		<option value = "KRWIRS" selected> KRWIRS </option>
																		<option value = "USDIRS"> USDIRS </option>
																		<option value = "EURIRS"> EURIRS </option>
																		<option value = "1010000W"> KRW TBond </option>
																	</select>									
																</spring:bind>
																<spring:bind path="productModel.productLegModels[0].couponIrcMrtyCd2">				
																	<input type="text" class="form-control" name="${status.expression}" disabled value="M3" style="width:15%; display:inline">							
																</spring:bind>
																<spring:bind path="productModel.productLegModels[0].couponIrcTypeCd2">				
																	<select class="form-control" name="${status.expression}" disabled style="width:25%; display:inline">
																		<option value = "SPOT"> ZERO </option>
																		<option value = "SWAP"> SWAP </option>
																		<option value = "RMS"> RMS </option>
																	</select>								
																</spring:bind>
																<spring:bind path="productModel.productLegModels[0].couponIrcCouponFreqCd2">				
																	<select class="form-control" name="${status.expression}" disabled style="width:30%; display:inline">
																		<option value = "Q"> Quarterly </option>
																		<option value = "S"> Semi-Annual </option>
																		<option value = "A"> Annual </option>
																	</select>								
																</spring:bind>
															</td>
															<td id="rcvIrInfo2">
																<spring:bind path="productModel.productLegModels[1].couponIrcCd2">				
																	<select class="form-control" name="${status.expression}" disabled style="width:25%; display:inline">
																		<option value = "NONE"> None </option>
																		<option value = "KRWIRS" selected> KRWIRS </option>
																		<option value = "USDIRS"> USDIRS </option>
																		<option value = "EURIRS"> EURIRS </option>
																		<option value = "1010000W"> KRW TBond </option>
																	</select>									
																</spring:bind>
																<spring:bind path="productModel.productLegModels[1].couponIrcMrtyCd2">				
																	<input type="text" class="form-control" name="${status.expression}" disabled value="M3" style="width:15%; display:inline">							
																</spring:bind>
																<spring:bind path="productModel.productLegModels[1].couponIrcTypeCd2">				
																	<select class="form-control" name="${status.expression}" disabled style="width:25%; display:inline">
																		<option value = "SPOT"> ZERO </option>
																		<option value = "SWAP"> SWAP </option>
																		<option value = "RMS"> RMS </option>
																	</select>								
																</spring:bind>
																<spring:bind path="productModel.productLegModels[1].couponIrcCouponFreqCd2">				
																	<select class="form-control" name="${status.expression}" disabled style="width:30%; display:inline">
																		<option value = "Q"> Quarterly </option>
																		<option value = "S"> Semi-Annual </option>
																		<option value = "A"> Annual </option>
																	</select>								
																</spring:bind>
															</td>
														</tr>
														<tr>
															<td><label class="control-label"> IR Code 3 </label></td>
															<td id="payIrInfo3">
																<spring:bind path="productModel.productLegModels[0].couponIrcCd3">				
																	<select class="form-control" name="${status.expression}" disabled style="width:25%; display:inline">
																		<option value = "NONE"> None </option>
																		<option value = "KRWIRS" selected> KRWIRS </option>
																		<option value = "USDIRS"> USDIRS </option>
																		<option value = "EURIRS"> EURIRS </option>
																		<option value = "1010000W"> KRW TBond </option>
																	</select>									
																</spring:bind>
																<spring:bind path="productModel.productLegModels[0].couponIrcMrtyCd3">				
																	<input type="text" class="form-control" name="${status.expression}" disabled value="M3" style="width:15%; display:inline">							
																</spring:bind>
																<spring:bind path="productModel.productLegModels[0].couponIrcTypeCd3">				
																	<select class="form-control" name="${status.expression}" disabled style="width:25%; display:inline">
																		<option value = "SPOT"> ZERO </option>
																		<option value = "SWAP"> SWAP </option>
																		<option value = "RMS"> RMS </option>
																	</select>								
																</spring:bind>
																<spring:bind path="productModel.productLegModels[0].couponIrcCouponFreqCd3">				
																	<select class="form-control" name="${status.expression}" disabled style="width:30%; display:inline">
																		<option value = "Q"> Quarterly </option>
																		<option value = "S"> Semi-Annual </option>
																		<option value = "A"> Annual </option>
																	</select>								
																</spring:bind>
															</td>
															<td id="rcvIrInfo3">
																<spring:bind path="productModel.productLegModels[1].couponIrcCd3">				
																	<select class="form-control" name="${status.expression}" disabled style="width:25%; display:inline">
																		<option value = "NONE"> None </option>
																		<option value = "KRWIRS" selected> KRWIRS </option>
																		<option value = "USDIRS"> USDIRS </option>
																		<option value = "EURIRS"> EURIRS </option>
																		<option value = "1010000W"> KRW TBond </option>
																	</select>									
																</spring:bind>
																<spring:bind path="productModel.productLegModels[1].couponIrcMrtyCd3">				
																	<input type="text" class="form-control" name="${status.expression}" disabled value="M3" style="width:15%; display:inline">							
																</spring:bind>
																<spring:bind path="productModel.productLegModels[1].couponIrcTypeCd3">				
																	<select class="form-control" name="${status.expression}" disabled style="width:25%; display:inline">
																		<option value = "SPOT"> ZERO </option>
																		<option value = "SWAP"> SWAP </option>
																		<option value = "RMS"> RMS </option>
																	</select>								
																</spring:bind>
																<spring:bind path="productModel.productLegModels[1].couponIrcCouponFreqCd3">				
																	<select class="form-control" name="${status.expression}" disabled style="width:30%; display:inline">
																		<option value = "Q"> Quarterly </option>
																		<option value = "S"> Semi-Annual </option>
																		<option value = "A"> Annual </option>
																	</select>								
																</spring:bind>
															</td>
														</tr>
														<tr>
															<td>
																<label class="control-label"> Schedule Generation </label>
															</td>
															<td>
																Coupon Frequency
																<select class="form-control" id="payCouponFreq" style="width:30%; display:inline">
																	<option value = "Q"> Quarterly </option>
																	<option value = "S"> Semi-Annual </option>
																	<option value = "A"> Annual </option>
																</select>	
																<button type="button" class="btn btn-success pull-right" id="paySchedule_button" onclick="genSchedule(this);"> Generate </button>
															</td>
															<td>
																Coupon Frequency
																<select class="form-control" id="rcvCouponFreq" style="width:30%; display:inline">
																	<option value = "Q"> Quarterly </option>
																	<option value = "S"> Semi-Annual </option>
																	<option value = "A"> Annual </option>
																</select>
																<button type="button" class="btn btn-success pull-right" id="rcvSchedule_button" onclick="genSchedule(this);"> Generate </button>
															</td>
														</tr>
														<tr>
															<td></td>
															<td>
																<table id ="paySchedule" class="table table-striped"></table>
															</td>
															
															<td>
																<table id ="rcvSchedule" class="table table-striped"></table>
															</td>
															
														</tr>
													</tbody>
												</table>
											</div>
										</div>
									</div>
								</div>
								
							</div>
							<div class="tab-pane fade" id="exerciseInfoTab">
							
							</div>
						</div>
					</form>					
				</div>
			</div>
			<!-- /#container-fluid -->
		</div>	
		<!-- /#page-wrapper -->
	</div>
	<!-- /#wrapper -->
	
	<jsp:include page="../fragments/footer.jsp" />
	<script type="text/javascript">
		function genSchedule(selector){
			var legCode = selector.id.substring(0,3);
			var issueDt = $('#issueDt').val().replace(/-/gi,"");
			var mrtyDt = $('#mrtyDt').val().replace(/-/gi,"");
			var couponFreq = $('#' + legCode +'CouponFreq').val();
			var jsonStr = './json/scheduleList?issueDt=' + issueDt + '&mrtyDt=' + mrtyDt + '&couponFreq=' + couponFreq;
			
			$.getJSON(jsonStr, function(data){
				$('#' + legCode + 'Schedule').empty();				
				buildScheduleTable(data, legCode)	
			});			
		};
	
		function buildScheduleTable(data, legCode){
			var selector = "#" + legCode +"Schedule";
			var columns = addColumns(data, selector, legCode);
			var capFloorCd = $('#' + legCode +'CapFloorCd').val();
			var undTypeCd = $("#" + legCode + "UndType option:selected").val();
			
			//Leg Index
			var legIndex = "-1";
			if (legCode == "pay"){
				legIndex = "0";
			} else if (legCode == "rcv"){
				legIndex = "1";
			}			
						
			//Generate
			for (var rowIndex = 0; rowIndex < data.length; rowIndex++){
				var row$ = $('<tr/>');
		        var rowIndexStr = "";
		        if (rowIndex < 10){
		        	rowIndexStr = "0" + rowIndex;
		        } else {
		        	rowIndexStr = rowIndex;
		        }
		        var scheduleModelStr = 'productScheduleModels[' + legIndex + '][' + rowIndex + ']';
		        
		        for (var colIndex = 0 ; colIndex < columns.length ; colIndex++) {
		        	var columnValue = columns[colIndex];
		        	var cellValue = data[rowIndex][columnValue];
		        	var td$ = $('<td/>');
		        	if (columnValue == "CapFloor"){
		        		//CapFloor
		        		td$.attr({
		            		'id' : legCode + "CapFloor" + rowIndexStr
		            	});
		            	var contents$ = "asd";//genCapFloorContents(tagCode, capFloorTypeCd);
		            	if (capFloorCd == 0){
		            		td$.html(contents$).attr({'hidden': true});
		            	} else {
		            		td$.html(contents$);	
		            	}
		        	} else if (columnValue == "CouponType"){
		        		//CouponType
		        		var contents$ = $('<select/>').attr({
		            		'class': 'form-control', 
		            		'name': scheduleModelStr + '.couponType',
		            		'onChange' :  'changeCouponTag(this);'
		            	});
		        		if (undTypeCd == 0){
		        			//NONE		
		            		contents$.append($('<option>',{value: 1, text: 'RESET'}).attr('disabled', true));
			            	contents$.append($('<option>',{value: 2, text: 'ACCRUAL'}).attr('disabled', true));
			            	contents$.append($('<option>',{value: 3, text: 'AVERAGE'}).attr('disabled', true));
			            	contents$.append($('<option>',{value: 4, text: 'FIXED'}));
		        		} else if (undTypeCd == 1){
		        			//R1	        			
		        			contents$.append($('<option>',{value: 1, text: 'RESET'}));
			            	contents$.append($('<option>',{value: 2, text: 'ACCRUAL'}));
			            	contents$.append($('<option>',{value: 3, text: 'AVERAGE'}));
			            	contents$.append($('<option>',{value: 4, text: 'FIXED'}));
		        		} else if (undTypeCd == 2){
		        			//R1 - R2	        			
		        			contents$.append($('<option>',{value: 1, text: 'RESET'}).attr('disabled', true));
			            	contents$.append($('<option>',{value: 2, text: 'ACCRUAL'}));
			            	contents$.append($('<option>',{value: 3, text: 'AVERAGE'}));
			            	contents$.append($('<option>',{value: 4, text: 'FIXED'}));
		        		} else if (undTypeCd == 3){
		        			//R1 & R2	        			
		        			contents$.append($('<option>',{value: 1, text: 'RESET'}).attr('disabled', true));
			            	contents$.append($('<option>',{value: 2, text: 'ACCRUAL'}));
			            	contents$.append($('<option>',{value: 3, text: 'AVERAGE'}).attr('disabled', true));
			            	contents$.append($('<option>',{value: 4, text: 'FIXED'}));
		        		} else if (undTypeCd == 4){
		        			//R1 & (R2 - R3)	        			
		        			contents$.append($('<option>',{value: 1, text: 'RESET'}).attr('disabled', true));
			            	contents$.append($('<option>',{value: 2, text: 'ACCRUAL'}));
			            	contents$.append($('<option>',{value: 3, text: 'AVERAGE'}).attr('disabled', true));
			            	contents$.append($('<option>',{value: 4, text: 'FIXED'}));
		        		}
		        		td$.html(contents$);
		        	/* } else if (columnValue == "Coupon"){
		        		//Coupon
		        		
		        	} else if (columnValue == "Condition"){
		        		//Condition
		        		 */
		        	} else if (columnValue == "startDate"){		        	
		        		//StartDate
		        		var contents$ = $('<input/>').attr({
		            		'type':'text',
		            		'value': cellValue,
		            		'class': 'form-control smaller',
		            		'name': scheduleModelStr + '.couponStrtDt'
		            	});	
		        		td$.html(contents$);
		        	} else if (columnValue == "endDate"){
		        		//EndDate
		        		var contents$ = $('<input/>').attr({
		            		'type':'text',
		            		'value': cellValue,
		            		'class': 'form-control smaller',
		            		'name': scheduleModelStr + '.couponEndDt'
		            	});	
		        		td$.html(contents$);
		        	} else if (columnValue == "paymentDate"){
		        		//PaymentDate
		        		var contents$ = $('<input/>').attr({
		            		'type':'text',
		            		'value': cellValue,
		            		'class': 'form-control smaller',
		            		'name': scheduleModelStr + '.couponPayDt'
		            	});	
		        		td$.html(contents$);
		        	}		        	
	            	row$.append(td$);
		        }		        
		        $(selector).append(row$);
			}			
		}
	
		function addColumns(data, selector, legCode){
			var columnSet = [];
		    var headerTr$ = $('<tr/>');
		    
	        var rowHash = data[0];
	        for (var key in rowHash) {
	            if ($.inArray(key, columnSet) == -1){
	                columnSet.push(key);
	                headerTr$.append($('<th/>')
	                		.attr({'width' : '10%'})
	                		.html(key));
	            }		            
	        }	        
		    
		  	//CouponType
		    columnSet.push("CouponType");	    
	        headerTr$.append($('<th/>').html("Coupon Type"));
	        
	        //Coupon
		    columnSet.push("Coupon");	    
	        headerTr$.append($('<th/>').html("Coupon"));
	        
	        //Cap, Floor
	        var capFloorCd = $('#' + legCode +'CapFloorCd').val();
	        columnSet.push("CapFloor");	   
	        if (capFloorCd == 0){
	        	headerTr$.append($('<th/>').html("CapFloor").attr({'hidden': true}));
	        } else {
	        	headerTr$.append($('<th/>').html("CapFloor"));
	        }	        
	        /* //Condition
	        var conditionCd = $('#' + tagCode + 'LegCondiTypeCd').val();
	        columnSet.push("Condition");
	        if (conditionCd == 0){
	        	headerTr$.append($('<th/>').html("Condition").attr({'hidden': true}));	
	        } else {
	        	headerTr$.append($('<th/>').html("Condition"));	
	        } */	
		    
		    $(selector).append(headerTr$);
		    
		    return columnSet;
		}
		
		function chgCondiType(selector){
			var legCode = selector.id.substring(0,3);
			var typeCd = selector.value;
			var targetTagInput1 = $('#' + legCode + "IrInfo1 input");
			var targetTagSelect1 = $('#' + legCode + "IrInfo1 select");
			var targetTagInput2 = $('#' + legCode + "IrInfo2 input");
			var targetTagSelect2 = $('#' + legCode + "IrInfo2 select");
			var targetTagInput3 = $('#' + legCode + "IrInfo3 input");
			var targetTagSelect3 = $('#' + legCode + "IrInfo3 select");			
			var targetCondiType = $('#' + legCode + 'ConditionType option');
			
			if (typeCd == 0){
				//NONE
				targetTagInput1.attr('disabled', 'true');
				targetTagSelect1.attr('disabled', 'true');				
				targetTagInput2.attr('disabled', 'true');
				targetTagSelect2.attr('disabled', 'true');
				targetTagInput3.attr('disabled', 'true');
				targetTagSelect3.attr('disabled', 'true');
				
				targetCondiType[0].disabled = false;
				for (var idx = 1; idx < targetCondiType.length; idx++){
					targetCondiType[idx].disabled = true;	
				}
				
			} else if (typeCd == 1){
				//R1
				targetTagInput1.removeAttr('disabled');
				targetTagSelect1.removeAttr('disabled');				
				targetTagInput2.attr('disabled', 'true');
				targetTagSelect2.attr('disabled', 'true');
				targetTagInput3.attr('disabled', 'true');
				targetTagSelect3.attr('disabled', 'true');
				
				targetCondiType[0].disabled = false;
				targetCondiType[1].disabled = false;
				for (var idx = 2; idx < targetCondiType.length; idx++){
					targetCondiType[idx].disabled = true;	
				}
				
			} else if (typeCd == 2){
				//R1 - R2
				targetTagInput1.removeAttr('disabled');
				targetTagSelect1.removeAttr('disabled');				
				targetTagInput2.removeAttr('disabled');
				targetTagSelect2.removeAttr('disabled');
				targetTagInput3.attr('disabled', 'true');
				targetTagSelect3.attr('disabled', 'true');
				
				targetCondiType[0].disabled = false;
				targetCondiType[1].disabled = false;
				targetCondiType[2].disabled = false;
				targetCondiType[4].disabled = false;
				
				targetCondiType[3].disabled = true;
				targetCondiType[5].disabled = true;
				targetCondiType[6].disabled = true;
				targetCondiType[7].disabled = true;
				targetCondiType[8].disabled = true;
				targetCondiType[9].disabled = true;
				
			} else if (typeCd == 3){
				//R1 & R2
				targetTagInput1.removeAttr('disabled');
				targetTagSelect1.removeAttr('disabled');				
				targetTagInput2.removeAttr('disabled');
				targetTagSelect2.removeAttr('disabled');
				targetTagInput3.attr('disabled', 'true');
				targetTagSelect3.attr('disabled', 'true');
				
				targetCondiType[0].disabled = false;
				targetCondiType[1].disabled = false;
				targetCondiType[2].disabled = false;
				targetCondiType[6].disabled = false;
				
				targetCondiType[3].disabled = true;
				targetCondiType[4].disabled = true;
				targetCondiType[5].disabled = true;				
				targetCondiType[7].disabled = true;
				targetCondiType[8].disabled = true;
				targetCondiType[9].disabled = true;
				
			} else if (typeCd == 4){
				//R1 & R2 - R3
				targetTagInput1.removeAttr('disabled');
				targetTagSelect1.removeAttr('disabled');				
				targetTagInput2.removeAttr('disabled');
				targetTagSelect2.removeAttr('disabled');
				targetTagInput3.removeAttr('disabled');
				targetTagSelect3.removeAttr('disabled');
				targetCondiType[0].disabled = false;
				targetCondiType[1].disabled = false;
				targetCondiType[2].disabled = false;
				targetCondiType[3].disabled = false;
				targetCondiType[5].disabled = false;				
				targetCondiType[9].disabled = false;
				
				targetCondiType[4].disabled = true;
				targetCondiType[6].disabled = true;
				targetCondiType[7].disabled = true;
				targetCondiType[8].disabled = true;
			}			
		};
	</script>	
	
</body>
</html>