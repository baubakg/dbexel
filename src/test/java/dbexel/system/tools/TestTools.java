/**
 *  DBEXEL is a Database Backed & Web-Based Worksheet and chart management program. 
 *  It has been influenced by Excel.
 *  For questions or suggestions please contact the developper at ( Development@Gandomi.com )
 *  Copyright (C) 2011 Baubak Gandomi   
 *
 *	This file is part of the application DBEXEL
 *
 *   DBEXEL is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   DBEXEL is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package dbexel.system.tools;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import dbexel.presentation.actions.AttributeTypes;

public class TestTools {
	WebClient webClient = new WebClient();

	// Testing the fetchInputFields
	@Test
	public void testPages() throws ElementNotFoundException, IOException {
		// Get first page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(getClass()
				.getResource("/testExamples/HTMLTest.html"));

		assertEquals(
				"1.",
				2,
				DBExelHtmlUnitSupport.fetchVisibleInputFields(
						l_currentPage.getFormByName("A")).size());
	}

	// Testing input starts with
	@Test
	public void testInputStartsWith() throws FailingHttpStatusCodeException,
			IOException {
		// Get first page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(getClass()
				.getResource("/testExamples/HTMLTest.html"));

		assertEquals(
				"1.",
				3,
				DBExelHtmlUnitSupport.getInputsByNameStart(
						l_currentPage.getFormByName("C"), "b_input").size());

		assertEquals(
				"2.",
				2,
				DBExelHtmlUnitSupport.getInputsByIdStart(
						l_currentPage.getFormByName("C"), "a_input").size());

		assertEquals(
				"3.",
				1,
				DBExelHtmlUnitSupport.getInputsByIdStart(
						l_currentPage.getFormByName("C"), "a_input1").size());
	}

	// Testing input stends with
	@Test
	public void testInputStEndsWith() throws FailingHttpStatusCodeException,
			IOException {
		// Get first page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(getClass()
				.getResource("/testExamples/HTMLTest.html"));

		assertEquals(
				"1A.",
				1,
				DBExelHtmlUnitSupport.getInputsByNameStEnd(
						l_currentPage.getFormByName("E"), "b", "1").size());

		assertEquals(
				"1B.",
				1,
				DBExelHtmlUnitSupport.getInputsByNameStEnd(
						l_currentPage.getFormByName("E"), "bE_input1", "")
						.size());

		assertEquals(
				"1C.",
				1,
				DBExelHtmlUnitSupport.getInputsByNameStEnd(
						l_currentPage.getFormByName("E"), "", "bE_input1")
						.size());

		assertEquals(
				"2.",
				1,
				DBExelHtmlUnitSupport.getInputsByIdStEnd(
						l_currentPage.getFormByName("E"), "aE_", "1").size());

		assertEquals(
				"2.",
				2,
				DBExelHtmlUnitSupport.getInputsByIdStEnd(
						l_currentPage.getFormByName("E"), "aE", "").size());

		assertEquals(
				"2.",
				1,
				DBExelHtmlUnitSupport.getInputsByIdStEnd(
						l_currentPage.getFormByName("E"), "", "4").size());

		// Testing a real life problem
		assertEquals(
				"3.",
				2,
				DBExelHtmlUnitSupport.getInputsByNameStEnd(
						l_currentPage.getFormByName("E"),
						"newAttributeValues[", "]").size());
	}

	// Testing select starts with
	@Test
	public void testSelectNameStartsWith()
			throws FailingHttpStatusCodeException, IOException {
		// Get first page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(getClass()
				.getResource("/testExamples/HTMLTest.html"));

		assertEquals(
				"3.",
				2,
				DBExelHtmlUnitSupport.getSelectsByNameStart(
						l_currentPage.getFormByName("B"), "cellAction").size());
	}

	// Testing select starts with id
	@Test
	public void testSelectIdStartsWith() throws FailingHttpStatusCodeException,
			IOException {
		// Get first page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(getClass()
				.getResource("/testExamples/HTMLTest.html"));

		assertEquals(
				"3.",
				2,
				DBExelHtmlUnitSupport.getSelectsByIdStart(
						l_currentPage.getFormByName("B"),
						"StoreInitWorkSheetAction_").size());
	}

	// Testing select with a specific Id id
	@Test
	public void testSelectId() throws FailingHttpStatusCodeException,
			IOException {
		// Get first page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(getClass()
				.getResource("/testExamples/HTMLTest.html"));

		HtmlSelect l_select = DBExelHtmlUnitSupport.getSelectById(
				l_currentPage.getFormByName("B"),
				"StoreInitWorkSheetAction_cellActions_1_");

		assertEquals("cellActions[1]", l_select.getNameAttribute());
	}

	// Testing the getSubmitButton method
	@Test
	public void testGetSubmitButton1() throws FailingHttpStatusCodeException,
			IOException {
		// Get first page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(getClass()
				.getResource("/testExamples/HTMLTest.html"));

		HtmlForm l_currentForm = l_currentPage.getFormByName("A");
		HtmlSubmitInput l_testedButton = DBExelHtmlUnitSupport
				.getSubmitButton(l_currentForm);
		assertNotNull(l_testedButton);
		assertEquals("bA", l_testedButton.getId());
	}

	// Testing the getSubmitButton method on a form without a submit method
	@Test(expected = ElementNotFoundException.class)
	public void testGetSubmitButton2() throws FailingHttpStatusCodeException,
			IOException {
		// Get first page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(getClass()
				.getResource("/testExamples/HTMLTest.html"));

		HtmlForm l_currentForm = l_currentPage.getFormByName("D");
		DBExelHtmlUnitSupport.getSubmitButton(l_currentForm);

	}

	// Creating tests for table validations
	@Test
	public void testTables() throws FailingHttpStatusCodeException, IOException {
		// Get first page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(getClass()
				.getResource("/testExamples/HTMLTableTest.html"));

		HtmlTable l_table = (HtmlTable) l_currentPage.getElementsByTagName(
				"table").get(0);
		assertEquals("t1", l_table.getId());
		assertEquals(2, l_table.getRows().size());

		HtmlTable l_table2 = DBExelHtmlUnitSupport.getTableById(l_currentPage,
				l_table.getId());

		assertNotNull(l_table2);
	}

	// Testing the getSubmitButton method on a form without a submit method
	@Test(expected = ElementNotFoundException.class)
	public void testNonExistentTable_getTableById()
			throws FailingHttpStatusCodeException, IOException {
		// Get first page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(getClass()
				.getResource("/testExamples/HTMLTableTest.html"));

		DBExelHtmlUnitSupport.getTableById(l_currentPage, "tt3");

	}

	// Creating tests for table validations
	@Test
	public void testTables_getTableByOrder()
			throws FailingHttpStatusCodeException, IOException {
		// Get first page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(getClass()
				.getResource("/testExamples/HTMLTableTest.html"));

		HtmlTable l_table = (HtmlTable) l_currentPage.getElementsByTagName(
				"table").get(0);

		// Testing getTableByOrder
		HtmlTable l_table3 = DBExelHtmlUnitSupport.getTableByOrder(
				l_currentPage, 0);

		assertEquals(l_table, l_table3);

		// Testing getTableByOrder
		HtmlTable l_table4 = DBExelHtmlUnitSupport.getTableByOrder(
				l_currentPage, 1);

		assertEquals("t2", l_table4.getId());

		// Testing the getTableByOrder with only one argument
		HtmlTable l_table5 = DBExelHtmlUnitSupport
				.getTableByOrder(l_currentPage);

		assertEquals(l_table, l_table5);
	}

	// Testing the getSubmitButton method on a form without a submit method
	@Test(expected = ElementNotFoundException.class)
	public void testNonExistentTable_getTableByOrder()
			throws FailingHttpStatusCodeException, IOException {
		// Get first page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(getClass()
				.getResource("/testExamples/HTMLTableTest.html"));

		DBExelHtmlUnitSupport.getTableByOrder(l_currentPage, 22);

	}

	// Testing getAnchorById
	@Test
	public void testGetAnchorById() throws FailingHttpStatusCodeException,
			IOException {
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(getClass()
				.getResource("/testExamples/HTMLLinkTest.html"));
		HtmlAnchor l_anchor = DBExelHtmlUnitSupport.getAnchorById(
				l_currentPage, "i2");
		assertNotNull(l_anchor);
		assertEquals("/DBEXEL/EditWorkSheet.action?workSheet_Id=2",
				l_anchor.getHrefAttribute());
	}

	// Testing getAnchorById 2 testing the effect of two links with the same id
	@Test
	public void testGetAnchorById2() throws FailingHttpStatusCodeException,
			IOException {
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(getClass()
				.getResource("/testExamples/HTMLLinkTest.html"));
		HtmlAnchor l_anchor = DBExelHtmlUnitSupport.getAnchorById(
				l_currentPage, "i3");
		assertNotNull(l_anchor);
		assertEquals("/DBEXEL/EditWorkSheet.action?workSheet_Id=3",
				l_anchor.getHrefAttribute());
	}

	// Testing getAnchorById 3 testing the effect a link that also contains the
	// name attribute
	@Test
	public void testGetAnchorById3() throws FailingHttpStatusCodeException,
			IOException {
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(getClass()
				.getResource("/testExamples/HTMLLinkTest.html"));
		HtmlAnchor l_anchor = DBExelHtmlUnitSupport.getAnchorById(
				l_currentPage, "i5");
		assertNotNull(l_anchor);
		assertEquals("/DBEXEL/EditWorkSheet.action?workSheet_Id=6",
				l_anchor.getHrefAttribute());
	}

	// Testing the new get last rowindex
	@Test
	public void testGetLastRowIndex() throws FailingHttpStatusCodeException,
			IOException {

		HtmlTable l_currentTable = DBExelHtmlUnitSupport.getTableByOrder(
				(HtmlPage) webClient.getPage(getClass().getResource(
						"/testExamples/HTMLTableTest.html")), 2);

		assertEquals(2, DBExelHtmlUnitSupport.getLastRowIdx(l_currentTable));
	}

	// Testing the new get last rowindex
	@Test
	public void testGetLastCellIndex() throws FailingHttpStatusCodeException,
			IOException {

		HtmlTable l_currentTable = DBExelHtmlUnitSupport.getTableByOrder(
				(HtmlPage) webClient.getPage(getClass().getResource(
						"/testExamples/HTMLTableTest.html")), 2);

		assertEquals(2, DBExelHtmlUnitSupport.getLastColumnIdx(l_currentTable));

		assertEquals(3,
				DBExelHtmlUnitSupport.getLastColumnIdx(l_currentTable, 1));

		assertEquals(0,
				DBExelHtmlUnitSupport.getLastColumnIdx(l_currentTable, 2));
	}

	// Testing the new get last rowindex
	@Test
	public void testGetLastCellIndex_byId()
			throws FailingHttpStatusCodeException, IOException {

		HtmlTable l_currentTable = (HtmlTable) ((HtmlPage) webClient
				.getPage(getClass().getResource(
						"/testExamples/HTMLTableTest.html")))
				.getElementById("t4");

		assertEquals(3,
				DBExelHtmlUnitSupport.getLastColumnIdx(l_currentTable, "row2"));

		assertEquals(0,
				DBExelHtmlUnitSupport.getLastColumnIdx(l_currentTable, "row3"));

		assertEquals(2, DBExelHtmlUnitSupport.getLastColumnIdx(l_currentTable,
				"rowXXXX"));
	}

	@Test
	public void testGetLastCell() throws FailingHttpStatusCodeException,
			IOException {

		HtmlTable l_currentTable = DBExelHtmlUnitSupport.getTableByOrder(
				(HtmlPage) webClient.getPage(getClass().getResource(
						"/testExamples/HTMLTableTest.html")), 2);

		assertEquals("e", DBExelHtmlUnitSupport.getLastCell(l_currentTable, 0)
				.getTextContent());

		assertEquals("4", DBExelHtmlUnitSupport.getLastCell(l_currentTable, 1)
				.getTextContent());

		assertEquals("+", DBExelHtmlUnitSupport.getLastCell(l_currentTable, 2)
				.getTextContent());
	}

	@Test
	public void testGetLastCellById() throws FailingHttpStatusCodeException,
			IOException {

		HtmlTable l_currentTable = DBExelHtmlUnitSupport.getTableByOrder(
				(HtmlPage) webClient.getPage(getClass().getResource(
						"/testExamples/HTMLTableTest.html")), 3);

		assertEquals("e",
				DBExelHtmlUnitSupport.getLastCell(l_currentTable, "row1")
						.getTextContent());

		assertEquals("4",
				DBExelHtmlUnitSupport.getLastCell(l_currentTable, "row2")
						.getTextContent());

		assertEquals("+",
				DBExelHtmlUnitSupport.getLastCell(l_currentTable, "row3")
						.getTextContent());
	}

	@Test
	public void testGetLastCellById_withoutId()
			throws FailingHttpStatusCodeException, IOException {

		HtmlTable l_currentTable = (HtmlTable) ((HtmlPage) webClient
				.getPage(getClass().getResource(
						"/testExamples/HTMLTableTest.html")))
				.getElementById("t3");
		/*
		 * HtmlTable l_currentTable = DBExelTestTools.getTableByOrder(
		 * (HtmlPage) webClient.getPage(getClass().getResource(
		 * "/testExamples/HTMLTableTest.html")), 2);
		 */
		assertEquals("e",
				DBExelHtmlUnitSupport.getLastCell(l_currentTable, "row1")
						.getTextContent());
	}

	@Test
	public void theMethodXShouldReturnCellGivenItsCoordiantesWithTable()
			throws FailingHttpStatusCodeException, IOException {
		HtmlTable l_currentTable = (HtmlTable) ((HtmlPage) webClient
				.getPage(getClass().getResource(
						"/testExamples/HTMLTableTest.html")))
				.getElementById("t1");

		assertEquals("Did not find cell at coordiantes 0 0", "a",
				DBExelHtmlUnitSupport
						.getCellByCoordinates(l_currentTable, 0, 0)
						.getTextContent());
		assertEquals("Did not find cell at coordiantes 0 1", "b",
				DBExelHtmlUnitSupport
						.getCellByCoordinates(l_currentTable, 0, 1)
						.getTextContent());

		assertEquals("Did not find cell at coordiantes 1 0", "1",
				DBExelHtmlUnitSupport
						.getCellByCoordinates(l_currentTable, 1, 0)
						.getTextContent());
		assertEquals("Did not find cell at coordiantes 1 1", "2",
				DBExelHtmlUnitSupport
						.getCellByCoordinates(l_currentTable, 1, 1)
						.getTextContent());

		// Testing upper row boundary
		try {
			assertNotNull(DBExelHtmlUnitSupport.getCellByCoordinates(
					l_currentTable, 2, 1).getTextContent());
		} catch (ElementNotFoundException enfE) {
			assertTrue(true);
		}

		// Testing upper cell boundary
		try {
			assertNotNull(DBExelHtmlUnitSupport.getCellByCoordinates(
					l_currentTable, 1, 2).getTextContent());
		} catch (ElementNotFoundException enfE) {
			assertTrue(true);
		}
	}

	/* Testing Meta */
	@Test
	public void theMethodShouldReturnTheContentofTheMetaDataInAPageGivenItsName()
			throws FailingHttpStatusCodeException, IOException {

		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(getClass()
				.getResource("/testExamples/HTMLMetaTest.html"));

		assertThat("The wrong content was returned for the meta tag",
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"main_actor.name"), equalTo("First Worksheet"));
	}

	@Test
	public void theMethodShouldReturnMetaDataCalledPageName()
			throws FailingHttpStatusCodeException, IOException {

		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(getClass()
				.getResource("/testExamples/HTMLMetaTest.html"));

		assertThat(
				"The wrong content was returned for the meta tag",
				DBExelHtmlUnitSupport.getMetaPageName(l_currentPage).toString(),
				is(equalTo(DBExelHtmlUnitSupport.getMetaContentByName(
						l_currentPage, "pageName"))));
	}

	@Test
	public void testParseStringToDouble() throws ParseException {

		assertNotNull(DBExelTools.parseStringToDouble("1"));
		assertNotNull(DBExelTools.parseStringToDouble("1.0"));

		assertNull(DBExelTools.parseStringToDouble(""));

		assertNotNull(DBExelTools.parseStringToDouble("1 000"));

		assertNull(DBExelTools.parseStringToDouble("A"));

		assertNotNull(DBExelTools.parseStringToDouble("1 "));
		assertNotNull(DBExelTools.parseStringToDouble(" 1"));

		assertNotNull(DBExelTools.parseStringToDouble("1,0"));

	}
	
	@Test
	public void testIdentifyType() {
		assertThat(DBExelTools.identifyType("1"), is(equalTo(AttributeTypes.Number)));
		assertThat(DBExelTools.identifyType("1.0"), is(equalTo(AttributeTypes.Number)));

		assertThat(DBExelTools.identifyType("1A"), is(equalTo(AttributeTypes.Text)));
		
		assertThat(DBExelTools.identifyType("A"), is(equalTo(AttributeTypes.Text)));
		
		
	}
	
	
	@Test
	public void testIdentifyTypeFromList() {
		
		List<String> l_list1 = Arrays.asList("1","1.0","1 000","2","3","1 "," 1");
		assertThat(DBExelTools.identifyType(l_list1), is(equalTo(AttributeTypes.Number)));
		
		List<String> l_list2 = Arrays.asList("a","b","C","g");
		assertThat(DBExelTools.identifyType(l_list2), is(equalTo(AttributeTypes.Text)));
		
		List<String> l_list3 = Arrays.asList("a","b","C","g","2");
		assertThat(DBExelTools.identifyType(l_list3), is(equalTo(AttributeTypes.Text)));
		
		List<String> l_list4 = Arrays.asList("a","1","C","1","2");
		assertThat(DBExelTools.identifyType(l_list4), is(equalTo(AttributeTypes.Number)));
		
		//What happens if we have an equal amount of all types?
		//In this case I would like to have a Text value
		List<String> l_list5 = Arrays.asList("a","1","1","b");
		assertThat(DBExelTools.identifyType(l_list5), is(equalTo(AttributeTypes.Text)));
		
		//bug. We discovered that we initialized the search map with 0 instead of 1.
		List<String> l_list6 = Arrays.asList("5");
		assertThat(DBExelTools.identifyType(l_list6), is(equalTo(AttributeTypes.Number)));
	}

}
