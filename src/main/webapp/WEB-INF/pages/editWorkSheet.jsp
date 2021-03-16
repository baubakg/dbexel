<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%-- 
	DBEXEL is a Database Backed & Web-Based Worksheet and chart management program. 
 	It has been influenced by Excel.
 	For questions or suggestions please contact the developper at ( Development@Gandomi.com )
 	Copyright (C) 2011 Baubak Gandomi   
 
 	This file is part of the application DBEXEL
 
 	DBEXEL is free software: you can redistribute it and/or modify
 	it under the terms of the GNU General Public License as published by
 	the Free Software Foundation, either version 3 of the License, or
 	(at your option) any later version.
 
 	DBEXEL is distributed in the hope that it will be useful,
 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 	GNU General Public License for more details.
 
 	You should have received a copy of the GNU General Public License
 	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 --%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<%@ page import="dbexel.system.DBEXEL_PAGES" %>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="pageName" content="<%=DBEXEL_PAGES.UpdateWorkSheet%>">
<meta name="main_actor.name"
	content="<s:property value="workSheet.ws_Name" />">
<meta name="main_actor.id"
	content="<s:property value="workSheet.ws_Id" />">

<title>Update Worksheet</title>
</head>
<link rel="stylesheet" href="css/DBExelStyle.css" type="text/css" />

<script>
	function confirmDelete(in_artefact) {
		var answer = window.confirm("Delete this " + in_artefact + "?");
		if (answer)
			return true;
		else
			return false;
	}
</script>
<body>
	<h1>
		<s:set var="newWSName" value="workSheet.ws_Name" />
		Updating Worksheet
		<s:property value="workSheet.ws_Name" />
	</h1>
	
	<s:if test="hasActionMessages()">
		   <div class="actionHints">
		      <s:actionmessage cssClass="actionHints"/>
		   </div>
	</s:if>
	
	<s:form>
		<s:hidden name="workSheet_Id" />

		<s:textfield label="Enter a new name for the worksheet"
			name="newWSName" />

		<s:submit value="Back" id="backToWorkSheetDetail"
			action="ShowDistinctWorkSheet" />
		<s:submit id="editWorkSheet" action="EditWorkSheet" />

		<table border="1" id="Entry_Table">
			<tr align="center">
				<s:iterator value="workSheet.attributes" var="currentAttribute"
					status="attrColIdx">
					<td>
						<s:if test="#attrColIdx.index == 0">
							<img alt="" src="icons/EmptyPlaceHolder.png" height="12"
								width="12">
						</s:if> <s:else>
							<s:a id="moveLeft_%{#attrColIdx.index}" action="EditWorkSheet"
								method="moveAttributeLeft">
								<img alt="" src="icons/1358448309_arrow_2_left_round.png">
								<s:param name="workSheet_Id" value="workSheet.ws_Id" />
								<s:param name="selectedAttributeId" value="attrId" />
								<s:param name="selectedIndex" value="%{#attrColIdx.index}" />
							</s:a>
						</s:else> 
						<s:a id="deleteAttribute_%{#attrColIdx.index}"
							action="EditWorkSheet" method="removeAttribute"
							onclick="return confirmDelete('Attribute')">
							<img alt="" src="icons/1358448281_cancel.png">
							<s:param name="workSheet_Id" value="workSheet.ws_Id" />
							<s:param name="selectedAttributeId" value="attrId" />
							<s:param name="selectedIndex" value="%{#attrColIdx.index}" />
						</s:a> 
						<s:if test="#attrColIdx.last">
							<img alt="" src="icons/EmptyPlaceHolder.png" height="12"
								width="12">
						</s:if> 
						<s:else>
							<s:a id="moveRight_%{#attrColIdx.index}" action="EditWorkSheet"
								method="moveAttributeRight">
								<img alt="" src="icons/1358448298_arrow_2_right_round.png" />
								<s:param name="workSheet_Id" value="workSheet.ws_Id" />
								<s:param name="selectedAttributeId" value="attrId" />
								<s:param name="selectedIndex" value="%{#attrColIdx.index}" />
							</s:a>							
						</s:else>
					</td>
				</s:iterator>
			</tr>
			<tr align="center">
				<s:iterator value="workSheet.attributes" var="currentAttribute">
					<th><s:a action="ShowDistinctAttribute">
							<s:param name="attribute_Id" value="attrId" />
							<s:param name="workSheet_Id" value="workSheet.ws_Id" />
							<s:property value='attrName' />
						</s:a></th>
				</s:iterator>
			</tr>
			<s:iterator value="workSheet.entries" var="currentEntry"
				status="serverPointer">

				<tr align="center">
					<s:iterator value="fetchAttributeValues()">
						<td><s:property value="value" /></td>
					</s:iterator>
				</tr>
			</s:iterator>
		</table>
	</s:form>

</body>
</html>