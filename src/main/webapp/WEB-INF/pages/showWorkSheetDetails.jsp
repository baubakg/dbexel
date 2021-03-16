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
<meta name="pageName" content="<%=DBEXEL_PAGES.WorkSheetDetails%>">
<meta name="main_actor.name"
	content="<s:property value="workSheet.ws_Name" />">
<meta name="main_actor.id"
	content="<s:property value="workSheet.ws_Id" />">

<title>Work Sheet <s:property value="workSheet.ws_Name" />
	Details
</title>
</head>
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
		Work Sheet
		<s:property value="workSheet.ws_Name" />
	</h1>
	<s:if test="workSheet.entries.size() > 0">
		<table border="1" id="Entry_Table">
			<tr>
				<s:iterator value="workSheet.attributes" var="currentAttribute"
					status="attributeIndex">
					<th><s:a action="ShowDistinctAttribute"
							id="attributeDetails_%{#attributeIndex.index}" title="%{description}">
							<s:param name="attributeId" value="attrId" />
							<s:param name="workSheet_Id" value="workSheet.ws_Id" />
							<s:property value='attrName' />
						</s:a></th>
				</s:iterator>
				<th>Actions</th>
			</tr>
			<s:iterator value="workSheet.entries" var="currentEntry"
				status="serverPointer">

				<tr>
					<s:iterator value="workSheet.attributes" var="currentAttribute">
						<td><s:property
								value="%{#currentEntry.findValueByAttribute(#currentAttribute).value}" /></td>
					</s:iterator>
					<td><s:a action="EditWorkSheetEntry"
							id="editLink_%{#serverPointer.count}">
							Edit
							<s:param name="selectedEntryId" value="entryId" />
							<s:param name="workSheet_Id" value="workSheet.ws_Id" />
						</s:a> <s:a action="DeleteWorkSheetEntry"
							id="deleteLink_%{#serverPointer.count}"
							onclick="return confirmDelete('Entry')">
							Delete
							<s:param name="selectedEntryId" value="entryId" />
							<s:param name="workSheet_Id" value="workSheet.ws_Id" />
						</s:a></td>
				</tr>
			</s:iterator>
		</table>
	</s:if>
	<s:else>
		<p align="center" style="font-style: italic;" id="warningNoEntries">
		The current worksheet is empty. Start filling it <s:a id="AddEntry_Warn" action="AddNewEntryToWorkSheet">here<s:param name="workSheet_Id" value="workSheet.ws_Id" />
			</s:a>.
		</p>
	</s:else>

	</BR>

	<s:a id="editWS" action="EditWorkSheet">Edit this Work Sheet
		<s:param name="workSheet_Id" value="workSheet.ws_Id" />
	</s:a>
	<s:a id="delWS" action="DeleteWorkSheet"
		onclick="return confirmDelete('Work Sheet')">Delete this Work Sheet
		<s:param name="workSheet_Id" value="workSheet.ws_Id" />
	</s:a>
	<s:a id="AddEntry" action="AddNewEntryToWorkSheet">Add an Entry
		<s:param name="workSheet_Id" value="workSheet.ws_Id" />
	</s:a>
	<s:a action="Start">See the Work Sheets</s:a>
</body>
</html>