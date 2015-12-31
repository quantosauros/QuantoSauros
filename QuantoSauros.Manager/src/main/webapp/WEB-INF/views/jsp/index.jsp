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
					<div>
						<ul class="nav nav-tabs">
							<li class="active">
								<a href="#home" data-toggle="tab" aria-expanded="true">Home</a>
							</li>
							<li>
								<a href="#profile" data-toggle="tab" aria-expanded="true">Profile</a>
							</li>
						</ul>
						<div class="tab-content">
							<div class="tab-pane fade active in" id="home">
								<h4>Home Tab</h4>
								<p> hahahaha </p>								
							</div>
							<div class="tab-pane fade" id="profile">
								<h4>Profile Tab</h4>
								<p> hahahahaasdasd </p>
							</div>	
						</div>
						
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<jsp:include page="./fragments/footer.jsp" />
			
</body>
</html>