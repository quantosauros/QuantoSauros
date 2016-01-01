<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<spring:url value="/" var="urlHome" />
<spring:url value="/registration" var="urlRegistration" />
<spring:url value="/priceTable" var="urlPriceTable" />
<spring:url value="/deltaTable" var="urlDeltaTable" />
<spring:url value="/detailTable" var="urlDetailTable" />
<spring:url value="/priceChart" var="urlPriceChart" />
<spring:url value="/deltaChart" var="urlDeltaChart" />
<spring:url value="/deltaChart2" var="urlDeltaChart2" />
<spring:url value="/settings/process" var="urlProcessSetting" />
<spring:url value="/settings/scenario" var="urlScenarioSetting" />
<spring:url value="/settings/portfolio" var="urlPortfolioSetting" />
<spring:url value="#" var="urlDetailChart" />

<!-- Navigation -->
<nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="${urlHome}">AAD Manger</a>
    </div>
    <!-- /.navbar-header -->
    
    <div class="navbar-default sidebar" role="navigation">
        <div class="sidebar-nav navbar-collapse">
            <ul class="nav" id="side-menu">                
                <li>
                    <a href="${urlRegistration}"><i class="fa fa-dashboard fa-fw"></i> Product Registration</a>
                </li>
                <li>
                    <a href="#"><i class="fa fa-table fa-fw"></i> Result Tables <span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li>
                            <a href="${urlPriceTable}">Price Table</a>
                        </li>
                        <li>
                            <a href="${urlDeltaTable}">Delta Table</a>
                        </li>
                        <li>
                            <a href="${urlDetailTable}">Detail Table</a>
                        </li>
                    </ul>
                    <!-- /.nav-second-level -->
                </li>
                <li>
                    <a href="#"><i class="fa fa-bar-chart-o fa-fw"></i> Result Charts <span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li>
                            <a href="${urlPriceChart}">Price Charts</a>
                        </li>
                        <li>
                            <a href="${urlDeltaChart}">Delta Charts</a>
                        </li>
                        <li>
                            <a href="${urlDeltaChart2}">Delta Charts2</a>
                        </li>
                        <li>
                            <a href="${urlDetailChart}">Detail Charts</a>
                        </li>
                    </ul>
                </li>
                <li>
                    <a href="#"><i class="fa fa-edit fa-fw"></i> Calculator </a>
                </li>
                <li>
                	<a href="#"><i class="fa fa-bar-chart-o fa-fw"></i> Settings <span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li>
                            <a href="${urlProcessSetting}">Process Setting</a>
                        </li>
                        <li>
                            <a href="${urlScenarioSetting}">Scenario Setting</a>
                        </li>
                        <li>
                            <a href="${urlPortfolioSetting}">Portfolio Setting</a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
        <!-- /.sidebar-collapse -->
    </div>
    <!-- /.navbar-static-side -->
</nav>
