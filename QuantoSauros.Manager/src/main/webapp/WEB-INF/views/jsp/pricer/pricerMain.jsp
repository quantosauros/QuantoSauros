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
														<table class="table table-striped">
															<tbody>
																<tr>
																	<td><label class="control-label"> Product Price </label></td>
																	<td>${resultPrice}</td>
																</tr>
																<tr>
																	<td><label class="control-label"> Rcv Leg Price </label></td>
																	<td>${resultRcvLegPrice}</td>
																</tr>
																<tr>
																	<td><label class="control-label"> Pay Leg Price </label></td>
																	<td>${resultPayLegPrice}</td>
																</tr>
															</tbody>
														</table>													
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
																<div class="table-responsive">
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
												<table class="table table-striped">
													<tbody>
														<tr>
															<td><label class="control-label"> Issue Date </label></td>
															<td>${productInfoModel.issueDt}</td>
														</tr>
														<tr>
															<td><label class="control-label"> Maturity Date </label></td>
															<td>${productInfoModel.mrtyDt}</td>
														</tr>
														<tr>
															<td><label class="control-label"> Principal Exchange </label></td>
															<td>${productInfoModel.hasPrincipalExchange}</td>
														</tr>
														<tr>
															<td><label class="control-label"> Currency </label></td>
															<td>${productInfoModel.currency}</td>
														</tr>
													</tbody>
												</table>												
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
														<div class="table-responsive">
															<table class="table table-striped">
																<tbody>
																	<tr>
																		<td><label class="control-label"> PayRcv Type </label></td>
																		<td>${legInfoPricerModel.payRcv}</td>
																	</tr>
																	<tr>
																		<td><label class="control-label"> DCF </label></td>
																		<td>${legInfoPricerModel.dcf}</td>
																	</tr>
																	<tr>
																		<td><label class="control-label"> Condition Type </label></td>
																		<td>${legInfoPricerModel.conditionType}</td>
																	</tr>
																	<tr>
																		<td><label class="control-label"> Underlying Type </label></td>
																		<td>${legInfoPricerModel.underlyingType}</td>
																	</tr>
																	<tr>
																		<td><label class="control-label"> Has Cap </label></td>
																		<td>${legInfoPricerModel.hasCap}</td>
																	</tr>
																	<tr>
																		<td><label class="control-label"> Has Floor </label></td>
																		<td>${legInfoPricerModel.hasFloor}</td>
																	</tr>
																	<tr>
																		<td><label class="control-label"> Next Coupon Date </label></td>
																		<td>${legInfoPricerModel.nextCouponDt}</td>
																	</tr>
																	<tr>
																		<td><label class="control-label"> Next Coupon Rate </label></td>
																		<td>${legInfoPricerModel.nextCouponRate}</td>
																	</tr>
																	<tr>
																		<td><label class="control-label"> Cumulated Accrual Days </label></td>
																		<td>${legInfoPricerModel.cumulatedAccrualDays}</td>
																	</tr>
																	<tr>
																		<td><label class="control-label"> Cumulated Average Coupon </label></td>
																		<td>${legInfoPricerModel.cumulatedAvgCoupon}</td>
																	</tr>
																	<c:forEach var="underlyingInfoPricerModel" items="${legInfoPricerModel.underlyingInfoPricerModels}" varStatus = "status1">
																		<tr>
																			<td><label> Underlying Info ${status1.index} </label></td>
																			<td><label> Underlying Info Type</label>
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
																			</td>
																		</tr>											
																	</c:forEach>
																</tbody>
															</table>
														</div>
													</div>													
													<div class="list-group">	
														<div class="table-responsive">														
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
										</div>
									</c:forEach>
                            	</div>
                            	<div class="tab-pane fade" id="market">
                            		<c:forEach var="marketInfoPricerModel" items="${legPricerModelForm.marketInfoPricerModels}" varStatus="status">
                            			<div class="col-lg-4">
                            				<div class="panel panel-primary form-group">
												<div class="panel-heading">
													<h3 class="panel-title"> ${legPricerModelForm.legInfoPricerModels[status.index].payRcv} Leg Market Information </h3>
												</div>
												<div class="panel-body">
													<div class="list-group">
														<c:forEach var="interestRateCurveModel" items="${marketInfoPricerModel.interestRateCurveModels}" varStatus="curveIndex">
															<div class = "list-group-item">
																<div class="table-responsive">														
																	<table class="table table-striped table-responsive">
																		<tbody>
																			<tr>
																				<td colspan=2><label class="control-label"> Underlying ${curveIndex.index} </label></td>																			
																			</tr>
																			<tr>
																				<td><label class="control-label">Date</label></td>
																				<td>${interestRateCurveModel.date}</td>
																			</tr>
																			<tr>
																				<td><label class="control-label">Day Count Convention</label></td>
																				<td>${interestRateCurveModel.dcf}</td>
																			</tr>
																		</tbody>																	
																	</table>
																</div>																													
																<div class="table-responsive">																
																	<table class="table table-striped table-responsive">
																		<thead>
																			<tr>
																				<th>Vertex</th>
																				<th>Rate</th>
																				<th>Rate Type</th>																			
																			</tr>
																		</thead>
																		<tbody>
																			<c:forEach items="${interestRateCurveModel.rate}" varStatus="rateIndex">
																				<tr>																																
																					<td>${interestRateCurveModel.vertex[rateIndex.index]}</td>
																					<td>${interestRateCurveModel.rate[rateIndex.index]}</td>
																					<td>${interestRateCurveModel.rateType[rateIndex.index]}</td>
																				</tr>																												
																			</c:forEach>	
																		</tbody>
																	</table>
																</div>															
																<div class="table-responsive">
																	<table class="table table-striped table-responsive">
																		<tbody>
																			<tr>
																				<td><label>Mean Reversion 1F </label></td>
																				<td>${marketInfoPricerModel.meanReversion1F[curveIndex.index]}</td>
																			</tr>
																			<tr>
																				<td><label>Mean Reversion 2F 1 </label></td>
																				<td>${marketInfoPricerModel.meanReversion2F1[curveIndex.index]}</td>
																			</tr>
																			<tr>
																				<td><label>Mean Reversion 2F 2 </label></td>
																				<td>${marketInfoPricerModel.meanReversion2F2[curveIndex.index]}</td>
																			</tr>
																			<tr>
																				<td><label>Mean Reversion 2F Correlation </label></td>
																				<td>${marketInfoPricerModel.correlation[curveIndex.index]}</td>
																			</tr>
																		</tbody>
																	</table>
																</div>
															</div>
														</c:forEach>
													</div>
												</div>
											</div>
                            			</div>
                            		</c:forEach>
                            		<div class="col-lg-4">
                            			<div class="panel panel-primary form-group">
											<div class="panel-heading">
												<h3 class="panel-title"> Discount Market Information </h3>
											</div>
											<div class="panel-body">
												<div class="list-group">
													<div class = "list-group-item">
														<div class="table-responsive">														
															<table class="table table-striped table-responsive">
																<tbody>
																	<tr>
																		<td><label class="control-label">Date</label></td>
																		<td>${discMarketInfoPricerModel.interestRateCurveModels[0].date}</td>
																	</tr>
																	<tr>
																		<td><label class="control-label">Day Count Convention</label></td>
																		<td>${discMarketInfoPricerModel.interestRateCurveModels[0].dcf}</td>
																	</tr>
																</tbody>																	
															</table>
														</div>														
														<div class="table-responsive">
															<table class="table table-striped table-responsive">
																<thead>
																	<tr>
																		<th>Vertex</th>
																		<th>Rate</th>
																		<th>Rate Type</th>																			
																	</tr>
																</thead>
																<c:forEach items="${discMarketInfoPricerModel.interestRateCurveModels[0].rate}" varStatus="discIndex">
																	<tr>														
																		<td>${discMarketInfoPricerModel.interestRateCurveModels[0].vertex[discIndex.index]}</td>
																		<td>${discMarketInfoPricerModel.interestRateCurveModels[0].rate[discIndex.index]}</td>
																		<td>${discMarketInfoPricerModel.interestRateCurveModels[0].rateType[discIndex.index]}</td>
																	</tr>
																</c:forEach>
															</table>
														</div>	
														<div class="table-responsive">
															<table class="table table-striped table-responsive">
																<tbody>
																	<tr>
																		<td><label>Mean Reversion 1F </label></td>
																		<td>${discMarketInfoPricerModel.meanReversion1F[0]}</td>
																	</tr>
																	<tr>
																		<td><label>Mean Reversion 2F 1 </label></td>
																		<td>${discMarketInfoPricerModel.meanReversion2F1[0]}</td>
																	</tr>
																	<tr>
																		<td><label>Mean Reversion 2F 2 </label></td>
																		<td>${discMarketInfoPricerModel.meanReversion2F2[0]}</td>
																	</tr>
																	<tr>
																		<td><label>Mean Reversion 2F Correlation </label></td>
																		<td>${discMarketInfoPricerModel.correlation[0]}</td>
																	</tr>
																</tbody>
															</table>
														</div>
													</div>													
												</div>																							
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