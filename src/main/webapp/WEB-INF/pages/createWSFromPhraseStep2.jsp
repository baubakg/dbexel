<%@page import="dbexel.presentation.actions.AttributeTypes"%>
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
<%@ page import="dbexel.system.DBEXEL_PAGES"%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="pageName" content="<%=DBEXEL_PAGES.CreateWorkSheetStep2%>">

<title>Create New WorkSheet Step #2</title>

<s:head />

<link rel="stylesheet" href="css/DBExelStyle.css" type="text/css" />

</head>
<body>
	<s:fielderror />

	<BR>
	
	<s:form theme="simple" action="StoreInitWorkSheetAction">
		<s:textfield key="workSheetName" theme="xhtml" />
		<p />
		<table id="fieldTable">

			<tr>
				<th><s:property value="getText('keepColumn')" /></th>
				<s:iterator value="attributeValues" id="ID" status="cellPosition"
					var="currentCell">
					<td><s:checkbox name="keepColumns[%{#cellPosition.index}]"
							value="true" fieldValue="true" /></td>
				</s:iterator>
			</tr>
			<tr>
				<th><s:property value="getText('ColumnType')" /></th>
				<s:iterator value="columnTypes" id="ID" status="cellPosition"
					var="currentCellType">
					<td>
					
						<s:select list="attributeTypeOptions"
							name="columnTypes[%{#cellPosition.index}]"
							value="currentCellType"/>		
					</td>
				</s:iterator>
			</tr>
			<tr>
				<th><s:property value="getText('AttributeName')" /></th>
				<s:iterator value="attributeValues" id="ID" status="cellPosition"
					var="currentCell">
					<th><s:textfield name="attributeNames[%{#cellPosition.index}]"></s:textfield>
					</th>
				</s:iterator>
			</tr>
			<tr>
				<th><s:property value="getText('AttributeValue')" /></th>
				<s:iterator value="attributeValues" id="ID" status="cellPosition"
					var="currentCell">
					<td><s:if
							test="%{errorNumberColumns[#cellPosition.index]==true}">
							<s:textfield label=""
								name="attributeValues[%{#cellPosition.index}]"
								value="%{currentCell}" cssClass="validationFailure" />
						</s:if> <s:else>
							<s:textfield label=""
								name="attributeValues[%{#cellPosition.index}]"
								value="%{currentCell}" />
						</s:else></td>
				</s:iterator>
			</tr>

		</table>
		<p>
			<s:submit />
		</p>
	</s:form>

</body>
</html>