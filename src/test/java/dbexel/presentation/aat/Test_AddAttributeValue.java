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
package dbexel.presentation.aat;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import dbexel.model.dao.JPATestTools;
import dbexel.model.dao.WorkSheetDao;
import dbexel.model.dao.WorkSheetDaoImpl;
import dbexel.model.mapping.WorkSheet;
import dbexel.model.mapping.Attribute;
import dbexel.presentation.actions.AttributeTypes;
import dbexel.system.DBEXEL_PAGES;
import dbexel.system.tools.DBExelHtmlUnitSupport;
import dbexel.system.tools.DBExelTestTools;
import dbexel.system.tools.FileUtils;

/**
 * This is a set of tests related to the issue 68: Add an attribute value. These
 * tests point to the
 * 
 * @author gandomi
 * 
 */
public class Test_AddAttributeValue {
	private WebClient webClient;
	private WorkSheet f_ws = DBExelTestTools
			.fetchTestWorkSheet("AddAttrValue WorkSheet");

	@BeforeClass
	public static void cleanUp() {
		JPATestTools.cleanAllData();
	}

	// Prepare the environment before we start testing
	// - Initiate resources
	// - Prepare necessary steps so that you are in the same state as the
	@Before
	public void initSystem() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		webClient = new WebClient(BrowserVersion.FIREFOX_3_6);
		webClient.setJavaScriptEnabled(true);
		webClient.getCookieManager().setCookiesEnabled(true);
		webClient.setCssEnabled(false);

		WorkSheetDao l_wsDao = new WorkSheetDaoImpl();
		l_wsDao.createWorkSheet(f_ws);

	}

	@After
	public void cleanWindows() {
		JPATestTools.cleanAllData();
		webClient.closeAllWindows();

	}

	@Test
	public void testAddAttributeValue_AddEntry_AC1()
			throws FailingHttpStatusCodeException, IOException {

		// 1. Test Ramp-Up
		// Performed in the @before method
		// Prepare environment to reach point of start
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite")
				+ "/DBEXEL/AddNewEntryToWorkSheet.action?workSheet_Id="
				+ f_ws.getWs_Id()));

		// 2. Validate Starting Point
		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"), is(equalTo(DBEXEL_PAGES.AddNewEntry.toString())));

		HtmlTable l_currentTable = DBExelHtmlUnitSupport.getTableByOrder(
				l_currentPage, 1);

		assertEquals("contentTable", l_currentTable.getId());

		// 3. Take References
		int l_initialRowCount = l_currentTable.getRowCount();

		int l_headerRowIdx = 0;
		int l_typeRowIdx = 1;
		int l_inputRowIdx = 2;

		int l_lastAttributeIdx = DBExelHtmlUnitSupport.getLastColumnIdx(
				l_currentTable, l_headerRowIdx);

		int l_lastAttributeTypeIdx = DBExelHtmlUnitSupport.getLastColumnIdx(
				l_currentTable, l_typeRowIdx);

		int l_lastAttributeValueIdx = DBExelHtmlUnitSupport.getLastColumnIdx(
				l_currentTable, l_inputRowIdx);

		assertTrue("We should now have a type row.",
				l_currentTable.hasHtmlElementWithId("attrTypeRow"));

		// 4. Initiate Tested Action
		HtmlButton l_button = l_currentPage.getDocumentElement()
				.getOneHtmlElementByAttribute("button", "id", "addAttrValue");

		// Now let us see if we click on the button do we get the new input
		// fields?
		l_button.click();

		// 5. Make Assertions
		assertEquals("The number of rows should not change!",
				l_initialRowCount, l_currentTable.getRowCount());

		l_lastAttributeIdx++;
		assertEquals("We should have one more attribute input field",
				l_lastAttributeIdx, DBExelHtmlUnitSupport.getLastColumnIdx(
						l_currentTable, l_headerRowIdx));

		l_lastAttributeTypeIdx++;
		assertEquals("We should have one more attribute type field",
				l_lastAttributeTypeIdx, DBExelHtmlUnitSupport.getLastColumnIdx(
						l_currentTable, l_typeRowIdx));

		l_lastAttributeValueIdx++;
		assertEquals("We should have one more attribute value input text",
				l_lastAttributeValueIdx,
				DBExelHtmlUnitSupport.getLastColumnIdx(l_currentTable,
						l_inputRowIdx));

		assertTrue(l_currentTable.getRow(l_headerRowIdx)
				.getCell(l_lastAttributeIdx).getFirstChild() instanceof HtmlInput);

		HtmlInput l_attributeInput = (HtmlInput) DBExelHtmlUnitSupport
				.getLastCell(l_currentTable, l_headerRowIdx).getFirstChild();

		assertEquals("newAddedAttributes[0]",
				l_attributeInput.getNameAttribute());

		assertThat(
				l_currentTable.getRow(l_typeRowIdx)
						.getCell(l_lastAttributeTypeIdx).getFirstChild(),
				is(instanceOf(HtmlSelect.class)));

		assertTrue(l_currentTable.getRow(l_inputRowIdx)
				.getCell(l_lastAttributeValueIdx).getFirstChild() instanceof HtmlInput);

		l_attributeInput = (HtmlInput) DBExelHtmlUnitSupport.getLastCell(
				l_currentTable, l_inputRowIdx).getFirstChild();

		assertEquals("newAddedAttributeValues[0]",
				l_attributeInput.getNameAttribute());

		// 6. Test Iteration
		// 4. Initiate Tested Action
		// Now let us see if we click on the button once again
		l_button.click();

		// 5. Make Assertions
		l_lastAttributeIdx++;
		assertEquals(l_lastAttributeIdx,
				DBExelHtmlUnitSupport.getLastColumnIdx(l_currentTable,
						l_headerRowIdx));

		l_lastAttributeTypeIdx++;
		assertEquals("We should have one more attribute type field",
				l_lastAttributeTypeIdx, DBExelHtmlUnitSupport.getLastColumnIdx(
						l_currentTable, l_typeRowIdx));

		l_lastAttributeValueIdx++;
		assertEquals(l_lastAttributeValueIdx,
				DBExelHtmlUnitSupport.getLastColumnIdx(l_currentTable,
						l_inputRowIdx));

		// Checking and setting the attribute
		l_attributeInput = (HtmlInput) DBExelHtmlUnitSupport.getLastCell(
				l_currentTable, l_headerRowIdx).getFirstChild();

		assertEquals("newAddedAttributes[1]",
				l_attributeInput.getNameAttribute());

		// Checking and setting the attribute
		assertTrue(l_currentTable.getRow(l_inputRowIdx)
				.getCell(l_lastAttributeValueIdx - 1).getFirstChild() instanceof HtmlInput);

		l_attributeInput = (HtmlInput) DBExelHtmlUnitSupport.getLastCell(
				l_currentTable, l_inputRowIdx).getFirstChild();

		assertEquals("newAddedAttributeValues[1]",
				l_attributeInput.getNameAttribute());

		// 7. Test Ramp-Down
		// In the After section
	}

	/*
	 * @Test public void testAddAttributeValue_AddEntry_AC1() throws
	 * FailingHttpStatusCodeException, IOException {
	 * 
	 * // 1. Test Ramp-Up // Performed in the @before method // Prepare
	 * environment to reach point of start HtmlPage l_currentPage = (HtmlPage)
	 * webClient.getPage(new URL(FileUtils .fetchProperty("dbexel.testsite") +
	 * "/DBEXEL/AddNewEntryToWorkSheet.action?workSheet_Id=" +
	 * f_ws.getWs_Id()));
	 * 
	 * // 2. Validate Starting Point
	 * assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
	 * "pageName"), is(equalTo(DBEXEL_PAGES.AddNewEntry.toString())));
	 * 
	 * HtmlTable l_currentTable = DBExelHtmlUnitSupport.getTableByOrder(
	 * l_currentPage, 1);
	 * 
	 * assertEquals("contentTable", l_currentTable.getId());
	 * 
	 * // 3. Take References int l_initialRowCount =
	 * l_currentTable.getRowCount();
	 * 
	 * int l_headerRowIdx = 0; int l_inputRowIdx = 1;
	 * 
	 * int l_lastAttributeIdx = DBExelHtmlUnitSupport.getLastColumnIdx(
	 * l_currentTable, l_headerRowIdx); int l_lastAttributeValueIdx =
	 * DBExelHtmlUnitSupport.getLastColumnIdx( l_currentTable, l_inputRowIdx);
	 * 
	 * // 4. Initiate Tested Action HtmlButton l_button =
	 * l_currentPage.getDocumentElement()
	 * .getOneHtmlElementByAttribute("button", "id", "addAttrValue");
	 * 
	 * // Now let us see if we click on the button do we get the new input //
	 * fields? l_button.click();
	 * 
	 * // 5. Make Assertions
	 * assertEquals("The number of rows should not change!", l_initialRowCount,
	 * l_currentTable.getRowCount());
	 * 
	 * l_lastAttributeIdx++;
	 * assertEquals("We should have one more attribute input text",
	 * l_lastAttributeIdx, DBExelHtmlUnitSupport.getLastColumnIdx(
	 * l_currentTable, l_headerRowIdx)); l_lastAttributeValueIdx++;
	 * 
	 * // This had to be changed because we are adding a new row describing //
	 * type assertEquals("We should have one more attribute value input text",
	 * l_lastAttributeValueIdx,
	 * DBExelHtmlUnitSupport.getLastColumnIdx(l_currentTable, l_inputRowIdx));
	 * 
	 * assertTrue(l_currentTable.getRow(l_headerRowIdx)
	 * .getCell(l_lastAttributeIdx).getFirstChild() instanceof HtmlInput);
	 * 
	 * HtmlInput l_attributeInput = (HtmlInput) DBExelHtmlUnitSupport
	 * .getLastCell(l_currentTable, l_headerRowIdx).getFirstChild();
	 * 
	 * assertEquals("newAddedAttributes[0]",
	 * l_attributeInput.getNameAttribute());
	 * 
	 * assertTrue(l_currentTable.getRow(l_inputRowIdx)
	 * .getCell(l_lastAttributeValueIdx).getFirstChild() instanceof HtmlInput);
	 * 
	 * l_attributeInput = (HtmlInput) DBExelHtmlUnitSupport.getLastCell(
	 * l_currentTable, l_inputRowIdx).getFirstChild();
	 * 
	 * assertEquals("newAddedAttributeValues[0]",
	 * l_attributeInput.getNameAttribute());
	 * 
	 * // 6. Test Iteration // 4. Initiate Tested Action // Now let us see if we
	 * click on the button once again l_button.click();
	 * 
	 * // 5. Make Assertions l_lastAttributeIdx++;
	 * assertEquals(l_lastAttributeIdx,
	 * DBExelHtmlUnitSupport.getLastColumnIdx(l_currentTable, l_headerRowIdx));
	 * 
	 * l_lastAttributeValueIdx++; assertEquals(l_lastAttributeValueIdx,
	 * DBExelHtmlUnitSupport.getLastColumnIdx(l_currentTable, l_inputRowIdx));
	 * 
	 * // Checking and setting the attribute l_attributeInput = (HtmlInput)
	 * DBExelHtmlUnitSupport.getLastCell( l_currentTable,
	 * l_headerRowIdx).getFirstChild();
	 * 
	 * assertEquals("newAddedAttributes[1]",
	 * l_attributeInput.getNameAttribute());
	 * 
	 * // Checking and setting the attribute
	 * assertTrue(l_currentTable.getRow(l_inputRowIdx)
	 * .getCell(l_lastAttributeValueIdx - 1).getFirstChild() instanceof
	 * HtmlInput);
	 * 
	 * l_attributeInput = (HtmlInput) DBExelHtmlUnitSupport.getLastCell(
	 * l_currentTable, l_inputRowIdx).getFirstChild();
	 * 
	 * assertEquals("newAddedAttributeValues[1]",
	 * l_attributeInput.getNameAttribute());
	 * 
	 * // 7. Test Ramp-Down // In the After section }
	 */

	@Test
	public void testAddAttributeValue_EditEntry_AC1()
			throws FailingHttpStatusCodeException, IOException {

		// 1. Test Ramp-Up
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite")
				+ "/DBEXEL/EditWorkSheetEntry.action?workSheet_Id="
				+ f_ws.getWs_Id()
				+ "&selectedEntryId="
				+ f_ws.getEntries().get(f_ws.getEntries().size() - 1)
						.getEntryId()));

		// 2. Validate Starting Point
		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"),
				is(equalTo(DBEXEL_PAGES.EditWorkSheetEntry.toString())));
		WebAssert.assertTitleEquals(l_currentPage, "Update Worksheet Entry");

		HtmlTable l_currentTable = DBExelHtmlUnitSupport.getTableByOrder(
				l_currentPage, 1);

		assertEquals("contentTable", l_currentTable.getId());

		// 3. Take References
		int l_attrRowIdx = 0;
		int l_typeRowIdx = 1;

		int l_initialRowCount = l_currentTable.getRowCount();
		int l_lastAttributeIdx = DBExelHtmlUnitSupport.getLastColumnIdx(
				l_currentTable, l_attrRowIdx);

		int l_lastAttributeTypeIdx = DBExelHtmlUnitSupport.getLastColumnIdx(
				l_currentTable, l_typeRowIdx);

		// get the row that is being edited
		HtmlTableRow l_editedRow = l_currentTable
				.getRowById("entryRow_editable");

		int l_lastAttributeValueIdx = l_editedRow.getCells().size() - 1;

		// 4. Initiate Tested Action
		HtmlButton l_button = l_currentPage.getDocumentElement()
				.getOneHtmlElementByAttribute("button", "id", "addAttrValue");

		// Now let us see if we click on the button do we get the new input
		// fields?
		l_button.click();

		// 5. Make Assertions
		assertEquals("The number of rows should not change!",
				l_initialRowCount, l_currentTable.getRowCount());

		l_lastAttributeIdx++;
		assertEquals("We should have one more attribute input text",
				l_lastAttributeIdx, DBExelHtmlUnitSupport.getLastColumnIdx(
						l_currentTable, l_attrRowIdx));

		l_lastAttributeTypeIdx++;
		assertEquals("We should have one more attribute type field",
				l_lastAttributeTypeIdx, DBExelHtmlUnitSupport.getLastColumnIdx(
						l_currentTable, l_typeRowIdx));

		l_lastAttributeValueIdx++;
		assertEquals("We should have one more attribute value input text",
				l_lastAttributeValueIdx, l_editedRow.getCells().size() - 1);

		assertTrue(l_currentTable.getRow(l_attrRowIdx)
				.getCell(l_lastAttributeIdx).getFirstChild() instanceof HtmlInput);

		assertTrue(l_currentTable.getRow(l_typeRowIdx)
				.getCell(l_lastAttributeIdx).getFirstChild() instanceof HtmlSelect);

		HtmlInput l_attributeInput = (HtmlInput) DBExelHtmlUnitSupport
				.getLastCell(l_currentTable, l_attrRowIdx).getFirstChild();

		assertEquals("newAddedAttributes[0]",
				l_attributeInput.getNameAttribute());

		assertThat(
				l_currentTable.getRow(l_typeRowIdx)
						.getCell(l_lastAttributeTypeIdx).getFirstChild(),
				is(instanceOf(HtmlSelect.class)));

		assertTrue(l_editedRow.getCell(l_lastAttributeValueIdx).getFirstChild() instanceof HtmlInput);

		l_attributeInput = (HtmlInput) DBExelHtmlUnitSupport.getLastCell(
				l_currentTable, "entryRow_editable").getFirstChild();

		assertEquals("newAddedAttributeValues[0]",
				l_attributeInput.getNameAttribute());

		// 4. Initiate Tested Action
		// Now let us see if we click on the button once again
		l_button.click();

		// 5. Make Assertions
		l_lastAttributeIdx++;
		assertEquals(l_lastAttributeIdx,
				DBExelHtmlUnitSupport.getLastColumnIdx(l_currentTable,
						l_attrRowIdx));

		l_lastAttributeTypeIdx++;
		assertEquals("We should have one more attribute type field",
				l_lastAttributeTypeIdx, DBExelHtmlUnitSupport.getLastColumnIdx(
						l_currentTable, l_typeRowIdx));

		l_lastAttributeValueIdx++;
		assertEquals(l_lastAttributeValueIdx,
				DBExelHtmlUnitSupport.getLastColumnIdx(l_currentTable,
						"entryRow_editable"));

		// Checking and setting the attribute
		l_attributeInput = (HtmlInput) DBExelHtmlUnitSupport.getLastCell(
				l_currentTable, l_attrRowIdx).getFirstChild();

		assertEquals("newAddedAttributes[1]",
				l_attributeInput.getNameAttribute());

		// Checking and setting the attribute
		assertTrue(DBExelHtmlUnitSupport.getLastCell(l_currentTable,
				"entryRow_editable").getFirstChild() instanceof HtmlInput);

		l_attributeInput = (HtmlInput) DBExelHtmlUnitSupport.getLastCell(
				l_currentTable, "entryRow_editable").getFirstChild();

		assertEquals("newAddedAttributeValues[1]",
				l_attributeInput.getNameAttribute());

		// 7. Test Ramp-Down
	}

	@Test
	public void testAddAttributeValue_AddEntry_AC2()
			throws FailingHttpStatusCodeException, IOException {

		// 1. Test Ramp-Up
		// Performed in the @before method
		// Prepare environment to reach point of start

		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite")
				+ "/DBEXEL/ShowDistinctWorkSheet.action?workSheet_Id="
				+ f_ws.getWs_Id()));

		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"),
				is(equalTo(DBEXEL_PAGES.WorkSheetDetails.toString())));

		HtmlTable l_currentTable = DBExelHtmlUnitSupport
				.getTableByOrder(l_currentPage);

		// 3. Take References
		int l_referenceRowCount = l_currentTable.getRowCount();
		int l_refNrOfAttrCols = l_currentTable.getRow(0).getCells().size();

		l_currentPage = l_currentPage.getElementById("AddEntry").click();

		// 1. Test Ramp-Up
		// Performed in the @before method
		// Prepare environment to reach point of start
		// HtmlPage l_currentPage = (HtmlPage) webClient
		// .getPage(getClass().getResource(
		// "/testExamples/scenarios/AddNewEntryToWorkSheet_A.htm"));

		// 2. Validate Starting Point
		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"), is(equalTo(DBEXEL_PAGES.AddNewEntry.toString())));

		l_currentTable = DBExelHtmlUnitSupport
				.getTableByOrder(l_currentPage, 1);

		// 3. Take References
		// int l_initialRowCount = l_currentTable.getRowCount();

		int l_headerRowIdx = 0;
		int l_typeRowIdx = 1;
		int l_inputRowIdx = 2;

		int l_previousNumberOfAttributes = l_currentTable
				.getRow(l_headerRowIdx).getCells().size();

		// 4. Prepare & Initiate Tested Action
		HtmlButton l_button = l_currentPage.getDocumentElement()
				.getOneHtmlElementByAttribute("button", "id", "addAttrValue");

		HtmlForm l_currentForm = l_currentPage
				.getFormByName("AddNewEntryToWorkSheet");

		List<HtmlInput> l_inputFields = DBExelHtmlUnitSupport
				.fetchVisibleInputFields(l_currentForm);

		assertEquals(l_previousNumberOfAttributes, l_inputFields.size());

		// Fill new row
		for (int i = 0; i < l_previousNumberOfAttributes; i++) {
			l_inputFields.get(i).type(Integer.toString(10 + i));
		}

		// Now let us see if we click on the button do we get the new input
		// fields?
		l_button.click();

		// Now let us see if we click on the button once again
		l_button.click();

		// Checking and setting the attribute
		HtmlInput l_attributeInput = (HtmlInput) DBExelHtmlUnitSupport
				.getLastCell(l_currentTable, l_headerRowIdx).getFirstChild();

		String l_newAttributeName = "Age";
		l_attributeInput.setValueAttribute(l_newAttributeName);

		HtmlSelect l_attributeTypeSelect = (HtmlSelect) DBExelHtmlUnitSupport
				.getLastCell(l_currentTable, l_typeRowIdx).getFirstChild();

		l_attributeTypeSelect.setSelectedAttribute(
				AttributeTypes.Number.toString(), true);

		// Checking and setting the attribute
		HtmlInput l_attributeValueInput = (HtmlInput) DBExelHtmlUnitSupport
				.getLastCell(l_currentTable, l_inputRowIdx).getFirstChild();

		String l_newAttributeValue = "19";
		l_attributeValueInput.setValueAttribute(l_newAttributeValue);

		// Submit form
		l_currentPage = DBExelHtmlUnitSupport.getSubmitButton(l_currentForm)
				.click();

		// 5. Make Assertions
		// Inspect result of submission
		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"),
				is(equalTo(DBEXEL_PAGES.WorkSheetDetails.toString())));

		l_currentTable = DBExelHtmlUnitSupport.getTableByOrder(l_currentPage);

		// We should now have 3 lines, and 4 attributes (+1 for an action
		// column)
		assertEquals(l_referenceRowCount + 1, l_currentTable.getRowCount());

		// Remove the action column
		int l_numberOfAttributeColumns = l_currentTable.getRow(0).getCells()
				.size();

		// Given the action column we should have the number of previous columns
		// + 1
		assertEquals(l_refNrOfAttrCols + 1, l_numberOfAttributeColumns);

		assertEquals(l_newAttributeName,
				l_currentTable.getRow(0)
						.getCell(l_numberOfAttributeColumns - 2)
						.getTextContent());

		// 7. Test Ramp-Down
	}

	@Test
	public void testAddAttributeValue_EditEntry_AC2()
			throws FailingHttpStatusCodeException, IOException {

		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite")
				+ "/DBEXEL/ShowDistinctWorkSheet.action?workSheet_Id="
				+ f_ws.getWs_Id()));

		// 2. Validate Starting Point
		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"),
				is(equalTo(DBEXEL_PAGES.WorkSheetDetails.toString())));

		HtmlTable l_currentTable = DBExelHtmlUnitSupport
				.getTableByOrder(l_currentPage);

		// 3. Take References
		int l_attrRowIdx = 0;

		int l_referenceRowCount = l_currentTable.getRowCount();
		int l_refNrOfAttrCols = l_currentTable.getRow(l_attrRowIdx).getCells()
				.size();

		l_currentPage = l_currentPage.getElementById(
				"editLink_" + (l_referenceRowCount - 1)).click();

		assertThat(DBExelHtmlUnitSupport.getMetaPageName(l_currentPage),
				is(equalTo(DBEXEL_PAGES.EditWorkSheetEntry)));

		int l_typeRowIdx = 1;
		int l_inputAttrValueRowIdx = l_referenceRowCount - 1 + l_typeRowIdx;

		l_currentTable = DBExelHtmlUnitSupport.getTableById(l_currentPage,
				"contentTable");

		// 4. Prepare & Initiate Tested Action
		HtmlButton l_button = l_currentPage.getDocumentElement()
				.getOneHtmlElementByAttribute("button", "id", "addAttrValue");

		// Now let us see if we click on the button do we get the new input
		// fields?
		l_button.click();

		HtmlInput l_attributeInput = (HtmlInput) DBExelHtmlUnitSupport
				.getLastCell(l_currentTable, l_attrRowIdx).getFirstChild();

		l_attributeInput = (HtmlInput) DBExelHtmlUnitSupport.getLastCell(
				l_currentTable, l_inputAttrValueRowIdx).getFirstChild();

		// Now let us see if we click on the button once again
		l_button.click();

		// Checking and setting the attribute
		l_attributeInput = (HtmlInput) DBExelHtmlUnitSupport.getLastCell(
				l_currentTable, l_attrRowIdx).getFirstChild();

		String l_newAttributeName = "e";
		l_attributeInput.type(l_newAttributeName);

		HtmlSelect l_attributeTypeSelect = (HtmlSelect) DBExelHtmlUnitSupport
				.getLastCell(l_currentTable, l_typeRowIdx).getFirstChild();

		l_attributeTypeSelect.setSelectedAttribute(
				AttributeTypes.Number.toString(), true);

		// Checking and setting the attribute
		l_attributeInput = (HtmlInput) DBExelHtmlUnitSupport.getLastCell(
				l_currentTable, l_inputAttrValueRowIdx).getFirstChild();

		String l_newAttributeValue = "7";
		l_attributeInput.setValueAttribute(l_newAttributeValue);

		// Submit form
		HtmlForm l_currentForm = l_currentPage
				.getFormByName("EditWorkSheetEntry");
		l_currentPage = DBExelHtmlUnitSupport.getSubmitButton(l_currentForm)
				.click();

		// 5. Make Assertions
		// Inspect result of submission
		assertThat(DBExelHtmlUnitSupport.getMetaPageName(l_currentPage),
				is(equalTo(DBEXEL_PAGES.WorkSheetDetails)));

		l_currentTable = DBExelHtmlUnitSupport.getTableByOrder(l_currentPage);

		// We should now have 3 lines, and 4 attributes (+1 for an action
		// column)
		assertEquals(l_referenceRowCount, l_currentTable.getRowCount());

		// Remove the action column
		int l_numberOfAttributeColumns = l_currentTable.getRow(l_attrRowIdx)
				.getCells().size();

		assertEquals("We should have had added a new column",
				l_refNrOfAttrCols + 1, l_numberOfAttributeColumns);

		assertEquals(l_newAttributeName, l_currentTable.getRow(l_attrRowIdx)
				.getCell(l_refNrOfAttrCols - 1).getTextContent());

		// 7. Test Ramp-Down
	}

	// 91: Newly added attribute in new attribute values cannot be set for
	// preexisting entries
	@Test
	public void testAddAttributeValueToPreExistingEntryWhenNewEntryAndAttributeValueIsSet()
			throws FailingHttpStatusCodeException, IOException {

		// 1. Test Ramp-Up
		// Performed in the @before method
		// Prepare environment to reach point of start

		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite")
				+ "/DBEXEL/ShowDistinctWorkSheet.action?workSheet_Id="
				+ f_ws.getWs_Id()));

		assertThat(DBExelHtmlUnitSupport.getMetaPageName(l_currentPage),
				is(equalTo(DBEXEL_PAGES.WorkSheetDetails)));

		int l_headerRowIdx = 0;
		int l_inputRowIdx = 1;

		HtmlTable l_currentTable = DBExelHtmlUnitSupport
				.getTableByOrder(l_currentPage);

		int l_initialRowCount = l_currentTable.getRowCount();
		int l_previousNumberOfAttributes = l_currentTable
				.getRow(l_headerRowIdx).getCells().size() - 1;

		l_currentPage = l_currentPage.getElementById("AddEntry").click();

		// 2. Validate Starting Point
		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"), is(equalTo(DBEXEL_PAGES.AddNewEntry.toString())));

		l_currentTable = DBExelHtmlUnitSupport.getTableById(l_currentPage,
				"contentTable");

		// 3. Take References
		l_headerRowIdx = 0;
		// int l_typeRowIdx = 1;
		l_inputRowIdx = 2;

		// 4. Prepare & Initiate Tested Action
		HtmlButton l_button = l_currentPage.getDocumentElement()
				.getOneHtmlElementByAttribute("button", "id", "addAttrValue");

		HtmlForm l_currentForm = l_currentPage
				.getFormByName("AddNewEntryToWorkSheet");

		List<HtmlInput> l_inputFields = DBExelHtmlUnitSupport
				.fetchVisibleInputFields(l_currentForm);

		// Fill new row
		for (int i = 0; i < l_previousNumberOfAttributes; i++) {
			l_inputFields.get(i).type(Integer.toString(10 + i));
		}

		// Now let us see if we click on the button do we get the new input
		// fields?
		l_button.click();

		HtmlInput l_attributeInput = (HtmlInput) DBExelHtmlUnitSupport
				.getLastCell(l_currentTable, l_headerRowIdx).getFirstChild();

		// Checking and setting the attribute
		String l_newAttributeName = "Author";
		l_attributeInput.setValueAttribute(l_newAttributeName);

		// Checking and setting the attribute
		HtmlInput l_attributeValueInput = (HtmlInput) DBExelHtmlUnitSupport
				.getLastCell(l_currentTable, l_inputRowIdx).getFirstChild();

		String l_newAttributeValue = "Melville";
		l_attributeValueInput.setValueAttribute(l_newAttributeValue);

		// Submit form
		l_currentPage = DBExelHtmlUnitSupport.getSubmitButton(l_currentForm)
				.click();

		// 5. Make Assertions
		// Inspect result of submission
		assertThat(DBExelHtmlUnitSupport.getMetaPageName(l_currentPage),
				is(equalTo(DBEXEL_PAGES.WorkSheetDetails)));

		l_currentTable = (HtmlTable) l_currentPage
				.getElementById("Entry_Table");

		// We should now have 1 more line with more attributes (+1 for an action
		// column)
		assertEquals(l_initialRowCount + 1, l_currentTable.getRowCount());
		assertThat(l_currentTable.getRow(l_headerRowIdx).getCells().size() - 1,
				is(equalTo(l_previousNumberOfAttributes + 1)));

		// Make sure that the pre existing entries do not have a value for this
		// attribute
		assertThat(
				l_currentTable.getRow(1).getCell(l_previousNumberOfAttributes)
						.getTextContent(), is(equalTo("")));

		// 6. Make reiterations
		// Now we will add a new attribute value for the newly added attribute
		// to the pre existing entry
		l_currentPage = DBExelHtmlUnitSupport.getAnchorById(l_currentPage,
				"editLink_" + l_initialRowCount).click();

		// 5. Make Assertions
		assertEquals(DBEXEL_PAGES.EditWorkSheetEntry,
				DBExelHtmlUnitSupport.getMetaPageName(l_currentPage));

		l_currentForm = l_currentPage.getFormByName("EditWorkSheetEntry");

		l_currentTable = DBExelHtmlUnitSupport.getTableById(l_currentPage,
				"contentTable");

		assertThat(l_currentTable.getRow(l_headerRowIdx).getCells().size(),
				is(equalTo(l_previousNumberOfAttributes + 1)));

		assertThat(
				l_currentTable.getRow(l_headerRowIdx).getCell(l_previousNumberOfAttributes)
						.getTextContent().trim(), is(equalTo("Author")));

		assertThat(
				DBExelHtmlUnitSupport.getInputsByNameStEnd(l_currentForm,
						"newAttributeValues[", "]").size(), is(equalTo(6)));

		HtmlInput l_newAV = DBExelHtmlUnitSupport.getInputsByNameStEnd(
				l_currentForm, "newAttributeValues[", "]").get(
				l_currentTable.getRow(l_headerRowIdx).getCells().size() - 1);

		// Set New Value
		l_newAV.setValueAttribute("Hesse");

		// 4. Finish last step ofTested Action
		l_currentPage = DBExelHtmlUnitSupport.getSubmitButton(l_currentForm)
				.click();

		// 5. Make Assertions
		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"),
				is(equalTo(DBEXEL_PAGES.WorkSheetDetails.toString())));

		l_currentTable = (HtmlTable) l_currentPage
				.getElementById("Entry_Table");

		// Hesse should be added to the pre existing entry
		assertThat(
				l_currentTable.getRow(l_initialRowCount).getCell(l_previousNumberOfAttributes)
						.getTextContent(), is(equalTo("Hesse")));

		// 7. Test Ramp-Down
	}
	
	//137: 	Maintaining Unique attributes when adding an attribute value
	//@Test
	public void testAddAttributeValue_AddEntry_AddAttrValueWithHomonymousName()
			throws FailingHttpStatusCodeException, IOException {

		// 1. Test Ramp-Up
		// Performed in the @before method
		// Prepare environment to reach point of start

		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite")
				+ "/DBEXEL/ShowDistinctWorkSheet.action?workSheet_Id="
				+ f_ws.getWs_Id()));

		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"),
				is(equalTo(DBEXEL_PAGES.WorkSheetDetails.toString())));

		HtmlTable l_currentTable = DBExelHtmlUnitSupport
				.getTableByOrder(l_currentPage);

		// 3. Take References
		int l_referenceRowCount = l_currentTable.getRowCount();
		int l_refNrOfAttrCols = l_currentTable.getRow(0).getCells().size();

		l_currentPage = l_currentPage.getElementById("AddEntry").click();

		// 1. Test Ramp-Up
		// Performed in the @before method
		// Prepare environment to reach point of start
		// HtmlPage l_currentPage = (HtmlPage) webClient
		// .getPage(getClass().getResource(
		// "/testExamples/scenarios/AddNewEntryToWorkSheet_A.htm"));

		// 2. Validate Starting Point
		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"), is(equalTo(DBEXEL_PAGES.AddNewEntry.toString())));

		l_currentTable = DBExelHtmlUnitSupport
				.getTableByOrder(l_currentPage, 1);

		// 3. Take References
		// int l_initialRowCount = l_currentTable.getRowCount();

		int l_headerRowIdx = 0;
		int l_typeRowIdx = 1;
		int l_inputRowIdx = 2;

		int l_previousNumberOfAttributes = l_currentTable
				.getRow(l_headerRowIdx).getCells().size();

		// 4. Prepare & Initiate Tested Action
		HtmlButton l_button = l_currentPage.getDocumentElement()
				.getOneHtmlElementByAttribute("button", "id", "addAttrValue");

		HtmlForm l_currentForm = l_currentPage
				.getFormByName("AddNewEntryToWorkSheet");

		List<HtmlInput> l_inputFields = DBExelHtmlUnitSupport
				.fetchVisibleInputFields(l_currentForm);

		assertEquals(l_previousNumberOfAttributes, l_inputFields.size());

		// Fill new row
		for (int i = 0; i < l_previousNumberOfAttributes; i++) {
			l_inputFields.get(i).type(Integer.toString(10 + i));
		}

		// Now let us see if we click on the button do we get the new input
		// fields?
		l_button.click();

		//Let us retrieve the attribute name of the first column
		assertEquals("Ensure that we have the correct attribute to start with","Column 3",  l_currentTable.getRow(l_headerRowIdx).getCell(0).getTextContent().trim());
		String l_repeatedAttrName = l_currentTable.getRow(l_headerRowIdx).getCell(0).getTextContent().trim();
		
		// Checking and setting the attribute
		HtmlInput l_attributeInput = (HtmlInput) DBExelHtmlUnitSupport
				.getLastCell(l_currentTable, l_headerRowIdx).getFirstChild();

		l_attributeInput.setValueAttribute(l_repeatedAttrName);

		HtmlSelect l_attributeTypeSelect = (HtmlSelect) DBExelHtmlUnitSupport
				.getLastCell(l_currentTable, l_typeRowIdx).getFirstChild();

		l_attributeTypeSelect.setSelectedAttribute(
				AttributeTypes.Text.toString(), true);

		// Checking and setting the attribute
		HtmlInput l_attributeValueInput = (HtmlInput) DBExelHtmlUnitSupport
				.getLastCell(l_currentTable, l_inputRowIdx).getFirstChild();

		String l_newAttributeValue = "19";
		l_attributeValueInput.setValueAttribute(l_newAttributeValue);

		// Submit form
		l_currentPage = DBExelHtmlUnitSupport.getSubmitButton(l_currentForm)
				.click();

		// 5. Make Assertions
		// Inspect result of submission
		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"),
				is(equalTo(DBEXEL_PAGES.WorkSheetDetails.toString())));

		l_currentTable = DBExelHtmlUnitSupport.getTableByOrder(l_currentPage);

		// We should now have 3 lines, and 4 attributes (+1 for an action
		// column)
		assertEquals(l_referenceRowCount + 1, l_currentTable.getRowCount());

		// Remove the action column
		int l_numberOfAttributeColumns = l_currentTable.getRow(0).getCells()
				.size();

		// Given the action column we should have the number of previous columns
		// + 1
		assertEquals(l_refNrOfAttrCols + 1, l_numberOfAttributeColumns);

		assertEquals(l_repeatedAttrName,
				l_currentTable.getRow(0)
						.getCell(l_numberOfAttributeColumns - 2)
						.getTextContent());

		// 7. Test Ramp-Down
	}
}
