<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<!DOCTYPE html>
<html>
<jsp:include page="./fragments/header.jsp" />

<body>
	<div id="wrapper">
		<!-- Navigator -->
		<jsp:include page="./fragments/navigator.jsp" />	       
		<div id="page-wrapper">
			<div id="container-fluid">
				<div class="row">
					<div class="col-lg-12">
							<h1 class="page-header"> Pricer </h1>
					</div>
				</div>	
				<div class="row">
					<div class="col-lg-12">
						<div class="panel panel-primary form-group">
							<div class="panel-heading">
								<h3 class="panel-title"> Pricer Control Panel </h3>
							</div>
							<div class="panel-body">
								<div class="list-group">
									<div class = "list-group-item">
										<label for="processIdInput"> Process ID </label>
										 
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
	
	<jsp:include page="./fragments/footer.jsp" />
	
</body>
</html>