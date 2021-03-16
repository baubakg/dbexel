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
<%@taglib prefix="sj" uri="/struts-jquery-tags"%>

<html>
<head>
<%@ page import="dbexel.system.DBEXEL_PAGES"%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="pageName" content="<%=DBEXEL_PAGES.AddNewEntry%>">
<meta name="main_actor.name"
	content="<s:property value="workSheet.ws_Name" />">
<meta name="main_actor.id"
	content="<s:property value="workSheet.ws_Id" />">

<title>Add New Entry to WorkSheet</title>
<script src="js/jquery-1.7.1.min.js"></script>
<s:head />

<link rel="stylesheet" href="css/DBExelStyle.css" type="text/css"/>

<sj:head jqueryui="true" />
</head>
<body>
	<s:fielderror />

	<s:form action="AddNewEntryToWorkSheet" theme="simple">
		<s:hidden name="workSheet_Id" />
		<table border="1">

			<tr>
				<td>
					<table id="contentTable">
						<tr id="attrRow">
							<s:iterator value="workSheet.attributes" var="currentAttribute" status="attributeIndex">
								
								<s:if test="%{errorNumberColumns[#attributeIndex.index]==true}">
									<th class="validationFailure">								
								</s:if>
								<s:else>
									<th>
								</s:else>
								<%--
									<s:property value="attrName" />
									--%>
									<s:a action="ShowDistinctAttribute"
										id="attributeDetails_%{#attributeIndex.index}"
										title="%{description}">
										<s:param name="attributeId" value="attrId" />
										<s:param name="workSheet_Id" value="workSheet.ws_Id" />
										<s:property value='attrName' />
								</s:a>
								</th>
							</s:iterator>
						</tr>
						<tr id="attrTypeRow" class="attributeType">
							<s:iterator value="workSheet.attributes" var="currentAttribute" status="attributeIndex">
								<s:if test="%{errorNumberColumns[#attributeIndex.index]==true}">
									<td class="validationFailure">
								</s:if>
								<s:else>
									<td>
								</s:else>
									<s:property value="type" />
								</td>
							</s:iterator>
						</tr>
						<tr id="attrValRow">
							<s:iterator value="workSheet.attributes" status="attributeIndex">

								<td>
									<s:if test="%{errorNumberColumns[#attributeIndex.index]==true}">
										<sj:autocompleter list="fetchStringValues()"
											name="%{'newAttributeValues['+attrId+']'}"
											forceValidOption="false" cssClass="validationFailure"/>
									</s:if>
									<s:else>
										<sj:autocompleter list="fetchStringValues()"
											name="%{'newAttributeValues['+attrId+']'}"
											forceValidOption="false"/>
									</s:else>
								</td>
							</s:iterator>
						</tr>
						<s:iterator value="workSheet.entries" var="currentEntry">
							<tr>
								<s:iterator value="fetchAttributeValues()">
									<td><s:property value="value" /></td>
								</s:iterator>
							</tr>
						</s:iterator>
					</table>
				</td>
				<td><button type="button" id="addAttrValue">+</button></td>
			</tr>
		</table>
		<s:submit />
	</s:form>


	<script>
		$(document)
				.ready(
						function() {
							var attrValueId = 0;

							$("#addAttrValue")
									.click(
											function() {

												$("#attrRow")
														.append(
																"<th><input name='newAddedAttributes["+attrValueId +"]'/></th>");											
		
												$("#attrTypeRow").append("<%
														String l_select = "<select name='newAddedColumnTypes[\"+attrValueId+\"]'>";
														for (String lt_aType : AttributeTypes.stringValues()) {
															l_select += "<option value='"+lt_aType+"'>"+lt_aType+"</option>";
														}
												
												l_select += "</select>";
												out.print("<td>");
											    out.print(l_select);
											    out.print("</td>");
												%>");

												$("#attrValRow")
														.append(
																"<td><input name='newAddedAttributeValues["+attrValueId +"]'/></td>");

												attrValueId = attrValueId + 1;
											});
						});
	</script>
</body>
</html>