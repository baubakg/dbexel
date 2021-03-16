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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%@ page import="dbexel.system.DBEXEL_PAGES" %>

<meta name="pageName" content="<%=DBEXEL_PAGES.WelcomeToDBExel%>">

<title>Welcome to DBExel</title>
</head>
<body>
<h1>Welcome to DBExel the on-line spreadsheet manager</h1>
<s:action name="ShowWorkSheets" executeResult="true"/>

<p>

<s:a id="createWorkSheet" action="CreateFreeWorkSheet">add a new worksheet</s:a>
</body>
</html>