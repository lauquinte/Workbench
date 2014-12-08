<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/struts-jquery-tags" prefix="sj"%>
<%@ taglib prefix="sb" uri="/struts-bootstrap-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<sj:head compressed='false' />
<script type="text/javascript">
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// JQuery Script to evaluate RadioButton selected value (Masterdata/Agency)
	// 
	// - When radiobutton's value 'Masterdata' is selected the corresponding dropdown for Masterdata 
	// 	 must be shown and Agency dropdown must be hidden (via its DIV container)
	// 
	// - When radiobutton's value 'Agency' is selected the corresponding dropdown for Agency 
	// 	 must be shown and Masterdata dropdown must be hidden (via its DIV container)
	/////////////////////////////////////////////////////////////////////////////////////////////////
	$(document).ready(function() {
		$("input[name$='dataRadioButton']").click(function() {
			var test = $(this).val();
			$("div.desc").hide();
			$("#" + test).show();
		});
	});
</script>
<style type="text/css">
	#header-dpc {
		padding: 5px;
		width: 100%;
		border-collapse: collapse;
	}
	
	img {
		width: 50%;
	}
</style>
<!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
<!-- [if lt IE 9]>
     <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
     <![endif] -->

<sb:head includeScripts="true" 
		 includeScriptsValidation="false"
		 includeStylesResponsive="true" />
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
				<div class="row-fluid" align="justify">
					<div class="span9">
						<div align="left">
							<s:if test="hasFieldErrors()">
								<br /><a href="/Workbench">Reload Workbench</a><br /><br />
							</s:if>
							<s:if test="hasActionErrors()">
								<br /><a href="/Workbench">Reload Workbench</a><br /><br />
								<s:actionerror theme="bootstrap" /><br />
							</s:if>
						</div>
		
						<s:form action="upload" method="post" enctype="multipart/form-data"
							theme="bootstrap" cssClass="form-horizontal">
							<h4>Welcome: <s:property value="%{#session.loggedUser}" /><br /></h4>
							<fieldset>
								<s:if test="%{#session.isAdmin}">
									<s:radio name="dataRadioButton" label="Select Data Type"
										list="{'Masterdata', 'Agency'}"
										value="defaultRadioButton" cssErrorClass="foo" />
		
									<s:select tooltip="Choose Processing Date" list="fyqList"
										name="selectedFYQ" label="Processing Date" labelposition="left"
										emptyOption="false" headerValue="Select value" />
		
									<div id="Masterdata" class="desc">
										<s:select tooltip="Choose Data Type" list="dataList"
											id="selectedData" name="selectedData" label="Masterdata"
											labelposition="left" emptyOption="false"
											headerValue="Select value" />
									</div>
									<div id="Agency" class="desc" style="display: none;">
										<s:select tooltip="Choose Data Type" list="dataList2"
											id="selectedData2" name="selectedData2" label="Agency"
											labelposition="left" emptyOption="false"
											headerValue="Select value" />
									</div>
								</s:if>
								<s:else> <%-- If not isAdmin --%>
									<s:label name="fiscalYearQuarter" label="Processing Date" />
									<s:select tooltip="Choose Agency" list="dataList"
										name="selectedData" label="Agency" labelposition="left"
										emptyOption="false" headerValue="Select value" />
								</s:else>
		
								<s:file name="uploadDoc" label="Filename"
									 tooltip="Upload Your File" />
		
								<s:submit value="Load File" align="right"
									   cssClass="btn btn-primary" />
							</fieldset>
						</s:form>
					</div>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>