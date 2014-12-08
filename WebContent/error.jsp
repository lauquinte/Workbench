<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags" %>

<html>
<head>
	<style>
		#header-dpc {
		    padding: 5px;
		    width: 100%;
		    border-collapse: collapse;
		}
		
		img {
	    	width:50%;
		}
	</style>
	<!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <sb:head includeScripts="true" includeScriptsValidation="false" includeStylesResponsive="true" />
	<title>Human Services Data Hub Workbench</title>
</head>
<body>
<table align="center">
	<tr>
		<td>
			<div class="header-dpc" align="center">
				<table cellpadding="5px" align="center">
					<tr>
						<td width="10%">
							<img alt="NSW Department of Premier and Cabinet Logo"
								 src="img/nsw-dpc-logo.png" 
								 style="width: 75px; height: 75px">
						</td>
						<td width="90%" bgcolor="#002664" align="center">
							<h1><font color="white" style="font-weight: normal;">Human Services Data Hub Workbench</font></h1>
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
	
	<tr>
		<td>
			<div class="container-fluid" align="center">
<!-- 				<div class="row-fluid"> -->
					<div class="span9">
						<div align="left">
							<s:if test="hasActionErrors()">
					   			<br /><a href="/Workbench">Go Back to Workbench</a><br /><br /> 
						    	<s:actionerror theme="bootstrap" /><br />
						      	<s:fielderror theme="bootstrap" /><br />
							</s:if>
					    </div>
					</div>
<!-- 				</div> -->
			</div>
		</td>
	</tr>
</table>
</body>
</html>