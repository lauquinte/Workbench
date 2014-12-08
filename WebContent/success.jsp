<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags" %>

<html>
<head>
	<style type="text/css">
	#log {
    font-family: "Arial, Verdana";
    width: 100%;
    border-collapse: collapse;
	border: 1px solid #002664;
	}
	
	#log td, #log th {
	    font-size: 1em;
	    border: 1px solid #002664;
	    padding: 3px 7px 2px 7px;
	}	
	#log th {
	    font-size: 1.1em;
	    text-align: left;
	    padding-top: 5px;
	    padding-bottom: 4px;
 	    background-color: #002664;
	    color: #ffffff;
	}
	#log tr.alt td {
	    color: #000000;
	    background-color: #EAF2D3;
	}
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
    <!-- [if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif] -->
	
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
									 style="width:75px;height:75px">
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
				<div class="container-fluid">
					<div class="row-fluid">
						<div class="span9">
							<div align="center">
								<br /><a href="/Workbench">Go Back to Workbench</a><br /><br />
							   	<s:if test="hasActionMessages()">
							   		<s:actionmessage theme="bootstrap" /><br />
							   		<s:if test="%{url != null}">
 							   			<s:a href="%{url}">Navigate to Support Folder</s:a>
							   		</s:if>
								</s:if>
							</div>
							
							<s:if test="%{logs.size() != 0}">
								<h4 class="row-fluid">Data Load Log</h4>
								<table id="log" class="table table-striped table-bordered">
									<tr>
										<th>Id</th>
										<th>Message Text</th>
									</tr>
									<s:iterator value="logs" var="log">
										<tr>
											<td><s:property value="jobExecId"/></td>
											<td><s:property value="messageText"/></td>
										</tr>
									</s:iterator>
								</table>
							</s:if>
						</div>
					</div>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>