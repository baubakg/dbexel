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

<meta name="pageName" content="<%=DBEXEL_PAGES.CreateWorkSheetStep1%>">

<title>Enter Data...</title>
<s:head />
</head>
<body>
<s:form action="InitWorkSheetCreationAction" id="wsCreateSentance">

	<s:textarea cols="50" rows="3" name="sentence" value="Hello cruel World"/>
	<s:submit />
</s:form>

</body>
</html>