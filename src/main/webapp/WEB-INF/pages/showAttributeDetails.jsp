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

<meta name="pageName" content="<%=DBEXEL_PAGES.AttributeDetails%>">
<meta name="main_actor.name"
	content="<s:property value="attribute.attrName" />">
<meta name="main_actor.id"
	content="<s:property value="attribute.attrId" />">

<title>Attribute Details</title>

<s:head />
<link rel="stylesheet" href="css/DBExelStyle.css" type="text/css" />

</head>
<body>
	
	<h1>
		Attribute
		<s:property value="attribute.attrName" />
	</h1>	
	
	<s:if test="hasActionMessages()">
		   <div class="actionHints">
		      <s:actionmessage cssClass="actionHints"/>
		   </div>
	</s:if>
						
	<s:form id="Attribute_Form" action="ShowDistinctAttribute">
		<s:hidden key="attributeId" />
		<s:hidden key="workSheet_Id" />

		<s:textfield key="attributeName"/>
		
		<s:select list="attributeTypeOptions"
							key="attributeType"
							value="attribute.type"/>
		
		<s:textarea key="attributeDescription" cols="40" rows="5"/>

		<s:submit />
	</s:form>
	<table id="page">
		<tr>
			<td>
				<table id="WorkSheet_Table">
					<tr>
						<th>Work Sheets</th>
					</tr>
					<tr>
						<td><s:a action="ShowDistinctWorkSheet">
								<s:param name="workSheet_Id" value="attribute.workSheet.ws_Id" />
								<%-- To be changed when we have more than one worksheet for an attribute --%>
								<s:property value="attribute.workSheet.ws_Name" />
							</s:a></td>
					</tr>
				</table>
			</td>
			<td>
				<table id="AttributeValue_Table">
					<tr>
						<th>Distinct Values</th>
					</tr>
					<s:iterator value="attribute.fetchStringValues()" var="value">
						<tr>
							<td><s:property value="value" /></td>
						</tr>
					</s:iterator>
				</table>
			</td>
		</tr>
	</table>
	
	<table border="1" id="Entry_Table">
			<tr>
				<s:iterator value="attribute.workSheet.attributes" var="currentAttribute" status="attributeIndex">
					<th><s:a action="ShowDistinctAttribute" id="attributeDetails_%{#attributeIndex.index}" title="%{description}">
							<s:param name="attributeId" value="attrId" />
							<s:param name="workSheet_Id" value="workSheet.ws_Id" />
							<s:property value='attrName' /></s:a></th>
				</s:iterator>
			</tr>
			<s:iterator value="entriesPresent" var="currentEntry"
				status="serverPointer">
	
				<tr>
					<s:iterator value="fetchAttributeValues()">
						<td><s:property value="value" /></td>
					</s:iterator>					
				</tr>
			</s:iterator>
		</table>
</body>
</html>