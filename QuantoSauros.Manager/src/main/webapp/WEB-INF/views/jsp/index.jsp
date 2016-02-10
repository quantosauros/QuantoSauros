<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>

<jsp:include page="./fragments/header.jsp" />

<body>	
	<div id="wrapper">
		<jsp:include page="./fragments/navigator.jsp" />
		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<h1 class="page-header"> AAD Manager </h1>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-12">
					<div class="panel panel-primary form-group">
						<div class="panel-heading">
							<h3 class="panel-title"> 1. AAD Manager </h3>
						</div>
						<div class="panel-body">
							AAD Manager is a system to manage the book of front office, especially for a FICC desk.
							<br>
							The system has a full process of managing a portfolio of a FICC desk.
							<br>						
							<br>
							<li> Register products</li>
							<li> Run the portfolio process to measure risks given scenario</li>
							<li> Check the results from tables, charts, and reports(TO BE).</li>
							<br>
							You can also calculate the individual product by using the PRICER.
							<br>
							<br>
							There are several types I made, Underlying Types, Condition Types, and Coupon Types(You can check those in the PRODUCT REGISTRATION Page).
													
							<h4> # Underlying Type </h4>
							The Underlying Type is a code to determine the form of the underlying assets in the chosen leg.
							<br>
							You could choose R1(which represents Rate1), R1-R2(which represents Rate1 - Rate2), R1&R2(which represents Rate1 and Rate2), and R1 & R2-R3 (which represent Rate1 and Rate2 - Rate3).
							<br>
							For example, if you want to make a FRN(floating rate note), the coupon of FRN is determined by a single reference rate, so you need to choose R1.
							<br>
							Another example, if you want to make a SRA Note(spread range accrual note), the coupon of SRA Note is fixed by a spread between two different reference rates, called R1 and R2.
							<br>
							Therefore, you need to choose R1-R2.
							<br>
							After choosing the underlying type, you can specify details of the product you want to make in the COUPON TYPE.
							 
							<h4> # Condition Type </h4>
							The Condition Type is a code for a range accrual feature. 
							<br>
							If you want to give a 5% coupon only if R1(rate 1) is in the range from 0% to 7%, you should choose Condition Type R1.
							<br>
							However, you need to choose the proper underlying type which include R1 in advance.
							<br>
							After choosing the condition type, you need to GENERATE schedule so that you can give a different condition on each coupon periods.						
							<h4> # Coupon Type </h4>
							The Coupon Type determines a coupon structure of the product you want to make.
							<br>
							There are four different types of the coupon type, RESET, ACCRUAL, AVERAGE, and FIXED.
							<li> The RESET type is that reference rate is calculated on the reset date and the determined reference rate will be payed on the payment date.</li>
							<li> The ACCRUAL type is for a range accrual feature.
							<li> The AVERAGE type represents the average of the corresponding reference rate from start date to end date of the period. </li>
							<li> The FIXED type is to pay the predefined coupon rate. 
							<br>
							You can generate almost all kinds of Interest rate derivatives by combining the types in the PRODUCT REGISTRATION Page.
							<br>
							Here are the examples of product you can make.
							<br>
							<br>
							<li> All types of bonds and swaps </li>						
							<li> Zero Coupon Bond, Coupon Bond </li> 
							<li> IRS </li> 
							<li> Range Accrual Note </li>
							<li> Dual Range Accrual Note </li>
							<li> Spread Dual Range Accrual Note </li>
							<li> Hybrid Dual Range Accrual Note (Stock, FX) </li>
							
						</div>
						<div class="panel-heading">
							<h3 class="panel-title"> 2. Pricing Library</h3>
						</div>
						<div class="panel-body">
							I made a pricing library to cover the interest rate derivatives from vanilla to exotic by using Hull-White 1F and 2F models.
							<br>
							The library is very flexible so that it is able to calculate all kinds of derivatives.							
						</div>
						<div class="panel-heading">
							<h3 class="panel-title"> 3. AAD(Adjoint Algorithmic Differential) Method</h3>
						</div>
						<div class="panel-body">
							AAD (Adjoint Algorithmic Differential) Method is the method of calculating sensitivities of derivatives which is much faster than the traditional method called a BUMP method.
							<br>
							When it comes to calculating sensitivities of interest rate derivatives, there are more than 10 risk factors. (Usually, the term structure has more than 10 tenors.)
							<br>
							In order to calculate a key rate duration of the product by using BUMP method, you need to calculate price 20 times(up and down scenario for each tenors).
							<br>
							If the product is affected by more than 2 term structures, you need to calculate more. It takes hours.
							<br>
							However, AAD Method is much faster than BUMP method and it's precision is the same as the BUMP method.
							<br>
							I implemented the AAD library of interest rate derivatives. And the results of AAD are same as the BUMP. 
							<br>
							And the BUMP method takes 2 hours on the average, however, the AAD method takes 15 min on the average.
							 
							<h4> REFERENCE </h4>
							<a href="http://www.fincad.com/sites/default/files/wysiwyg/Structured%20products%20desks%20join%20the%20AAD%20revolution.pdf" target="_blank">
							Structured products desks join the AAD revolution. - Risk
							</a>				
						</div>
						<div class="panel-heading">
							<h3 class="panel-title"> Contact </h3>
						</div>
						<div class="panel-body">
							Jihoon Lee
							<br>
							jayjlee12@gmail.com
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<jsp:include page="./fragments/footer.jsp" />
			
</body>
</html>