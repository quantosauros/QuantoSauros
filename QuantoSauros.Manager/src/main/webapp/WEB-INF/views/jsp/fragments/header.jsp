<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="author" content="JIHOON LEE">
    
    <title>AAD Manager</title>
    
	<spring:url value="/resources/bower_components/bootstrap/dist/css/bootstrap.min.css" var="bootstrapCss" />
	<spring:url value="/resources/bower_components/metisMenu/dist/metisMenu.min.css" var="metisMenuCss" />
	<spring:url value="/resources/dist/css/timeline.css" var="timelineCss" />
	<spring:url value="/resources/dist/css/sb-admin-2.css" var="customCss" />
	<spring:url value="/resources/bower_components/font-awesome/css/font-awesome.min.css" var="customFont" />

    <!-- Bootstrap Core CSS -->
    <link href="${bootstrapCss}" rel="stylesheet">
    <!-- MetisMenu CSS -->
    <link href="${metisMenuCss}" rel="stylesheet">
    <!-- Timeline CSS -->
    <link href="${timelineCss}" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${customCss}" rel="stylesheet">
    <!-- Custom Fonts -->
    <link href="${customFont}" rel="stylesheet" type="text/css">
    
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script>
	  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
	
	  ga('create', 'UA-71975637-1', 'auto');
	  ga('send', 'pageview');
	
	</script>
</head>
