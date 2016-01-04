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
							<h1 class="page-header"> Pricer </h1>
					</div>
				</div>	
				<div class="row">
					<c:if test="${not empty msg}">
						<div class="alert alert-${css} alert-dismissible" role="alert">
							<button type="button" class="close" data-dismiss="alert" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
							<strong>${msg}</strong>
						</div>
					</c:if>
					<div class="col-lg-12">
						<div class="panel panel-primary form-group">
							<div class="panel-heading">
								<h3 class="panel-title"> Pricer Control Panel </h3>
							</div>
							<div class="panel-body">
								<spring:url value="/pricer/list" var="pricerExecuteUrl" />
								<form action="${pricerExecuteUrl}" method="get">								
									<div class="list-group">
										<div class = "list-group-item form-group">
											<label> Instrument Code </label>			
											<select class="form-control" name="instrumentCd">
												<c:forEach items="${instrumentList}" var="list">													
													<c:choose>													
														<c:when test="${list.value == selectedInstrumentCd}">
															<option value="${list.value}" selected> ${list.value}</option>
														</c:when>													
														<c:otherwise>
															<option value="${list.value}"> ${list.value}</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
										</div>
										<div class = "list-group-item form-group">
											<label> Process Date </label>												
											<input class="form-control" type="date" name="processDt" value="${processDt}">
										</div>
										<div class = "list-group-item form-group">
											<label> Pricer Type </label>
											<input type="radio" name="pricerType" id="pricerType1" value="price" checked> Price
											<input type="radio" name="pricerType" id="pricerType2" value="delta"> Delta											
										</div>
									</div>
									<div>
										<input class="btn btn-success" type ="submit" id ="executeButton" value ='Execute'>	
									</div>									
								</form>								
							</div>						
						</div>						
					</div>
				</div>
				<div class="row">					
					<c:choose>
						<c:when test="${not empty productInfoModel}">
							<div class="col-lg-12">
								<ul class="nav nav-pills">
	                                <li class="active"><a href="#result" data-toggle="tab">Result</a>
	                                </li>
	                                <li><a href="#product" data-toggle="tab">Product Information</a>
	                                </li>
	                                <li><a href="#leg" data-toggle="tab">Leg Information</a>
	                                </li>
	                                <li><a href="#market" data-toggle="tab">Market Information</a>
	                                </li>
	                            </ul>
                            </div>
                            
                            <!-- Tab panes -->
                            <div class="tab-content">
                            	<div class="tab-pane fade in active" id="result">
                            		<c:choose>
                            			<c:when test="${not empty resultPrice}">                            			
	                            			<div class="col-lg-12">
												<div class="panel panel-primary form-group">
													<div class="panel-heading">
														<h3 class="panel-title"> Pricer Result </h3>
													</div>
													<div class="panel-body">													
														<div class="list-group">													
															<div class = "list-group-item">
																<label> Product Price </label>
																${resultPrice}
															</div>
															<div class = "list-group-item">
																<label> Rcv Leg Price </label>
																${resultRcvLegPrice}		
															</div>
															<div class = "list-group-item">
																<label> Pay Leg Price </label>
																${resultPayLegPrice}		
															</div>		
														</div>
													</div>
												</div>
											</div>
										</c:when>										
										<c:otherwise>
											<c:forEach var="deltaRF" items="${resultDelta}" varStatus="deltaLegIndex">
												<div class="col-lg-6">
													<div class="panel panel-primary form-group">
														<div class="panel-heading">
															<h3 class="panel-title"> ${resultRiskFactor[deltaLegIndex.index]} </h3>
														</div>
														<div class="panel-body">													
															<div class="list-group">
																<table class="table table-striped">
																	<thead>
																		<tr>
																			<th>Vertex</th>
																			<th>Delta</th>
																		</tr>
																	</thead>
																	<c:forEach var="delta" items="${deltaRF}" varStatus="deltaIndex">
																		<tr>
																			<td>${resultVertex[deltaLegIndex.index][deltaIndex.index]}</td>
																			<td>${delta}</td>
																		</tr>
																	</c:forEach>	
																</table>
															</div>
														</div>
													</div>
												</div>																										
											</c:forEach>
										</c:otherwise>
									</c:choose>													
                            	</div>
                            	<div class="tab-pane fade" id="product">
                            		<div class="col-lg-12">
										<div class="panel panel-primary form-group">
											<div class="panel-heading">
												<h3 class="panel-title"> Product Information </h3>
											</div>
											<div class="panel-body">
												<div class="list-group">						
													<div class = "list-group-item">											
														<label> Issue Date </label>										
														${productInfoModel.issueDt}
													</div>
													<div class = "list-group-item">
														<label> Maturity Date </label>
														${productInfoModel.mrtyDt}
													</div>	
													<div class = "list-group-item">
														<label> Principal Exchange </label>
														${productInfoModel.hasPrincipalExchange}
													</div>
													<div class = "list-group-item">
														<label> Currency </label>
														${productInfoModel.currency}
													</div>
												</div>
											</div>							
										</div>
									</div>
                            	</div>
                            	<div class="tab-pane fade" id="leg">
                            		<c:forEach var="legInfoPricerModel" items="${legPricerModelForm.legInfoPricerModels}" varStatus="status">
										<div class="col-lg-6">
											<div class="panel panel-primary form-group">
												<div class="panel-heading">
													<h3 class="panel-title"> ${legInfoPricerModel.payRcv} Leg Information </h3>
												</div>
												<div class="panel-body">
													<div class="list-group">						
														<div class = "list-group-item">
															<label> PayRcv Type </label>
															${legInfoPricerModel.payRcv}
														</div>
														<div class = "list-group-item">
															<label> DCF </label>
															${legInfoPricerModel.dcf}
														</div>
														<div class = "list-group-item">
															<label> Condition Type </label>
															${legInfoPricerModel.conditionType}
														</div>
														<div class = "list-group-item">
															<label> Underlying Type </label>
															${legInfoPricerModel.underlyingType}
														</div>
														<div class = "list-group-item">
															<label> Has Cap </label>
															${legInfoPricerModel.hasCap}
														</div>
														<div class = "list-group-item">
															<label> Has Floor </label>
															${legInfoPricerModel.hasFloor}
														</div>
														<div class = "list-group-item">
															<label> Next Coupon Date </label>
															${legInfoPricerModel.nextCouponDt}
														</div>
														<div class = "list-group-item">
															<label> Next Coupon Rate </label>
															${legInfoPricerModel.nextCouponRate}
														</div>
														<div class = "list-group-item">
															<label> Cumulated Accrual Days </label>
															${legInfoPricerModel.cumulatedAccrualDays}
														</div>
														<div class = "list-group-item">
															<label> Cumulated Average Coupon </label>
															${legInfoPricerModel.cumulatedAvgCoupon}
														</div>
														<c:forEach var="underlyingInfoPricerModel" items="${legInfoPricerModel.underlyingInfoPricerModels}" varStatus = "status1">
															<div class = "list-group-item">
																<label> Underlying Info ${status1.index} </label>	
																<br>
																<label> Underlying Info Type</label>
																${underlyingInfoPricerModel.underlyingInfoFlag}
																<br>
																<c:choose>
																	<c:when test="${underlyingInfoPricerModel.underlyingInfoFlag == 'Rate'}">
																		<label class="control-label"> Rate Type </label>
																		${underlyingInfoPricerModel.rateType}
																		<br>
																		<label> Rate Tenor </label>
																		${underlyingInfoPricerModel.tenor}
																		<br>
																		<label> Swap Coupon Frequency </label>
																		${underlyingInfoPricerModel.swapCouponFrequency}
																		<br>
																		<label> Model Type </label>
																		${underlyingInfoPricerModel.modelType}
																		<br>
																	</c:when>
																	<c:when test="${underlyingInfoPricerModel.underlyingInfoFlag == 'Equity'}">
																		<label> Base Price </label>
																		${underlyingInfoPricerModel.basePrice}
																		<br>
																		<label> RiskFree Type </label>
																		${underlyingInfoPricerModel.riskFreeType}
																		<br>
																	</c:when>
																	<c:when test="${underlyingInfoPricerModel.underlyingInfoFlag == 'FX'}">
																		
																	</c:when>
																</c:choose>
															</div>											
														</c:forEach>	
														
														<table class="table table-striped">
															<thead>
																<tr>
																	<th>Start Date</th>
																	<th>End Date</th>
																	<th>Payment Date</th>
																	<th>Coupon Type</th>
																	<th>Coupon</th>
																	<th>Condition</th>
																</tr>
															</thead>
															<c:forEach items="${legPricerModelForm.legPeriodPricerModels[status.index].startDt}" varStatus="status2">
																<tr>
																	<td>${legPricerModelForm.legPeriodPricerModels[status.index].startDt[status2.index]}</td>
																	<td>${legPricerModelForm.legPeriodPricerModels[status.index].endDt[status2.index]}</td>
																	<td>${legPricerModelForm.legPeriodPricerModels[status.index].paymentDt[status2.index]}</td>
																	<td>${legPricerModelForm.legPeriodPricerModels[status.index].couponType[status2.index]}</td>
																	<td>${legPricerModelForm.legPeriodPricerModels[status.index].coupon[status2.index]}</td>
																	<td>${legPricerModelForm.legPeriodPricerModels[status.index].condition[status2.index]}</td>
																</tr>												
															</c:forEach>
														</table>																	
													</div>
												</div>
											</div>
										</div>
									</c:forEach>
                            	</div>
                            	<div class="tab-pane fade" id="market">
                            		<div class="col-lg-12">
                            			<div class="panel panel-primary form-group">
											<div class="panel-heading">
												<h3 class="panel-title"> Market Information </h3>
											</div>
											<div class="panel-body">
												TO BE IMPLEMENTED
												${discountRateCurveModel.date}
												<br>
												${discountRateCurveModel.dcf}
												<br>
												<c:forEach var="discRateCurveModel" items="${discountRateCurveModel.rate}" varStatus="discIndex">
													${discountRateCurveModel.vertex[discIndex.index]}
													${discountRateCurveModel.rate[discIndex.index]}
													${discountRateCurveModel.rateType[discIndex.index]}
													<br>
												</c:forEach>
											</div>
										</div>                            			
                            		</div>                            		
                            	</div>
                            </div>
						</c:when>
					</c:choose>			
				</div>
			</div>
		</div>
		<!-- /#page-wrapper -->
	</div>
	<!-- /#wrapper -->	
	
	<jsp:include page="../fragments/footer.jsp" />
	
</body>
</html>