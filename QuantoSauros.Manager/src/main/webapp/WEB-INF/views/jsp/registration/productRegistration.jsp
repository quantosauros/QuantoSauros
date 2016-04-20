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
									<a href="#payLegInfoTab" data-toggle="tab" aria-expanded="true">PAY LEG</a>
								</li>																					
								<li>
									<a href="#rcvLegInfoTab" data-toggle="tab" aria-expanded="true">RECEIVE LEG</a>
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
																	<input type="date" class="form-control" name="${status.expression}" id="issueDt" placeholder="Issue Date" value="2013-12-02">
																</spring:bind>	
															</td>															
														</tr>
														<tr>														
															<td><label class="control-label"> Maturity Date </label></td>
															<td>
																<spring:bind path="productModel.productInfoModel.mrtyDt">
																	<input type="date" class="form-control" name="${status.expression}" id="mrtyDt" placeholder="Maturity Date" value="2028-12-03">
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
													</tbody>
												</table>	
											</div>	
										</div>								
									</div>
								</div>
							</div>
							<div class="tab-pane fade" id="payLegInfoTab">
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
														<th style="width:15%;"> </th>
														<th style="width:15%; min-width:85px;"> </th>
														<th style="width:10%; min-width:65px;"> </th>
														<th style="width:30%; min-width:75px;"> </th>				
														<th style="width:30%"> </th>											
													</tr>
												</thead>
												<tbody>
													<form:hidden path="productModel.productLegModels[0].payRcvCd" value="P" />														
													<tr>
														<td><label class="control-label"> Leg Type Code </label></td>
														<td colspan=4>
															<spring:bind path="productModel.productLegModels[0].legTypeCd">				
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
																<select class="form-control" name="${status.expression}" id="${status.expression}">
																	<option value="KRW">KRW</option>
																	<option value="USD">USD</option>
																	<option value="EUR">EUR</option>
																</select>
															</spring:bind>
														</td>
														<td colspan=3>															
															<spring:bind path="productModel.productLegModels[0].notionalPrincipal">				
																<input type="number" class="form-control" name="${status.expression}" placeholder="Pay Notional Principal" id="${status.expression}" value="10000000000">											
															</spring:bind>
														</td>															
													</tr>														
													<tr>
														<td><label class="control-label"> DCF </label></td>
														<td colspan=4>
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
													</tr>
													<tr>
														<td><label class="control-label"> Underlying Type</label></td>
														<td colspan=4>
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
													</tr>
													<tr>
														<td><label class="control-label"> Condition Type </label></td>
														<td colspan=4>
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
													</tr>
													<tr>
														<td><label class="control-label"> Cap/Floor Type </label></td>
														<td colspan=4>
															<spring:bind path="productModel.productLegModels[0].capFloorCd">				
																<select class="form-control" name="${status.expression}" id="payCapFloorCd">
																	<option value = "0"> None </option>
																	<option value = "1"> Cap Only </option>
																	<option value = "2"> Floor Only </option>
																	<option value = "3"> Cap & Floor </option>
																</select>									
															</spring:bind>
														</td>															
													</tr>
													<tr id="payIrInfo1">
														<td><label class="control-label"> IR Code 1 </label></td>
														<td>
															<spring:bind path="productModel.productLegModels[0].couponIrcCd1">				
																<select class="form-control" name="${status.expression}" disabled>
																	<option value = "NONE"> None </option>
																	<option value = "KRWIRS" selected> KRWIRS </option>
																	<option value = "USDIRS"> USDIRS </option>
																	<option value = "EURIRS"> EURIRS </option>
																	<option value = "KTB"> KTB </option>
																</select>									
															</spring:bind>
														</td>
														<td>	
															<spring:bind path="productModel.productLegModels[0].couponIrcMrtyCd1">				
																<input type="text" class="form-control" name="${status.expression}" disabled value="M3">							
															</spring:bind>
														</td>
														<td>
															<spring:bind path="productModel.productLegModels[0].couponIrcTypeCd1">				
																<select class="form-control" name="${status.expression}" disabled>
																	<option value = "SPOT"> ZERO </option>
																	<option value = "SWAP"> SWAP </option>
																	<option value = "RMS"> RMS </option>
																</select>								
															</spring:bind>
														</td>
														<td>
															<spring:bind path="productModel.productLegModels[0].couponIrcCouponFreqCd1">				
																<select class="form-control" name="${status.expression}" disabled>
																	<option value = "Q"> Quarterly </option>
																	<option value = "S"> Semi-Annual </option>
																	<option value = "A"> Annual </option>
																</select>								
															</spring:bind>															
														</td>															
													</tr>
													<tr id="payIrInfo2">
														<td><label class="control-label"> IR Code 2 </label></td>
														<td>
															<spring:bind path="productModel.productLegModels[0].couponIrcCd2">				
																<select class="form-control" name="${status.expression}" disabled>
																	<option value = "NONE"> None </option>
																	<option value = "KRWIRS" selected> KRWIRS </option>
																	<option value = "USDIRS"> USDIRS </option>
																	<option value = "EURIRS"> EURIRS </option>
																	<option value = "KTB"> KTB </option>
																</select>									
															</spring:bind>
														</td>
														<td>
															<spring:bind path="productModel.productLegModels[0].couponIrcMrtyCd2">				
																<input type="text" class="form-control" name="${status.expression}" disabled value="M3">							
															</spring:bind>
														</td>
														<td>
															<spring:bind path="productModel.productLegModels[0].couponIrcTypeCd2">				
																<select class="form-control" name="${status.expression}" disabled>
																	<option value = "SPOT"> ZERO </option>
																	<option value = "SWAP"> SWAP </option>
																	<option value = "RMS"> RMS </option>
																</select>								
															</spring:bind>
														</td>
														<td>
															<spring:bind path="productModel.productLegModels[0].couponIrcCouponFreqCd2">				
																<select class="form-control" name="${status.expression}" disabled>
																	<option value = "Q"> Quarterly </option>
																	<option value = "S"> Semi-Annual </option>
																	<option value = "A"> Annual </option>
																</select>								
															</spring:bind>
														</td>															
													</tr>
													<tr id="payIrInfo3">
														<td><label class="control-label"> IR Code 3 </label></td>
														<td>
															<spring:bind path="productModel.productLegModels[0].couponIrcCd3">				
																<select class="form-control" name="${status.expression}" disabled>
																	<option value = "NONE"> None </option>
																	<option value = "KRWIRS" selected> KRWIRS </option>
																	<option value = "USDIRS"> USDIRS </option>
																	<option value = "EURIRS"> EURIRS </option>
																	<option value = "KTB"> KTB </option>
																</select>									
															</spring:bind>
														</td>
														<td>
															<spring:bind path="productModel.productLegModels[0].couponIrcMrtyCd3">				
																<input type="text" class="form-control" name="${status.expression}" disabled value="M3">							
															</spring:bind>
														</td>
														<td>
															<spring:bind path="productModel.productLegModels[0].couponIrcTypeCd3">				
																<select class="form-control" name="${status.expression}" disabled>
																	<option value = "SPOT"> ZERO </option>
																	<option value = "SWAP"> SWAP </option>
																	<option value = "RMS"> RMS </option>
																</select>								
															</spring:bind>
														</td>
														<td>
															<spring:bind path="productModel.productLegModels[0].couponIrcCouponFreqCd3">				
																<select class="form-control" name="${status.expression}" disabled>
																	<option value = "Q"> Quarterly </option>
																	<option value = "S"> Semi-Annual </option>
																	<option value = "A"> Annual </option>
																</select>								
															</spring:bind>
														</td>															
													</tr>
													<tr>
														<td>
															<label class="control-label"> Schedule Generate </label>
														</td>
														<td>
															Coupon Frequency
														</td>
														<td colspan=2>															
															<select class="form-control" id="payCouponFreq">
																<option value = "Q"> Quarterly </option>
																<option value = "S"> Semi-Annual </option>
																<option value = "A"> Annual </option>
															</select>
														</td>	
														<td>
															<button type="button" class="btn btn-success pull-right" id="paySchedule_button" onclick="genSchedule(this);"> Generate </button>
														</td>															
													</tr>													
												</tbody>
											</table>
											</div>
										</div>
									</div>
									
									<div class="table-responsive">
										<table id ="paySchedule" class="table table-striped"></table>
									</div>
									
								</div>
							</div>
							<div class="tab-pane fade" id="rcvLegInfoTab">
								<div class="col-lg-12">
									<div class="panel panel-primary form-group">
										<div class="panel-heading">
											<h3 class="panel-title"> Receive Leg </h3>
										</div>
										<div class="panel-body">	
											<div class="table-responsive">									
											<table class="table table-striped">
												<thead>
													<tr>
														<th style="width:15%;"> </th>
														<th style="width:15%; min-width:85px;"> </th>
														<th style="width:10%; min-width:65px;"> </th>
														<th style="width:30%; min-width:75px;"> </th>				
														<th style="width:30%"> </th>											
													</tr>
												</thead>
												<tbody>
													<form:hidden path="productModel.productLegModels[1].payRcvCd" value="R" />														
													<tr>
														<td><label class="control-label"> Leg Type Code </label></td>
														<td colspan=4>
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
															<spring:bind path="productModel.productLegModels[1].ccyCd">				
																<select class="form-control" name="${status.expression}" id="${status.expression}">
																	<option value="KRW">KRW</option>
																	<option value="USD">USD</option>
																	<option value="EUR">EUR</option>
																</select>
															</spring:bind>
														</td>
														<td colspan=3>															
															<spring:bind path="productModel.productLegModels[1].notionalPrincipal">				
																<input type="number" class="form-control" name="${status.expression}" placeholder="RCV Notional Principal" id="${status.expression}" value="10000000000">											
															</spring:bind>
														</td>															
													</tr>														
													<tr>
														<td><label class="control-label"> DCF </label></td>
														<td colspan=4>
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
														<td><label class="control-label"> Underlying Type</label></td>
														<td colspan=4>
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
														<td colspan=4>
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
														<td colspan=4>
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
													<tr id="rcvIrInfo1">
														<td><label class="control-label"> IR Code 1 </label></td>
														<td>
															<spring:bind path="productModel.productLegModels[1].couponIrcCd1">				
																<select class="form-control" name="${status.expression}" disabled>
																	<option value = "NONE"> None </option>
																	<option value = "KRWIRS" selected> KRWIRS </option>
																	<option value = "USDIRS"> USDIRS </option>
																	<option value = "EURIRS"> EURIRS </option>
																	<option value = "1010000W"> KRW TBond </option>
																</select>									
															</spring:bind>
														</td>
														<td>	
															<spring:bind path="productModel.productLegModels[1].couponIrcMrtyCd1">				
																<input type="text" class="form-control" name="${status.expression}" disabled value="M3">							
															</spring:bind>
														</td>
														<td>
															<spring:bind path="productModel.productLegModels[1].couponIrcTypeCd1">				
																<select class="form-control" name="${status.expression}" disabled>
																	<option value = "SPOT"> ZERO </option>
																	<option value = "SWAP"> SWAP </option>
																	<option value = "RMS"> RMS </option>
																</select>								
															</spring:bind>
														</td>
														<td>
															<spring:bind path="productModel.productLegModels[1].couponIrcCouponFreqCd1">				
																<select class="form-control" name="${status.expression}" disabled>
																	<option value = "Q"> Quarterly </option>
																	<option value = "S"> Semi-Annual </option>
																	<option value = "A"> Annual </option>
																</select>								
															</spring:bind>															
														</td>															
													</tr>
													<tr id="rcvIrInfo2">
														<td><label class="control-label"> IR Code 2 </label></td>
														<td>
															<spring:bind path="productModel.productLegModels[1].couponIrcCd2">				
																<select class="form-control" name="${status.expression}" disabled>
																	<option value = "NONE"> None </option>
																	<option value = "KRWIRS" selected> KRWIRS </option>
																	<option value = "USDIRS"> USDIRS </option>
																	<option value = "EURIRS"> EURIRS </option>
																	<option value = "1010000W"> KRW TBond </option>
																</select>									
															</spring:bind>
														</td>
														<td>
															<spring:bind path="productModel.productLegModels[1].couponIrcMrtyCd2">				
																<input type="text" class="form-control" name="${status.expression}" disabled value="M3">							
															</spring:bind>
														</td>
														<td>
															<spring:bind path="productModel.productLegModels[1].couponIrcTypeCd2">				
																<select class="form-control" name="${status.expression}" disabled>
																	<option value = "SPOT"> ZERO </option>
																	<option value = "SWAP"> SWAP </option>
																	<option value = "RMS"> RMS </option>
																</select>								
															</spring:bind>
														</td>
														<td>
															<spring:bind path="productModel.productLegModels[1].couponIrcCouponFreqCd2">				
																<select class="form-control" name="${status.expression}" disabled>
																	<option value = "Q"> Quarterly </option>
																	<option value = "S"> Semi-Annual </option>
																	<option value = "A"> Annual </option>
																</select>								
															</spring:bind>
														</td>															
													</tr>
													<tr id="rcvIrInfo3">
														<td><label class="control-label"> IR Code 3 </label></td>
														<td>
															<spring:bind path="productModel.productLegModels[1].couponIrcCd3">				
																<select class="form-control" name="${status.expression}" disabled>
																	<option value = "NONE"> None </option>
																	<option value = "KRWIRS" selected> KRWIRS </option>
																	<option value = "USDIRS"> USDIRS </option>
																	<option value = "EURIRS"> EURIRS </option>
																	<option value = "1010000W"> KRW TBond </option>
																</select>									
															</spring:bind>
														</td>
														<td>
															<spring:bind path="productModel.productLegModels[1].couponIrcMrtyCd3">				
																<input type="text" class="form-control" name="${status.expression}" disabled value="M3">							
															</spring:bind>
														</td>
														<td>
															<spring:bind path="productModel.productLegModels[1].couponIrcTypeCd3">				
																<select class="form-control" name="${status.expression}" disabled>
																	<option value = "SPOT"> ZERO </option>
																	<option value = "SWAP"> SWAP </option>
																	<option value = "RMS"> RMS </option>
																</select>								
															</spring:bind>
														</td>
														<td>
															<spring:bind path="productModel.productLegModels[1].couponIrcCouponFreqCd3">				
																<select class="form-control" name="${status.expression}" disabled>
																	<option value = "Q"> Quarterly </option>
																	<option value = "S"> Semi-Annual </option>
																	<option value = "A"> Annual </option>
																</select>								
															</spring:bind>
														</td>															
													</tr>
													<tr>
														<td>
															<label class="control-label"> Schedule Generate </label>
														</td>
														<td>
															Coupon Frequency
														</td>
														<td colspan=2>															
															<select class="form-control" id="rcvCouponFreq">
																<option value = "Q"> Quarterly </option>
																<option value = "S"> Semi-Annual </option>
																<option value = "A"> Annual </option>
															</select>
														</td>	
														<td>
															<button type="button" class="btn btn-success pull-right" id="rcvSchedule_button" onclick="genSchedule(this);"> Generate </button>
														</td>															
													</tr>													
												</tbody>
											</table>
											</div>
										</div>
									</div>
									<div class="table-responsive">
										<table id ="rcvSchedule" class="table table-striped"></table>
									</div>
								</div>
							</div>
							<div class="tab-pane fade" id="exerciseInfoTab">
								<div class="col-lg-12">
									<div class="panel panel-primary form-group">
										<div class="panel-heading">
											<h3 class="panel-title"> Exercise Information </h3>
										</div>
										<div class="panel-body">											
											<div class="table-responsive">
												<table class="table table-striped" id="exeControl">
													<tbody>
														<tr>														
															<td><label class="control-label"> Option Type </label></td>
															<td>
																<spring:bind path="productModel.productInfoModel.optionTypeCd">
																	<select class="form-control" name="${status.expression}" id="optionTypeCd" onchange="chgExerciseTag(this);">
																		<option value="NONE"> None </option>
																		<option value="C"> Call </option>
																		<option value="P"> Put </option>
																	</select>													
																</spring:bind>
															</td>															
														</tr>
														<tr>															
															<td><label class="control-label"> Exercise Frequency </label></td>
															<td>
																<select class="form-control" id="exerciseFrequency" disabled>
																	<option value="Q">Quarterly</option>
																	<option value="S">Semi-Annual</option>
																	<option value="A">Annual</option>
																</select>
															</td>															
														</tr>
														<tr>
															<td><label class="control-label"> NonCall Year </label></td>
															<td>
																<input type="text" class="form-control" id="nonCallYear" value="1" disabled>
															</td>
														</tr>
														<tr>
															<td colspan=2>
																<button type="button" class="btn btn-success pull-right" id="exerciseGenerate" onclick="genExercise(this);" disabled> Generate </button>
															</td>
														</tr>													
													</tbody>
												</table>
											</div>
										</div>
									</div>
									<div class="table-responsive">
										<table id ="exeSchedule" class="table table-striped"></table>
									</div>
								</div>
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
	<spring:url value="/resources/js/registration.js" var="registrationJs" />
	<script src="${registrationJs}"></script>
	
	
	
</body>
</html>