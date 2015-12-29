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
    <style type="text/css">		
		.rightfloat{
			float:right;						
			/* display: inline-block; */			
		}				
		.form-control.smaller {  			
  			width: 88px;
  		}
		.form-control.middle {  			
  			width: 100px;
  		}
  		.form-control:disabled {
  			background-color:pink; 
  			color:blue;
  		}
  		.smaller {
  			width: 105px;
  		}
  		.middle {
  			width: 105px;
  		}
  		.inputLeverage {
  			width: 20px;
  		}
  		.inputSpread {
  			width: 35px;
  		}
  		.inputRate {
  			width: 35px;
  		}
	</style>    
</head>
<body>
	<div id="wrapper">		
		<!-- Navigator -->
		<jsp:include page="./navigator.jsp" />
	
		<div id="page-wrapper">	
			<div id="container-fluid">		
				<div class="row">
					<div class="col-lg-12">
						<h1 class="page-header"> Product Registration </h1>
					</div>
				</div>	
				<div class="row">
					<form action = "./registration/execute" method = "post">
						<div class="col-lg-12">
							<ul class="nav nav-pills">
								<li class="active">
									<a href="#productInfo" data-toggle="tab" aria-expanded="true">Product Info</a>
								</li>
								<li>
									<a href="#payLegInfo" data-toggle="tab" aria-expanded="true">Pay Leg</a>
								</li>
								<li>
									<a href="#rcvLegInfo" data-toggle="tab" aria-expanded="true">Receive Leg</a>
								</li>														
								<li id="exerciseDiv" style="visibility:hidden">								
									<a href="#exerciseInfo" data-toggle="tab" aria-expanded="true">Exercise Info</a>								
								</li>
								<li>
									<a href="#execution" data-toggle="tab" aria-expanded="true">Execution</a>									
								</li>
							</ul>						
							
							
							<div class="tab-content">
								<div class="tab-pane fade active in" id="productInfo">
									<div class="panel panel-primary form-group">									
										<div class="panel-body">
											<div class="list-group">
												<div class = "list-group-item">					
													<label for="productCodeInput"> Product Code </label>										
													<span><input class="form-control" id="input-productCode" name="productCd" value ="PRODUCT001"></span>												
												</div>
												<div class = "list-group-item">
													<label for="productCcyCd"> Settlement Currency </label>										
													<span><select class="form-control" id = "input-productCcycd" name="productCcyCd">
														<option value = "KRW"> KRW </option>
														<option value = "USD"> USD </option>
														<option value = "EUR"> EUR </option>
													</select></span>
												</div>
												<div class = "list-group-item">
													<label for="principalExchCd"> Principal Exchange </label>
													<span><select class="form-control" id = "input-principalExchCd" name="principalExchCd">
															<option value ="N"> No Exchange </option>
															<option value ="Y"> Exchange </option>
													</select></span>
												</div>
												<div class = "list-group-item">
													<label for="optionTypeCd"> Option Type Code </label>
													<span><select class ="form-control" id = "input-optionTypeCd" onchange="changeExerciseTag(this);" name="optionTypeCd">
															<option value = "NONE"> NONE </option>
															<option value = "C"> Call </option>
															<option value = "P"> Put </option>
													</select></span>
												</div>																							
												<div class = "list-group-item">
													<label for="issueDateInput"> Issue Date </label>
													<span><input class="form-control" type="date" id="input-issueDt" name="issueDt" value ="2014-09-01"></span>
												</div>
												<div class = "list-group-item">
													<label for="mrtyDateInput"> Maturity Date </label>
													<span><input class="form-control" type="date" id="input-mrtyDt" name="mrtyDt" value ="2029-09-01"></span>
												</div>
											</div>										
										</div>
										<div class="panel-footer">
	                            			Product Information
	                       				</div>
									</div>																
								</div>
								<div class="tab-pane fade" id="payLegInfo">
									<div class="panel panel-primary form-group">									
										<div class="panel-body">
											<div class="list-group">
												<div class="list-group-item">
													<label for="payLegTypeCd">  Leg Type Code(No Select) </label>
													<span><input class="form-control" type="text" id="input-payLegTypeCd" name="payLegTypeCd" value ="RA"></span>
												</div>
												<div class="list-group-item">				
													<label for="payLegNotional"> Notional Principal </label>
													<span><input class="form-control" type="text" id="input-payLegNotional" name="payLegNotional" value ="10000000000"></span>
												</div>
												<div class="list-group-item">
													<label for="payLegCcyCd"> Notional Currency </label>
													<span>
														<select class="form-control" id="input-payLegCcyCd" name="payLegCcyCd">
															<option value = "KRW"> KRW </option>
															<option value = "USD"> USD </option>
															<option value = "EUR"> EUR </option>
														</select>
													</span>
												</div>
												<div class="list-group-item">
													<label for="payLegDCF"> Day Count Fraction </label>
													<span>
														<select class="form-control" id = "input-payLegDCF" name="payLegDCF">
															<option value = "1"> ACTUAL/365 </option>
															<option value = "2"> ACTUAL/360 </option>
															<option value = "3"> ACTUAL/ACTUAL </option>
															<option value = "4"> 30/360</option>
															<option value = "5"> 30E/360</option>
														</select>
													</span>
												</div>
												<div class = "list-group-item">												
													<label for="capFloorCd"> Cap/Floor Code </label>
													<span><select class="form-control" id = "input-payCapFloorCd" name="payLegCapFloorCd">
															<option value = "0"> NONE </option>
															<option value = "1"> Cap Only </option>
															<option value = "2"> Floor Only </option>
															<option value = "3"> Cap & Floor </option>
													</select></span>									
												</div>
												<div class="list-group-item">
													<label for="payLegUndTypeCd">  Underlying Type Code </label>
													<span>
														<select class="form-control" id="input-payLegUndTypeCd" onchange="changeCondiTypeCdTag(this);" name="payLegUndTypeCd">
															<option value ="0"> None </option>
															<option value ="1"> R1 </option>
															<option value ="2"> R1 - R2 </option> 
															<option value ="3"> R1 & R2 </option>
															<option value ="4"> R1 & (R2 - R3) </option>			
														</select>
													</span>
												</div>
												<div class="list-group-item">
													<label for="payLegCondiTypeCd">  Condition Type Code </label>
													<span>
														<select class="form-control" id="input-payLegCondiTypeCd" name="payLegCondiTypeCd">
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
													</span>
												</div>
												<div id="payIrInfo1" hidden="true">	
													<div class="list-group-item">
														<label for="payLegIrCd1"> IR Code 1</label>
														<span>														
															<select class="form-control" id = "input-payLegIrCd1">
																<option value = ""> NONE </option>
																<option value = "KRWIRS" selected> KRW IRS </option>
																<option value = "USDIRS"> USD IRS </option>
																<option value = "EURIRS"> EUR IRS </option>
																<option value = "1010000W"> KRW TBOND </option>
															</select>
														</span>
														<span>
															Rate Type
															<select class="form-control" id = "input-payLegIrTypeCd1" name="payLegIrTypeCd1">
																<option value = "SPOT"> ZERO </option>
																<option value = "SWAP"> SWAP </option>	
																<option value = "RMS"> RMS </option>											
															</select>
														</span>
														<span>
															Tenor
															<input class="form-control" type="text" id="input-payLegIrTenor1" name="payLegIrTenor1" value ="M3">
														</span>
														<span>
															Swap Coupon Frequency
															<select class="form-control" id = "input-payLegIrCouponFreq1" name="payLegIrCouponFreq1">
																<option value = "Q"> Quarterly </option>
																<option value = "S"> Semi-Annually </option>
																<option value = "A"> Annually </option>				
															</select>
														</span>
													</div>												
												</div>
												<div id="payIrInfo2" hidden="true">	
													<div class="list-group-item">
														<label for="payLegIrCd2"> IR Code 2</label>
														<span>														
															<select class="form-control" id = "input-payLegIrCd2" name="payLegIrCd2">
																<option value = ""> NONE </option>
																<option value = "KRWIRS" selected> KRW IRS </option>
																<option value = "USDIRS"> USD IRS </option>
																<option value = "EURIRS"> EUR IRS </option>
																<option value = "1010000W"> KRW TBOND </option>
															</select>
														</span>
														<span>
															Rate Type
															<select class="form-control" id = "input-payLegIrTypeCd2" name="payLegIrTypeCd2">
																<option value = "SPOT"> ZERO </option>
																<option value = "SWAP"> SWAP </option>	
																<option value = "RMS"> RMS </option>											
															</select>
														</span>
														<span>
															Tenor
															<input class="form-control" type="text" id="input-payLegIrTenor2" name="payLegIrTenor2" value ="M3">
														</span>
														<span>
															Swap Coupon Frequency
															<select class="form-control" id = "input-payLegIrCouponFreq2" name="payLegIrCouponFreq2">
																<option value = "Q"> Quarterly </option>
																<option value = "S"> Semi-Annually </option>
																<option value = "A"> Annually </option>				
															</select>
														</span>
													</div>												
												</div>
												<div id="payIrInfo3" hidden="true">	
													<div class="list-group-item">
														<label for="payLegIrCd3"> IR Code 3</label>
														<span>														
															<select class="form-control" id = "input-payLegIrCd3" name="payLegIrCd3">
																<option value = ""> NONE </option>
																<option value = "KRWIRS" selected> KRW IRS </option>
																<option value = "USDIRS"> USD IRS </option>
																<option value = "EURIRS"> EUR IRS </option>
																<option value = "1010000W"> KRW TBOND </option>
															</select>
														</span>
														<span>
															Rate Type
															<select class="form-control" id = "input-payLegIrTypeCd3" name="payLegIrTypeCd3">
																<option value = "SPOT"> ZERO </option>
																<option value = "SWAP"> SWAP </option>	
																<option value = "RMS"> RMS </option>											
															</select>
														</span>
														<span>
															Tenor
															<input class="form-control" type="text" id="input-payLegIrTenor3" name="payLegIrTenor3" value="M3">
														</span>
														<span>
															Swap Coupon Frequency
															<select class="form-control" id = "input-payLegIrCouponFreq3" name="payLegIrCouponFreq3">
																<option value = "Q"> Quarterly </option>
																<option value = "S"> Semi-Annually </option>
																<option value = "A"> Annually </option>				
															</select>
														</span>
													</div>												
												</div>
												<div class="list-group-item">
													<label for="payLegScheduleGenerator"> Schedule Frequency </label>
													<span>
														<select class="form-control" id="input-payLegScheduleFreq" name="payLegScheduleFreq">
															<option value = "Q"> Quarterly </option>
															<option value = "S"> Semi-Annually </option>
															<option value = "A"> Annually </option>
														</select>
													</span>
												</div>
												<div class="list-group-item">
													<label for="payLegInitSpread"> Initial Spread </label>
													<span>
														<input class="form-control" type="text" id="input-payLegInitSpread" value="0">													
													</span>
													<label for="payLegInitLeverage1"> Initial Leverage1 </label>
													<span>
														<input class="form-control" type="text" id="input-payLegInitLeverage1" value="1">													
													</span>
													<label for="payLegInitLeverage2"> Initial Leverage2 </label>
													<span>
														<input class="form-control" type="text" id="input-payLegInitLeverage2" value="1">														
													</span>
													<label for="payLegInitLeverage3"> Initial Leverage3 </label>
													<span>
														<input class="form-control" type="text" id="input-payLegInitLeverage3" value="1">													
													</span>
													<label for="payLegInitInCoupon"> Initial InCoupon </label>
													<span>
														<input class="form-control" type="text" id="input-payLegInitInCoupon" value="5">														
													</span>
													<label for="payLegInitOutCoupon"> Initial OutCoupon </label>
													<span>
														<input class="form-control" type="text" id="input-payLegInitOutCoupon" value="0">														
													</span>			
													<label for="payLegInitLowerBound1"> Initial LowerBound1 </label>
													<span>
														<input class="form-control" type="text" id="input-payLegInitLowerBound1" value="0">														
													</span>
													<label for="payLegInitUpperBound1"> Initial UpperBound1 </label>
													<span>
														<input class="form-control" type="text" id="input-payLegInitUpperBound1" value="5">														
													</span>
													<label for="payLegInitLowerBound2"> Initial LowerBound2 </label>
													<span>
														<input class="form-control" type="text" id="input-payLegInitLowerBound2" value="0">														
													</span>
													<label for="payLegInitUpperBound2"> Initial UpperBound2 </label>
													<span>
														<input class="form-control" type="text" id="input-payLegInitUpperBound2" value="5">														
													</span>
													<label for="payLegInitCap"> Initial Cap </label>
													<span>
														<input class="form-control" type="text" id="input-payLegInitCap" value="7">														
													</span>
													<label for="payLegInitFloor"> Initial Floor </label>
													<span>
														<input class="form-control" type="text" id="input-payLegInitFloor" value="0">														
													</span>										
												</div>
												<div class="list-group-item table-responsive">
													<button class="btn btn-primary" type ="button" id="payLegScheduleGen_button"> Generate Schedule </button>										
													<table id="payLegScheduleTable" class="table table-bordered table-hover table-striped" name="payLegScheduleTable"></table>
												</div>											
											</div>
										</div>
										<div class="panel-footer">
	                            			Pay Leg
	                       				</div>
									</div>
								</div>	
								<div class="tab-pane fade" id="rcvLegInfo">
									<div class="panel panel-primary form-group">									
										<div class="panel-body">
											<div class="list-group">
												<div class="list-group-item">
													<label for="rcvLegTypeCd">  Leg Type Code(No Select) </label>
													<span><input class="form-control" type="text" id="input-rcvLegTypeCd" name="rcvLegTypeCd" value ="RA"></span>
												</div>
												<div class="list-group-item">				
													<label for="rcvLegNotional"> Notional Principal </label>
													<span><input class="form-control" type="text" id="input-rcvLegNotional" name="rcvLegNotional" value ="10000000000"></span>
												</div>
												<div class="list-group-item">
													<label for="rcvLegCcyCd"> Notional Currency </label>
													<span>
														<select class="form-control" id="input-rcvLegCcyCd" name="rcvLegCcyCd">
															<option value = "KRW"> KRW </option>
															<option value = "USD"> USD </option>
															<option value = "EUR"> EUR </option>
														</select>
													</span>
												</div>
												<div class="list-group-item">
													<label for="rcvLegDCF"> Day Count Fraction </label>
													<span>
														<select class="form-control" id = "input-rcvLegDCF" name="rcvLegDCF">
															<option value = "1"> ACTUAL/365 </option>
															<option value = "2"> ACTUAL/360 </option>
															<option value = "3"> ACTUAL/ACTUAL </option>
															<option value = "4"> 30/360</option>
															<option value = "5"> 30E/360</option>
														</select>
													</span>
												</div>	
												<div class = "list-group-item">												
													<label for="capFloorCd"> Cap/Floor Code </label>
													<span><select class="form-control" id = "input-rcvCapFloorCd" name="rcvLegCapFloorCd">
															<option value = "0"> NONE </option>
															<option value = "1"> Cap Only </option>
															<option value = "2"> Floor Only </option>
															<option value = "3"> Cap & Floor </option>
													</select></span>									
												</div>
												<div class="list-group-item">
													<label for="rcvLegUndTypeCd">  Underlying Type Code </label>
													<span>
														<select class="form-control" id="input-rcvLegUndTypeCd" onchange="changeCondiTypeCdTag(this);" name="rcvLegUndTypeCd">
															<option value ="0"> None </option>
															<option value ="1"> R1 </option>
															<option value ="2"> R1 - R2 </option> 
															<option value ="3"> R1 & R2 </option>
															<option value ="4"> R1 & (R2 - R3) </option>			
														</select>
													</span>
												</div>
												<div class="list-group-item">
													<label for="rcvLegCondiTypeCd">  Condition Type Code </label>
													<span>
														<select class="form-control" id="input-rcvLegCondiTypeCd" name="rcvLegCondiTypeCd">
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
													</span>
												</div>
												<div id="rcvIrInfo1" hidden="true">	
													<div class="list-group-item">
														<label for="rcvLegIrCd1"> IR Code 1</label>
														<span>														
															<select class="form-control" id = "input-rcvLegIrCd1" name="rcvLegIrCd1">
																<option value = ""> NONE </option>
																<option value = "KRWIRS" selected> KRW IRS </option>
																<option value = "USDIRS"> USD IRS </option>
																<option value = "EURIRS"> EUR IRS </option>
																<option value = "1010000W"> KRW TBOND </option>
															</select>
														</span>
														<span>
															Rate Type
															<select class="form-control" id = "input-rcvLegIrTypeCd1" name="rcvLegIrTypeCd1">
																<option value = "SPOT"> ZERO </option>
																<option value = "SWAP"> SWAP </option>	
																<option value = "RMS"> RMS </option>											
															</select>
														</span>
														<span>
															Tenor
															<input class="form-control" type="text" id="input-rcvLegIrTenor1" name="rcvLegIrTenor1" value ="M3">
														</span>
														<span>
															Swap Coupon Frequency
															<select class="form-control" id = "input-rcvLegIrCouponFreq1" name="rcvLegIrCouponFreq1">
																<option value = "Q"> Quarterly </option>
																<option value = "S"> Semi-Annually </option>
																<option value = "A"> Annually </option>				
															</select>
														</span>
													</div>												
												</div>
												<div id="rcvIrInfo2" hidden="true">	
													<div class="list-group-item">
														<label for="rcvLegIrCd2"> IR Code 2</label>
														<span>														
															<select class="form-control" id = "input-rcvLegIrCd2" name="rcvLegIrCd2">
																<option value = ""> NONE </option>
																<option value = "KRWIRS" selected> KRW IRS </option>
																<option value = "USDIRS"> USD IRS </option>
																<option value = "EURIRS"> EUR IRS </option>
																<option value = "1010000W"> KRW TBOND </option>
															</select>
														</span>
														<span>
															Rate Type
															<select class="form-control" id = "input-rcvLegIrTypeCd2" name="rcvLegIrTypeCd2">
																<option value = "SPOT"> ZERO </option>
																<option value = "SWAP"> SWAP </option>	
																<option value = "RMS"> RMS </option>											
															</select>
														</span>
														<span>
															Tenor
															<input class="form-control" type="text" id="input-rcvLegIrTenor2" name="rcvLegIrTenor2" value ="M3">
														</span>
														<span>
															Swap Coupon Frequency
															<select class="form-control" id = "input-rcvLegIrCouponFreq2" name="rcvLegIrCouponFreq2">
																<option value = "Q"> Quarterly </option>
																<option value = "S"> Semi-Annually </option>
																<option value = "A"> Annually </option>				
															</select>
														</span>
													</div>												
												</div>
												<div id="rcvIrInfo3" hidden="true">	
													<div class="list-group-item">
														<label for="rcvLegIrCd3"> IR Code 3</label>
														<span>														
															<select class="form-control" id = "input-rcvLegIrCd3" name="rcvLegIrCd3">
																<option value = ""> NONE </option>
																<option value = "KRWIRS" selected> KRW IRS </option>
																<option value = "USDIRS"> USD IRS </option>
																<option value = "EURIRS"> EUR IRS </option>
																<option value = "1010000W"> KRW TBOND </option>
															</select>
														</span>
														<span>
															Rate Type
															<select class="form-control" id = "input-rcvLegIrTypeCd3" name="rcvLegIrTypeCd3">
																<option value = "SPOT"> ZERO </option>
																<option value = "SWAP"> SWAP </option>	
																<option value = "RMS"> RMS </option>											
															</select>
														</span>
														<span>
															Tenor
															<input class="form-control" type="text" id="input-rcvLegIrTenor3" name="rcvLegIrTenor3" value="M3">
														</span>
														<span>
															Swap Coupon Frequency
															<select class="form-control" id = "input-rcvLegIrCouponFreq3" name="rcvLegIrCouponFreq3">
																<option value = "Q"> Quarterly </option>
																<option value = "S"> Semi-Annually </option>
																<option value = "A"> Annually </option>				
															</select>
														</span>
													</div>												
												</div>
												<div class="list-group-item">
													<label for="rcvLegScheduleGenerator"> Schedule Frequency </label>
													<span>
														<select class="form-control" id="input-rcvLegScheduleFreq" name="rcvLegScheduleFreq">
															<option value = "Q"> Quarterly </option>
															<option value = "S"> Semi-Annually </option>
															<option value = "A"> Annually </option>
														</select>
													</span>
												</div>
												<div class="list-group-item">
													<label for="rcvLegInitSpread"> Initial Spread </label>
													<span>
														<input class="form-control" type="text" id="input-rcvLegInitSpread" value="0">														
													</span>
													<label for="rcvLegInitLeverage1"> Initial Leverage1 </label>
													<span>
														<input class="form-control" type="text" id="input-rcvLegInitLeverage1" value="1">													
													</span>
													<label for="rcvLegInitLeverage2"> Initial Leverage2 </label>
													<span>
														<input class="form-control" type="text" id="input-rcvLegInitLeverage2" value="1">														
													</span>
													<label for="rcvLegInitLeverage3"> Initial Leverage3 </label>
													<span>
														<input class="form-control" type="text" id="input-rcvLegInitLeverage3" value="1">													
													</span>
													<label for="rcvLegInitInCoupon"> Initial InCoupon </label>
													<span>
														<input class="form-control" type="text" id="input-rcvLegInitInCoupon" value="5">														
													</span>
													<label for="rcvLegInitOutCoupon"> Initial OutCoupon </label>
													<span>
														<input class="form-control" type="text" id="input-rcvLegInitOutCoupon" value="0">														
													</span>													
													<label for="rcvLegInitLowerBound1"> Initial LowerBound1 </label>
													<span>
														<input class="form-control" type="text" id="input-rcvLegInitLowerBound1" value="0">														
													</span>
													<label for="rcvLegInitUpperBound1"> Initial UpperBound1 </label>
													<span>
														<input class="form-control" type="text" id="input-rcvLegInitUpperBound1" value="5">														
													</span>
													<label for="rcvLegInitLowerBound2"> Initial LowerBound2 </label>
													<span>
														<input class="form-control" type="text" id="input-rcvLegInitLowerBound2" value="0">														
													</span>
													<label for="rcvLegInitUpperBound2"> Initial UpperBound2 </label>
													<span>
														<input class="form-control" type="text" id="input-rcvLegInitUpperBound2" value="5">														
													</span>
													<label for="rcvLegInitCap"> Initial Cap </label>
													<span>
														<input class="form-control" type="text" id="input-rcvLegInitCap" value="7">														
													</span>
													<label for="rcvLegInitFloor"> Initial Floor </label>
													<span>
														<input class="form-control" type="text" id="input-rcvLegInitFloor" value="0">														
													</span>		
												</div>
												<div class="list-group-item table-responsive">
													<button class="btn btn-primary" type ="button" id="rcvLegScheduleGen_button"> Generate Schedule </button>										
													<table id="rcvLegScheduleTable" class="table table-bordered table-hover table-striped" name="rcvLegScheduleTable"></table>
												</div>											
											</div>
										</div>
										<div class="panel-footer">
	                            			Receive Leg
	                       				</div>
									</div>
								</div>
								<div class="tab-pane fade" id="exerciseInfo">
									<div class="panel panel-primary form-group">									
										<div class="panel-body">
											<div class="list-group">
												<div class="list-group-item">
													<label for="exerciseScheduleGenerator"> Schedule Generate </label>
													<span>
														<select class="form-control" id = "input-exerciseScheduleFreq" name = "exerciseScheduleFreq">
															<option value = "Q"> Quarterly </option>
															<option value = "S"> Semi-Annually </option>
															<option value = "A"> Annually </option>
														</select>
													</span>	
												</div>
												<div class="list-group-item">
													<label for="nonCallYear"> NonCall Year </label>
													<span>
														<input class="form-control" type="text" id="input-noncallExerYear" name="noncallExerYear" value ="1">
													</span>
												</div>
												<div class = "list-group-item table-responsive">
													<button class="btn btn-primary" type ="button" id="exerciseScheduleGen_button"> Generate Schedule </button>									
													<table id="exerciseScheduleTable" class="table table-bordered table-hover table-striped"></table>
												</div>
											</div>
										</div>
									</div>								
								</div>
								<div class="tab-pane fade" id="execution">
									<button type="submit" class="btn btn-success"> Execute </button>
								</div>
							</div>						
						</div>					
					</form>
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
	
	<!-- java scripts -->	
	<script type="text/javascript">
	$(function(){
		$('#payLegScheduleGen_button').click(function(){
			var issueDt = $('#input-issueDt').val().replace(/-/gi,"");
			var mrtyDt = $('#input-mrtyDt').val().replace(/-/gi,"");
			var couponFreq = $('#input-payLegScheduleFreq').val();
			var jsonStr = './json/scheduleList?issueDt=' + issueDt + '&mrtyDt=' + mrtyDt + '&couponFreq=' + couponFreq;
			
			$.getJSON(jsonStr, function(data){
				buildHtmlTable('#payLegScheduleTable', data, 'pay')
			});				
		});	
		$('#rcvLegScheduleGen_button').click(function(){
			var issueDt = $('#input-issueDt').val().replace(/-/gi,"");
			var mrtyDt = $('#input-mrtyDt').val().replace(/-/gi,"");
			var couponFreq = $('#input-rcvLegScheduleFreq').val();
			var jsonStr = './json/scheduleList?issueDt=' + issueDt + '&mrtyDt=' + mrtyDt + '&couponFreq=' + couponFreq;
						
			$.getJSON(jsonStr, function(data){
				buildHtmlTable('#rcvLegScheduleTable', data, 'rcv')
			});
		});
		
		$('#exerciseScheduleGen_button').click(function(){
			var issueDt = $('#input-issueDt').val().replace(/-/gi,"");
			var mrtyDt = $('#input-mrtyDt').val().replace(/-/gi,"");
			var couponFreq = $('#input-rcvLegScheduleFreq').val();
			var noncallYear = $('#input-noncallExerYear').val();
			var jsonStr = './json/scheduleList?issueDt=' + issueDt + '&mrtyDt=' + mrtyDt + '&couponFreq=' + couponFreq + '&noncallYear=' + noncallYear;
						
			$.getJSON(jsonStr, function(data){
				buildHtmlTable('#exerciseScheduleTable', data, 'exercise')
			});
		});
		
	});
	
	function genCapFloorContents(tagCode, capFloorTypeCd){
		//tagCode: pay/rcv, capFloorTypeCd: capFloor Code;
		var resultTag = $("<div/>");
		var capTag = $("<input/>").attr({
			'type' : 'text',
			'class' : 'inputRate',
			'name' : tagCode + 'cap'			
		});
		var floorTag = $("<input/>").attr({
			'type' : 'text',
			'class' : 'inputRate',
			'name' : tagCode + 'floor'
		});
		var initCap = $('#input-' + tagCode + "LegInitCap").val();
		var initFloor = $('#input-' + tagCode + "LegInitFloor").val();
		
		
		if (capFloorTypeCd == 0){
			//NONE		
			resultTag.append(capTag.attr({'hidden': 'true'}));
			resultTag.append(floorTag.attr({'hidden': 'true'}));
			
		} else if (capFloorTypeCd == 1){
			//Cap Only
			resultTag.append("min(Coupon, ");
			resultTag.append(capTag.attr({'value': initCap}));
			resultTag.append("%)");
			resultTag.append(floorTag.attr({'hidden': 'true'}))
		} else if (capFloorTypeCd == 2){
			//Floor Only
			resultTag.append("max(Coupon, ");
			resultTag.append(floorTag.attr({'value' : initFloor}));
			resultTag.append("%)");
			resultTag.append(capTag.attr({'hidden': 'true'}));
		} else if (capFloorTypeCd == 3){
			//Cap & Floor
			resultTag.append("min[max(Coupon, ");
			resultTag.append(floorTag.attr({'value' : initFloor}));
			resultTag.append("%), ");
			resultTag.append(capTag.attr({'value': initCap}));
			resultTag.append("%]");
		}
		
		return resultTag;
	};
	
	function genConditionContents(tagCode, condiTypeCd){
		//tagCode: pay/rcv, condiTypeCd: condition Type Code	
		var resultTag = $("<div/>");
		var lowerLimit1Tag = $("<input/>").attr({
			'type' : 'text',
			'class' : 'inputRate',
			'name' : tagCode + "LowerLimit1"
		});
		var lowerLimit2Tag = $("<input/>").attr({
			'type' : 'text',
			'class' : 'inputRate',
			'name' : tagCode + "LowerLimit2"
		});
		var upperLimit1Tag = $("<input/>").attr({
			'type' : 'text',
			'class' : 'inputRate',
			'name' : tagCode + "UpperLimit1"
		});
		var upperLimit2Tag = $("<input/>").attr({
			'type' : 'text',
			'class' : 'inputRate',
			'name' : tagCode + "UpperLimit2"
		});
		var initLowerBound1 = $('#input-' + tagCode + "LegInitLowerBound1").val();
		var initUpperBound1 = $('#input-' + tagCode + "LegInitUpperBound1").val();
		var initLowerBound2 = $('#input-' + tagCode + "LegInitLowerBound2").val();
		var initUpperBound2 = $('#input-' + tagCode + "LegInitUpperBound2").val();
		
		if (condiTypeCd == 0){
			//NONE		
			resultTag.append("No Condition");
			resultTag.append(lowerLimit1Tag.attr({'hidden' : 'true'}));
			resultTag.append(upperLimit1Tag.attr({'hidden' : 'true'}));
			resultTag.append(lowerLimit2Tag.attr({'hidden' : 'true'}));
			resultTag.append(upperLimit2Tag.attr({'hidden' : 'true'}));
		} else if (condiTypeCd == 1){
			//R1
			resultTag.append(lowerLimit1Tag.attr({'value' : initLowerBound1}));
			resultTag.append("% < R1 < ");
			resultTag.append(upperLimit1Tag.attr({'value' : initUpperBound1}));
			resultTag.append("%");
			resultTag.append(lowerLimit2Tag.attr({'hidden' : 'true'}));
			resultTag.append(upperLimit2Tag.attr({'hidden' : 'true'}));
		} else if (condiTypeCd == 2){
			//R2
			resultTag.append(lowerLimit1Tag.attr({'value' : initLowerBound1}));
			resultTag.append("% < R2 < ");
			resultTag.append(upperLimit1Tag.attr({'value' : initUpperBound1}));
			resultTag.append("%");
			resultTag.append(lowerLimit2Tag.attr({'hidden' : 'true'}));
			resultTag.append(upperLimit2Tag.attr({'hidden' : 'true'}));			
		} else if (condiTypeCd == 3){
			//R3
			resultTag.append(lowerLimit1Tag.attr({'value' : initLowerBound1}));
			resultTag.append("% < R3 < ");
			resultTag.append(upperLimit1Tag.attr({'value' : initUpperBound1}));
			resultTag.append("%");
			resultTag.append(lowerLimit2Tag.attr({'hidden' : 'true'}));
			resultTag.append(upperLimit2Tag.attr({'hidden' : 'true'}));
		} else if (condiTypeCd == 4){
			//R1-R2
			resultTag.append(lowerLimit1Tag.attr({'value' : initLowerBound1}));
			resultTag.append("% < R1 - R2 < ");
			resultTag.append(upperLimit1Tag.attr({'value' : initUpperBound1}));
			resultTag.append("%");
			resultTag.append(lowerLimit2Tag.attr({'hidden' : 'true'}));
			resultTag.append(upperLimit2Tag.attr({'hidden' : 'true'}));
		} else if (condiTypeCd == 5){
			//R2-R3
			resultTag.append(lowerLimit1Tag.attr({'value' : initLowerBound1}));
			resultTag.append("% < R2 - R3 < ");
			resultTag.append(upperLimit1Tag.attr({'value' : initUpperBound1}));
			resultTag.append("%");
			resultTag.append(lowerLimit2Tag.attr({'hidden' : 'true'}));
			resultTag.append(upperLimit2Tag.attr({'hidden' : 'true'}));
		} else if (condiTypeCd == 6){
			//R1 & R2
			resultTag.append(lowerLimit1Tag.attr({'value' : initLowerBound1}));
			resultTag.append("% < R1 < ");
			resultTag.append(upperLimit1Tag.attr({'value' : initUpperBound1}));
			resultTag.append("%, ");
			resultTag.append(lowerLimit2Tag.attr({'value' : initLowerBound2}));
			resultTag.append("% < R2 < ");
			resultTag.append(upperLimit2Tag.attr({'value' : initUpperBound2}));
			resultTag.append("%");
		} else if (condiTypeCd == 7){
			//R1 & R3
			resultTag.append(lowerLimit1Tag.attr({'value' : initLowerBound1}));
			resultTag.append("% < R1 < ");
			resultTag.append(upperLimit1Tag.attr({'value' : initUpperBound1}));
			resultTag.append("%, ");
			resultTag.append(lowerLimit2Tag.attr({'value' : initLowerBound2}));
			resultTag.append("% < R3 < ");
			resultTag.append(upperLimit2Tag.attr({'value' : initUpperBound2}));
			resultTag.append("%");		
		} else if (condiTypeCd == 8){
			//R2 & R3
			resultTag.append(lowerLimit1Tag.attr({'value' : initLowerBound1}));
			resultTag.append("% < R2 < ");
			resultTag.append(upperLimit1Tag.attr({'value' : initUpperBound1}));
			resultTag.append("%, ");
			resultTag.append(lowerLimit2Tag.attr({'value' : initLowerBound2}));
			resultTag.append("% < R3 < ");
			resultTag.append(upperLimit2Tag.attr({'value' : initUpperBound2}));
			resultTag.append("%");
		} else if (condiTypeCd == 9){
			//R1 & (R2- R3)
			resultTag.append(lowerLimit1Tag.attr({'value' : initLowerBound1}));
			resultTag.append("% < R1 < ");
			resultTag.append(upperLimit1Tag.attr({'value' : initUpperBound1}));
			resultTag.append("%, ");
			resultTag.append(lowerLimit2Tag.attr({'value' : initLowerBound2}));
			resultTag.append("% < R2 - R3 < ");
			resultTag.append(upperLimit2Tag.attr({'value' : initUpperBound2}));
			resultTag.append("%");
		}
		
		return resultTag;
	}
	
	function genCouponContents(tagCode, undTypeCd, typeCd){
		//tagCode: pay/rcv, undTypeCd: underlying Type Code, typeCd: Coupon Type Code
		var resultTag = $("<div/>").attr({'display': 'inline'});
		var initSpread = $('#input-' + tagCode + "LegInitSpread").val();
		var initLeverage1 = $('#input-' + tagCode + "LegInitLeverage1").val();
		var initLeverage2 = $('#input-' + tagCode + "LegInitLeverage2").val();
		var initLeverage3 = $('#input-' + tagCode + "LegInitLeverage3").val();
		var initInCoupon = $('#input-' + tagCode + "LegInitInCoupon").val();
		var initOutCoupon = $('#input-' + tagCode + "LegInitOutCoupon").val();
		
		//Underlying Tag
		var leverageTag1 = $('<input/>').attr({
			'type' : 'text',
			'class' : 'inputLeverage',
			'name' : tagCode + "Leverage1"
		});
		var leverageTag2 = $('<input/>').attr({
			'type' : 'text',
			'class' : 'inputLeverage',
			'name' : tagCode + "Leverage2"
		});
		var leverageTag3 = $('<input/>').attr({
			'type' : 'text',
			'class' : 'inputLeverage',
			'name' : tagCode + "Leverage3"
		});
		var underlyingTag = $('<div/>').attr({'display': 'inline'});
		if (undTypeCd == 0 || typeCd == 2 || typeCd == 4){
			//NONE				
			underlyingTag.append(leverageTag1.attr({'hidden' : 'true', 'value' : '0'}));
			underlyingTag.append(leverageTag2.attr({'hidden' : 'true', 'value' : '0'}));
			underlyingTag.append(leverageTag3.attr({'hidden' : 'true', 'value' : '0'}));
		} else if (undTypeCd == 1){
			//R1
			underlyingTag.append(leverageTag1.attr({'value' : '1'}));
			underlyingTag.append(" x R1 + ");
			underlyingTag.append(leverageTag2.attr({'hidden' : 'true', 'value' : '0'}));
			underlyingTag.append(leverageTag3.attr({'hidden' : 'true', 'value' : '0'}));
		} else if (undTypeCd == 2){
			//R1 - R2
			underlyingTag.append("(");
			underlyingTag.append(leverageTag1.attr({'value' : initLeverage1}));
			underlyingTag.append(" x R1 - ");
			underlyingTag.append(leverageTag2.attr({'value' : initLeverage2}));
			underlyingTag.append(" x R2) + ");
			underlyingTag.append(leverageTag3.attr({'hidden' : 'true', 'value' : '0'}));
		} else if (undTypeCd == 3){
			//R1 & R2
			underlyingTag.append(leverageTag1.attr({'value' : initLeverage1}));
			underlyingTag.append(" x R1 & ");
			underlyingTag.append(leverageTag2.attr({'value' : initLeverage2}));
			underlyingTag.append(" x R2 + ");
			underlyingTag.append(leverageTag3.attr({'hidden' : 'true', 'value' : '0'}));
		} else if (undTypeCd == 4){
			//R1 & (R2 - R3)
			underlyingTag.append(leverageTag1.attr({'value' : initLeverage1}));
			underlyingTag.append(" x R1 & (");
			underlyingTag.append(leverageTag2.attr({'value' : initLeverage2}));
			underlyingTag.append(" x R2 - ");
			underlyingTag.append(leverageTag3.attr({'value' : initLeverage3}));
			underlyingTag.append(" x R3) + ");
		}
		//Spread Tag
		var spreadTag = $("<input/>").attr({
			'type' : 'text',
			'class' : 'inputSpread',
			'name' : tagCode + 'Spread'
		});		
		//InCoupon, OutCoupon Tag
		var inCouponTag = $("<input/>").attr({
			'type' : 'text',
			'class' : 'inputRate',
			'name' : tagCode + 'InCouponRate'
		});
		var outCouponTag = $("<input/>").attr({
			'type' : 'text',
			'class' : 'inputRate',
			'name' : tagCode + 'OutCouponRate'
		});
		
		//Combine
		if (typeCd == 1){
			//RESET		
			if (undTypeCd == 1){
				resultTag.append(underlyingTag);
				resultTag.append(spreadTag.attr({'value' : initSpread}));
				resultTag.append(" %");
				resultTag.append(inCouponTag.attr({'hidden' : 'true', 'value': '0'}));
				resultTag.append(outCouponTag.attr({'hidden' : 'true', 'value': '0'}));		
			} else {
				resultTag.append(underlyingTag);
				resultTag.append($('<div/>').attr({'class' : 'alert-danger'})
											.html("Choose the correct Underlying Type Code."));
				resultTag.append(spreadTag.attr({'hidden' : 'true', 'value' : '0'}));
				resultTag.append(inCouponTag.attr({'hidden' : 'true', 'value': '0'}));
				resultTag.append(outCouponTag.attr({'hidden' : 'true', 'value': '0'}));
			}			 
		} else if (typeCd == 2){
			//ACCRUAL			
			resultTag.append(underlyingTag);
			resultTag.append("Satisfied: ");
			resultTag.append(inCouponTag.attr({'value' : initInCoupon}));
			resultTag.append("%, Otherwise: ");
			resultTag.append(outCouponTag.attr({'value' : initOutCoupon}));
			resultTag.append("%");
			resultTag.append(spreadTag.attr({'hidden' : 'true', 'value' : '0'}));
		} else if (typeCd == 3){
			//AVERAGE			
			if (undTypeCd == 1 || undTypeCd == 2){
				resultTag.append("AVG[");
				resultTag.append(underlyingTag);
				resultTag.append(spreadTag.attr({'value': initSpread}));
				resultTag.append("] %");
				resultTag.append(inCouponTag.attr({'hidden' : 'true', 'value': '0'}));
				resultTag.append(outCouponTag.attr({'hidden' : 'true', 'value': '0'}));	
			} else {
				resultTag.append(underlyingTag);
				resultTag.append($('<div/>').attr({'class' : 'alert-danger'})
						.html("Choose the correct Underlying Type Code."));
				resultTag.append(spreadTag.attr({'hidden' : 'true', 'value' : '0'}));
				resultTag.append(inCouponTag.attr({'hidden' : 'true', 'value': '0'}));
				resultTag.append(outCouponTag.attr({'hidden' : 'true', 'value': '0'}));
			}			 
		} else if (typeCd == 4){
			//FIXED			
			resultTag.append(underlyingTag);
			resultTag.append(spreadTag.attr({'value': initSpread}));
			resultTag.append(" %");
			resultTag.append(inCouponTag.attr({'hidden' : 'true', 'value': '0'}));
			resultTag.append(outCouponTag.attr({'hidden' : 'true', 'value': '0'}));						 		
		}
		return resultTag;
	}
	
	function changeExerciseTag(selector){
		var typeCd = selector.value;
		if (typeCd == "NONE"){
			$('#exerciseDiv').css("visibility", "hidden");
			$('#exerciseScheduleTable').empty();
		} else {
			$('#exerciseDiv').css("visibility", "visible");
		}
	}
	
	function changeCondiTypeCdTag(selector){
		var typeCd = selector.value;
		var selectorId = selector.id;
		var tagCode1 = selectorId.substring(6,9);
		var targetTagStr = "#input-" + tagCode1 + "LegCondiTypeCd";
		var targetTag = $(targetTagStr);
		if (typeCd == 0){
			//None
			targetTag[0][0].disabled = false;
			targetTag[0][1].disabled = true;
			targetTag[0][2].disabled = true;
			targetTag[0][3].disabled = true;
			targetTag[0][4].disabled = true;
			targetTag[0][5].disabled = true;
			targetTag[0][6].disabled = true;
			targetTag[0][7].disabled = true;
			targetTag[0][8].disabled = true;
			targetTag[0][9].disabled = true;			
			$("#" + tagCode1 + "IrInfo1")[0].hidden = true;
			$("#" + tagCode1 + "IrInfo2")[0].hidden = true;
			$("#" + tagCode1 + "IrInfo3")[0].hidden = true;
			$("#input-" + tagCode1 + "LegIrCd1").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrCd2").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrCd3").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrTypeCd1").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrTypeCd2").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrTypeCd3").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrTenor1").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrTenor2").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrTenor3").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrCouponFreq1").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrCouponFreq2").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrCouponFreq3").removeAttr("name");
		} else if (typeCd == 1){
			//R1
			targetTag[0][0].disabled = false;
			targetTag[0][1].disabled = false;
			targetTag[0][2].disabled = true;
			targetTag[0][3].disabled = true;
			targetTag[0][4].disabled = true;
			targetTag[0][5].disabled = true;
			targetTag[0][6].disabled = true;
			targetTag[0][7].disabled = true;
			targetTag[0][8].disabled = true;
			targetTag[0][9].disabled = true;
			$("#" + tagCode1 + "IrInfo1")[0].hidden = false;
			$("#" + tagCode1 + "IrInfo2")[0].hidden = true;
			$("#" + tagCode1 + "IrInfo3")[0].hidden = true;
			$("#input-" + tagCode1 + "LegIrCd1").attr("name", tagCode1 + "LegIrCd1");			
			$("#input-" + tagCode1 + "LegIrCd2").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrCd3").removeAttr("name");	
			$("#input-" + tagCode1 + "LegIrTypeCd1").attr("name", tagCode1 + "LegIrTypeCd1");
			$("#input-" + tagCode1 + "LegIrTypeCd2").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrTypeCd3").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrTenor1").attr("name", tagCode1 + "LegIrTenor1");
			$("#input-" + tagCode1 + "LegIrTenor2").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrTenor3").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrCouponFreq1").attr("name", tagCode1 + "LegIrCouponFreq1");
			$("#input-" + tagCode1 + "LegIrCouponFreq2").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrCouponFreq3").removeAttr("name");
		} else if (typeCd == 2){
			//R1 - R2
			targetTag[0][0].disabled = false;
			targetTag[0][1].disabled = false;
			targetTag[0][2].disabled = false;
			targetTag[0][3].disabled = true;
			targetTag[0][4].disabled = false;
			targetTag[0][5].disabled = true;
			targetTag[0][6].disabled = true;
			targetTag[0][7].disabled = true;
			targetTag[0][8].disabled = true;
			targetTag[0][9].disabled = true;
			$("#" + tagCode1 + "IrInfo1")[0].hidden = false;
			$("#" + tagCode1 + "IrInfo2")[0].hidden = false;
			$("#" + tagCode1 + "IrInfo3")[0].hidden = true;
			$("#input-" + tagCode1 + "LegIrCd1").attr("name", tagCode1 + "LegIrCd1");
			$("#input-" + tagCode1 + "LegIrCd2").attr("name", tagCode1 + "LegIrCd2");		
			$("#input-" + tagCode1 + "LegIrCd3").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrTypeCd1").attr("name", tagCode1 + "LegIrTypeCd1");
			$("#input-" + tagCode1 + "LegIrTypeCd2").attr("name", tagCode1 + "LegIrTypeCd2");
			$("#input-" + tagCode1 + "LegIrTypeCd3").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrTenor1").attr("name", tagCode1 + "LegIrTenor1");
			$("#input-" + tagCode1 + "LegIrTenor2").attr("name", tagCode1 + "LegIrTenor2");
			$("#input-" + tagCode1 + "LegIrTenor3").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrCouponFreq1").attr("name", tagCode1 + "LegIrCouponFreq1");
			$("#input-" + tagCode1 + "LegIrCouponFreq2").attr("name", tagCode1 + "LegIrCouponFreq2");
			$("#input-" + tagCode1 + "LegIrCouponFreq3").removeAttr("name");
		} else if (typeCd == 3){
			//R1 & R2
			targetTag[0][0].disabled = false;
			targetTag[0][1].disabled = false;
			targetTag[0][2].disabled = false;
			targetTag[0][3].disabled = true;
			targetTag[0][4].disabled = true;
			targetTag[0][5].disabled = true;
			targetTag[0][6].disabled = false;
			targetTag[0][7].disabled = true;
			targetTag[0][8].disabled = true;
			targetTag[0][9].disabled = true;	
			$("#" + tagCode1 + "IrInfo1")[0].hidden = false;
			$("#" + tagCode1 + "IrInfo2")[0].hidden = false;
			$("#" + tagCode1 + "IrInfo3")[0].hidden = true;
			$("#input-" + tagCode1 + "LegIrCd1").attr("name", tagCode1 + "LegIrCd1");
			$("#input-" + tagCode1 + "LegIrCd2").attr("name", tagCode1 + "LegIrCd2");		
			$("#input-" + tagCode1 + "LegIrCd3").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrTypeCd1").attr("name", tagCode1 + "LegIrTypeCd1");
			$("#input-" + tagCode1 + "LegIrTypeCd2").attr("name", tagCode1 + "LegIrTypeCd2");
			$("#input-" + tagCode1 + "LegIrTypeCd3").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrTenor1").attr("name", tagCode1 + "LegIrTenor1");
			$("#input-" + tagCode1 + "LegIrTenor2").attr("name", tagCode1 + "LegIrTenor2");
			$("#input-" + tagCode1 + "LegIrTenor3").removeAttr("name");
			$("#input-" + tagCode1 + "LegIrCouponFreq1").attr("name", tagCode1 + "LegIrCouponFreq1");
			$("#input-" + tagCode1 + "LegIrCouponFreq2").attr("name", tagCode1 + "LegIrCouponFreq2");
			$("#input-" + tagCode1 + "LegIrCouponFreq3").removeAttr("name");
		} else if (typeCd == 4){
			//R1 & (R2 - R3)
			targetTag[0][0].disabled = false;
			targetTag[0][1].disabled = false;
			targetTag[0][2].disabled = false;
			targetTag[0][3].disabled = false;
			targetTag[0][4].disabled = true;
			targetTag[0][5].disabled = false;
			targetTag[0][6].disabled = true;
			targetTag[0][7].disabled = true;
			targetTag[0][8].disabled = true;
			targetTag[0][9].disabled = false;
			$("#" + tagCode1 + "IrInfo1")[0].hidden = false;
			$("#" + tagCode1 + "IrInfo2")[0].hidden = false;
			$("#" + tagCode1 + "IrInfo3")[0].hidden = false;
			$("#input-" + tagCode1 + "LegIrCd1").attr("name", tagCode1 + "LegIrCd1");
			$("#input-" + tagCode1 + "LegIrCd2").attr("name", tagCode1 + "LegIrCd2");
			$("#input-" + tagCode1 + "LegIrCd3").attr("name", tagCode1 + "LegIrCd3");
			$("#input-" + tagCode1 + "LegIrTypeCd1").attr("name", tagCode1 + "LegIrTypeCd1");
			$("#input-" + tagCode1 + "LegIrTypeCd2").attr("name", tagCode1 + "LegIrTypeCd2");
			$("#input-" + tagCode1 + "LegIrTypeCd3").attr("name", tagCode1 + "LegIrTypeCd3");
			$("#input-" + tagCode1 + "LegIrTenor1").attr("name", tagCode1 + "LegIrTenor1");
			$("#input-" + tagCode1 + "LegIrTenor2").attr("name", tagCode1 + "LegIrTenor2");
			$("#input-" + tagCode1 + "LegIrTenor3").attr("name", tagCode1 + "LegIrTenor3");
			$("#input-" + tagCode1 + "LegIrCouponFreq1").attr("name", tagCode1 + "LegIrCouponFreq1");
			$("#input-" + tagCode1 + "LegIrCouponFreq2").attr("name", tagCode1 + "LegIrCouponFreq2");
			$("#input-" + tagCode1 + "LegIrCouponFreq3").attr("name", tagCode1 + "LegIrCouponFreq3");
		}		
	}	
	
	function changeCouponTag(selector){
		var typeCd = selector.value;
		var selectorId = selector.id;
		var tagCode1 = selectorId.substring(0,3);
		var tagCode2 = selectorId.substring(selectorId.length - 2, selectorId.length);
		
		var undTypeCdTagStr = "#input-" + tagCode1 + "LegUndTypeCd option:selected";
		var undTypeCdStr = $(undTypeCdTagStr).text();
		var undTypeCd = $(undTypeCdTagStr).val();		
		var count = $('#' + tagCode1 + 'LegScheduleTable tr').length;
		for (var index = Number(tagCode2); index < count - 1; index++){
			var resultStr = genCouponContents(tagCode1, undTypeCd, typeCd);
			var indexStr = index;
			if (index < 10){
				indexStr = "0" + index;
			}
			var targetCouponTagStr = "#" + tagCode1 + "Coupon" + indexStr;		
			$(targetCouponTagStr).html(resultStr);	
			var typeCdTagStr = "#" + tagCode1 + "CouponType" + indexStr;
			$(typeCdTagStr)[0][selector.selectedIndex].setAttribute(
					"selected","selected");			
		}			
	}
	
	// Builds the HTML Table out of myList.
	function buildHtmlTable(selector, myList, indicator) {
		$(selector).empty();
	    var columns = addAllColumnHeaders(selector, myList, indicator);
	    var tagCode = selector.substring(1,4);	   
	    var undTypeCd = $("#input-" + tagCode + "LegUndTypeCd option:selected").val();
	    var condiTypeCd = $("#input-" + tagCode + "LegCondiTypeCd option:selected").val();
	    var capFloorTypeCd = $("#input-" + tagCode + "CapFloorCd option:selected").val();
	    
	    for (var rowIndex = 0 ; rowIndex < myList.length ; rowIndex++) {
	        var row$ = $('<tr/>');
	        var rowIndexStr = "";
	        if (rowIndex < 10){
	        	rowIndexStr = "0" + rowIndex;
	        } else {
	        	rowIndexStr = rowIndex;
	        }
	        for (var colIndex = 0 ; colIndex < columns.length ; colIndex++) {
	        	var columnValue = columns[colIndex];	        	
	            var cellValue = myList[rowIndex][columnValue];
	            var td$ = $('<td/>');
	            
	            if (columnValue == "CouponType"){	            	
	            	var contents$ = $('<select/>').attr({
	            		'class': 'form-control', 
	            		'name': indicator + columnValue,
	            		'id': indicator + columnValue + rowIndexStr,
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
	            	//.attr('disabled', true)
	            	
	            	td$.html(contents$);
	            } else if (columnValue == "TypeCd"){
	            	var contents$ = $('<select/>').attr({
	            		'class': 'form-control smaller', 
	            		'name': indicator + columnValue
	            	});
	            	
	            	contents$.append($('<option>',{value: "NONE", text: 'NONE'}));
	            	contents$.append($('<option>',{value: "C", text: 'Call'}));
	            	contents$.append($('<option>',{value: "P", text: 'Put'}));	            	
	            	var optionCd = $('#input-optionTypeCd option:selected').val();
	            	if (optionCd == "NONE"){
	            		//None
	            		contents$[0][0].setAttribute("selected","selected");
	            	} else if (optionCd == "C"){
	            		//Call
	            		contents$[0][1].setAttribute("selected","selected");
	            	} else if (optionCd == "P"){
	            		//Put
	            		contents$[0][2].setAttribute("selected","selected");
	            	}
	            	td$.html(contents$);
	            } else if (columnValue == "StrtDt"){
	            	cellValue = myList[rowIndex]["endDate"];
	            	var contents$ = $('<input/>').attr({
	            		'type':'text',
	            		'value': cellValue,
	            		'class': 'form-control smaller',
	            		'name': indicator + columnValue
	            	});
	            	td$.html(contents$);
	            } else if (columnValue == "EndDt"){
            		cellValue = myList[rowIndex]["endDate"];
	            	var contents$ = $('<input/>').attr({
	            		'type':'text',
	            		'value': cellValue,
	            		'class': 'form-control smaller',
	            		'name': indicator + columnValue
	            	});
	            	td$.html(contents$);
	            } else if (columnValue == "Coupon"){	    
	            	td$.attr({
	            		'id' : indicator + "Coupon" + rowIndexStr
	            	});
	            	if (undTypeCd == 0){
	        			//NONE
	            		typeCd = 4;
	        		} else if (undTypeCd == 1){
	        			//R1	
	        			typeCd = 1;
	        		} else if (undTypeCd == 2){
	        			//R1 - R2
	        			typeCd = 2;
	        		} else if (undTypeCd == 3){
	        			//R1 & R2
	        			typeCd = 2;
	        		} else if (undTypeCd == 4){
	        			//R1 & (R2 - R3)
	        			typeCd = 2;
	        		}
	            	
	            	var contents$ = genCouponContents(tagCode, undTypeCd, typeCd);
	            	td$.html(contents$);
	            } else if (columnValue == "Condition"){
	            	td$.attr({
	            		'id' : indicator + "Condition" + rowIndexStr
	            	});
	            	var contents$ = genConditionContents(tagCode, condiTypeCd);
	            	if (condiTypeCd == 0){
	            		td$.html(contents$).attr({'hidden': true});	
	            	} else {
	            		td$.html(contents$);
	            	}
	            } else if (columnValue == "CapFloor"){
	            	td$.attr({
	            		'id' : indicator + "CapFloor" + rowIndexStr
	            	});
	            	var contents$ = genCapFloorContents(tagCode, capFloorTypeCd);
	            	if (capFloorTypeCd == 0){
	            		td$.html(contents$).attr({'hidden': true});
	            	} else {
	            		td$.html(contents$);	
	            	}
	            } else if (columnValue == "Strike"){
	            	var contents$ = $('<input/>').attr({
	            		'type':'text',
	            		'value': 1,
	            		'class': 'form-control smaller',
	            		'name': indicator + columnValue
	            	});
	            	td$.html(contents$);
	            } else {
	            	var contents$ = $('<input/>').attr({
	            		'type':'text',
	            		'value': cellValue,
	            		'class': 'form-control smaller',
	            		'name': indicator + columnValue
	            	});
	            	td$.html(contents$);
	            }
	            
	            row$.append(td$);
	        }
	        $(selector).append(row$);
	    }
	};

	// Adds a header row to the table and returns the set of columns.
	// Need to do union of keys from all records as some records may not contain
	// all records
	function addAllColumnHeaders(selector, myList, indicator)
	{
	    var columnSet = [];
	    var headerTr$ = $('<tr/>');
	    var tagCode = selector.substring(1,4);
	    
	    if (indicator != "exercise"){
	    	for (var i = 0 ; i < myList.length ; i++) {
		        var rowHash = myList[i];
		        for (var key in rowHash) {
		            if ($.inArray(key, columnSet) == -1){
		                columnSet.push(key);
		                headerTr$.append($('<th/>')
		                		.attr({'class' : 'smaller'})
		                		.html(key));
		            }
		        }	        
		    }
	    	//CouponType
		    columnSet.push("CouponType");	    
	        headerTr$.append($('<th/>')	  
	        		.attr({'class' : 'middle'})
	        		.html("Coupon Type"));
	        //Coupon
		    columnSet.push("Coupon");	    
	        headerTr$.append($('<th/>').html("Coupon"));
	        //Cap, Floor
	        var capFloorCd = $('#input-' + tagCode +'CapFloorCd').val();
	        columnSet.push("CapFloor");	    
	        
	        if (capFloorCd == 0){
	        	headerTr$.append($('<th/>').html("CapFloor").attr({'hidden': true}));
	        } else {
	        	headerTr$.append($('<th/>').html("CapFloor"));
	        }
	        //Condition
	        var conditionCd = $('#input-' + tagCode + 'LegCondiTypeCd').val();
	        columnSet.push("Condition");
	        if (conditionCd == 0){
	        	headerTr$.append($('<th/>').html("Condition").attr({'hidden': true}));	
	        } else {
	        	headerTr$.append($('<th/>').html("Condition"));	
	        }	        
	    } else {
	    	//Exercise Start Date
	        columnSet.push("StrtDt");	    
	        headerTr$.append($('<th/>').html("Start Date"));
	        //Exercise End Date
	        columnSet.push("EndDt");	    
	        headerTr$.append($('<th/>').html("End Date"));
	    	//Exercise Type Code
	        columnSet.push("TypeCd");	    
	        headerTr$.append($('<th/>').html("Exercise Type"));
	        //Exercise Strike
	        columnSet.push("Strike");	    
	        headerTr$.append($('<th/>').html("Exercise Strike"));
	    }   
	    
	    $(selector).append(headerTr$);
	    
	    return columnSet;
	};
		
	</script>
		
</body>
</html>