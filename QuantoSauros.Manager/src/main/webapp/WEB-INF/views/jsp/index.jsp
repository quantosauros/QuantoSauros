<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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

</head>

<body>	
	<div id="wrapper">
		<jsp:include page="./navigator.jsp" />
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
	
	 <!-- jQuery -->
    <script src="resources/bower_components/jquery/dist/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="resources/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>

    <!-- Metis Menu Plugin JavaScript -->
    <script src="resources/bower_components/metisMenu/dist/metisMenu.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="resources/dist/js/sb-admin-2.js"></script>
			
</body>
</html>