<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
	<title>Human Services Data Hub Workbench</title>
</head>
<body
	<%
// Get Logged-In User from the session Cookie
   	Cookie[] cookies = request.getCookies();

	for (int e = 0; e < cookies.length; e++) {
		Cookie c = cookies[e];
		// Retrieve the value of the cookie that holds the BOE user currently logged in
		if (c.getName().equals("InfoViewPLATFORMSVC_COOKIE_USR")) {
			session.setAttribute("loggedUser", c.getValue());
		}
	}
	
// Fix Logged-In User for local testing purposes
// We use "WB_ADMIN" user for Workbench Administrator
// We use any "DataProvider_*" user for Agency users
//	session.setAttribute("loggedUser", "DataProvider_DEC");
	%>>
	
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span9">
				<s:if test="hasActionErrors()">
					<div align="left">
				   		<br /><a href="/Workbench">Go Back to Workbench</a><br /><br /> 
				    </div>
	      			<s:actionerror theme="bootstrap" />
				</s:if>
				<s:action name="login" executeResult="true" />
	   		</div>
	   	</div>
	</div>
</body>
</html>