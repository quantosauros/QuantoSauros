<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<spring:url value="/resources/bower_components/jquery/dist/jquery.min.js" var="jQueryJs" />
<spring:url value="/resources/bower_components/bootstrap/dist/js/bootstrap.min.js" var="bootStrapJs" />
<spring:url value="/resources/bower_components/metisMenu/dist/metisMenu.min.js" var="metisMenuJs" />
<spring:url value="/resources/dist/js/sb-admin-2.js" var="customJs" />

<!-- jQuery -->
<script src="${jQueryJs}"></script>
<!-- Bootstrap Core JavaScript -->
<script src="${bootStrapJs}"></script>
<!-- Metis Menu Plugin JavaScript -->
<script src="${metisMenuJs}"></script>
<!-- Custom Theme JavaScript -->
<script src="${customJs}"></script>