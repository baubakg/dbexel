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

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ConfirmHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import dbexel.model.dao.JPATestTools;
import dbexel.model.dao.WorkSheetDao;
import dbexel.model.dao.WorkSheetDaoImpl;
import dbexel.model.mapping.AttributeValue;
import dbexel.model.mapping.WorkSheet;
import dbexel.model.mapping.Attribute;
import dbexel.model.mapping.Entry;
import dbexel.presentation.actions.AttributeTypes;
import dbexel.system.DBEXEL_PAGES;
import dbexel.system.tools.DBExelHtmlUnitSupport;
import dbexel.system.tools.DBExelTestTools;
import dbexel.system.tools.FileUtils;

public class WorkSheetEntryAAT {
	private static WorkSheetDao wsDao = new WorkSheetDaoImpl();
	private WebClient webClient;

	@BeforeClass
	public static void cleanSystem() {
		JPATestTools.cleanAllData();
	}

	@Before
	public void initSystem() {

		webClient = new WebClient();
		webClient = new WebClient(BrowserVersion.FIREFOX_3_6);
		webClient.setJavaScriptEnabled(true);
		webClient.getCookieManager().setCookiesEnabled(true);
		webClient.setCssEnabled(false);
	}

	@After
	public void cleanWindows() {
		JPATestTools.cleanAllData();
		webClient.closeAllWindows();
	}

	// In this test we add a new entry to the worksheet
	@Test
	public void testAddAnEntry() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		WorkSheet l_workSheet = DBExelTestTools
				.fetchTestWorkSheet("testAddAnEntry");

		wsDao.createWorkSheet(l_workSheet);

		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite") + "/DBEXEL/"));

		// Enter New Worksheet
		l_currentPage = l_currentPage.getAnchorByText(l_workSheet.getWs_Name())
				.click();

		// Validate Table
		// We should have 3 lines in the table
		HtmlTable l_currentTable = DBExelHtmlUnitSupport
				.getTableByOrder(l_currentPage);
		assertEquals(3, l_currentTable.getRowCount());

		WebAssert.assertLinkPresent(l_currentPage, "AddEntry");

		// Go to the Add entry page
		l_currentPage = ((HtmlAnchor) l_currentPage.getElementById("AddEntry"))
				.click();

		// Validate Table
		l_currentTable = DBExelHtmlUnitSupport
				.getTableByOrder(l_currentPage, 1);

		// We should have 5 lines in the table
		// 1 header line
		// 1 type line
		// 2 already existing entries
		// 1 input list
		assertEquals(5, l_currentTable.getRowCount());

		// We should have 5 columns
		assertEquals(5, l_currentTable.getRow(0).getCells().size());

		HtmlForm l_currentForm = l_currentPage
				.getFormByName("AddNewEntryToWorkSheet");

		List<HtmlInput> l_inputFields = DBExelHtmlUnitSupport
				.getInputsByNameStEnd(l_currentForm, "newAttributeValues[", "]");

		assertEquals(5, l_inputFields.size());

		// assertEquals("newAttributeValues["+l_workSheet.getAttributes().get(0).getAttrId()+"]",
		// l_inputFields.get(0).getId());
		assertEquals("newAttributeValues["
				+ l_workSheet.getAttributes().get(0).getAttrId() + "]",
				l_inputFields.get(0).getNameAttribute());

		for (int i = 0; i < 5; i++) {
			l_inputFields.get(i).setValueAttribute(Integer.toString(10 + i));
		}

		// Submit for to go back to the previous page
		l_currentPage = DBExelHtmlUnitSupport.getSubmitButton(l_currentForm)
				.click();

		assertEquals(DBEXEL_PAGES.WorkSheetDetails,
				DBExelHtmlUnitSupport.getMetaPageName(l_currentPage));

		// Check that the given values are present
		// Validate Table
		l_currentTable = DBExelHtmlUnitSupport.getTableByOrder(l_currentPage);

		// We should now have 5 lines in the table
		assertEquals(4, l_currentTable.getRowCount());
	}

	// In this test we add a new entry to the worksheet
	@Test
	public void testAddAnEntry_validationFail()
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {

		WorkSheet l_workSheet = DBExelTestTools
				.fetchTestWorkSheet("testAddAnEntry");

		wsDao.createWorkSheet(l_workSheet);

		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite")
				+ "/DBEXEL/AddNewEntryToWorkSheet.action?workSheet_Id="
				+ l_workSheet.getWs_Id()));

		// Validate Table
		// We should have 3 lines in the table
		HtmlTable l_currentTable = (HtmlTable) l_currentPage
				.getElementById("contentTable");

		int l_headerRowIdx = 0;
		int l_typeRowIdx = 1;
		int l_inputRowIdx = 2;

		int l_refRowCount = l_currentTable.getRowCount();
		// 1 Header Line
		// 1 type line
		// 1 input line for the new entry
		// N lines for the entries in the pre-existing worksheet
		int l_expectedRowNum = 1 + 1 + 1 + l_workSheet.getEntries().size();
		assertEquals("Unexpected number of rows for add entry",
				l_expectedRowNum, l_refRowCount);

		// We should have 5 columns
		assertEquals(5, l_currentTable.getRow(l_headerRowIdx).getCells().size());

		HtmlForm l_currentForm = l_currentPage
				.getFormByName("AddNewEntryToWorkSheet");

		List<HtmlInput> l_inputFields = DBExelHtmlUnitSupport
				.getInputsByNameStEnd(l_currentForm, "newAttributeValues[", "]");

		assertEquals(5, l_inputFields.size());

		// assertEquals("newAttributeValues["+l_workSheet.getAttributes().get(0).getAttrId()+"]",
		// l_inputFields.get(0).getId());
		assertEquals("newAttributeValues["
				+ l_workSheet.getAttributes().get(0).getAttrId() + "]",
				l_inputFields.get(0).getNameAttribute());

		for (int i = 0; i < 5; i++) {
			l_inputFields.get(i).setValueAttribute(Integer.toString(10 + i));
		}

		HtmlButton l_button = l_currentPage.getDocumentElement()
				.getOneHtmlElementByAttribute("button", "id", "addAttrValue");

		l_button.click();

		HtmlInput l_attributeInput = (HtmlInput) DBExelHtmlUnitSupport
				.getLastCell(l_currentTable, l_headerRowIdx).getFirstChild();

		// Checking and setting the attribute
		String l_newAttributeName = "Age";
		l_attributeInput.setValueAttribute(l_newAttributeName);

		HtmlSelect l_attributeTypeInput = (HtmlSelect) DBExelHtmlUnitSupport
				.getLastCell(l_currentTable, l_typeRowIdx).getFirstChild();

		l_attributeTypeInput.setSelectedAttribute(
				AttributeTypes.Number.toString(), true);

		// Checking and setting the attribute
		HtmlInput l_attributeValueInput = (HtmlInput) DBExelHtmlUnitSupport
				.getLastCell(l_currentTable, l_inputRowIdx).getFirstChild();

		String l_newAttributeValue = "Melville";
		l_attributeValueInput.setValueAttribute(l_newAttributeValue);

		// Submit for to go back to the previous page
		l_currentPage = DBExelHtmlUnitSupport.getSubmitButton(l_currentForm)
				.click();

		assertEquals(DBEXEL_PAGES.AddNewEntry,
				DBExelHtmlUnitSupport.getMetaPageName(l_currentPage));

		// Check that the given values are present
		// Validate Table
		l_currentTable = DBExelHtmlUnitSupport
				.getTableByOrder(l_currentPage, 1);

		// We should now have as many lines as before lines in the table
		assertEquals(l_refRowCount, l_currentTable.getRowCount());

	}

	// In this test we edit an entry in the worksheet
	@Test
	public void testEditAnEntry() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		WorkSheet l_workSheet = DBExelTestTools
				.fetchTestWorkSheet("testEditAnEntry");

		wsDao.createWorkSheet(l_workSheet);

		// Get first page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite") + "/DBEXEL/"));

		// Enter New Worksheet
		l_currentPage = l_currentPage.getAnchorByText(l_workSheet.getWs_Name())
				.click();

		HtmlTable l_currentTable = DBExelHtmlUnitSupport
				.getTableByOrder(l_currentPage);

		WebAssert.assertLinkPresentWithText(l_currentPage, "Edit");

		int l_lastEntryIdx = l_workSheet.getEntries().size() - 1;
		int l_attrIdx = 0;
		int i = 0;

		for (Attribute lt_attr : l_workSheet.getAttributes()) {
			if (lt_attr.getType().equals(AttributeTypes.Number)) {
				l_attrIdx = i;
			}
			i++;
		}

		String l_oldValue = l_workSheet.getEntries().get(l_lastEntryIdx)
				.getAttributeValues().get(l_attrIdx).getValue();

		// Test Before Edit
		assertEquals(l_oldValue, l_currentTable.getRow(l_lastEntryIdx + 1)
				.getCell(l_attrIdx).getTextContent());

		HtmlAnchor l_currentAnchor = DBExelHtmlUnitSupport.getAnchorById(
				l_currentPage, "editLink_" + (l_lastEntryIdx + 1));

		// Go to the Edit entry page
		l_currentPage = l_currentAnchor.click();
		
		assertEquals(DBExelHtmlUnitSupport.getMetaPageName(l_currentPage),
				DBEXEL_PAGES.EditWorkSheetEntry);

		int l_headerRowIdx = 0;
		int l_typeRowIdx = 1;
		int l_inputRowIdx = l_lastEntryIdx + l_typeRowIdx;

		assertEquals(DBEXEL_PAGES.EditWorkSheetEntry.toString(),
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"));

		HtmlForm l_currentForm = l_currentPage
				.getFormByName("EditWorkSheetEntry");

		l_currentTable = DBExelHtmlUnitSupport.getTableById(l_currentPage,
				"contentTable");

		assertEquals(l_workSheet.getAttributes().size(),
				l_currentTable.getRow(l_headerRowIdx).getCells().size());

		assertEquals(
				l_workSheet.getAttributes().size(),
				DBExelHtmlUnitSupport.getInputsByNameStEnd(l_currentForm,
						"newAttributeValues[", "]").size());

		HtmlInput l_input = DBExelHtmlUnitSupport.getInputsByNameStEnd(
				l_currentForm, "newAttributeValues[", "]").get(l_attrIdx);

		assertEquals(l_oldValue, l_input.getValueAttribute());

		String l_newValue = "3333";
		l_input.setValueAttribute(l_newValue);

		// Press Submit
		l_currentPage = DBExelHtmlUnitSupport.getSubmitButton(l_currentForm)
				.click();

		assertEquals(DBExelHtmlUnitSupport.getMetaPageName(l_currentPage),
				DBEXEL_PAGES.WorkSheetDetails);

		l_currentTable = DBExelHtmlUnitSupport.getTableByOrder(l_currentPage);

		assertEquals(l_newValue, l_currentTable.getRow(l_lastEntryIdx + 1)
				.getCell(l_attrIdx).getTextContent());
	}

	// In this test we edit an entry in the worksheet
	@Test
	public void testEditAnEntry_validationFail()
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {

		WorkSheet l_workSheet = DBExelTestTools
				.fetchTestWorkSheet("testEditAnEntry");

		wsDao.createWorkSheet(l_workSheet);

		// Get first page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite") + "/DBEXEL/"));

		// Enter New Worksheet
		l_currentPage = l_currentPage.getAnchorByText(l_workSheet.getWs_Name())
				.click();

		HtmlTable l_currentTable = DBExelHtmlUnitSupport
				.getTableByOrder(l_currentPage);

		int l_lastEntryIdx = l_workSheet.getEntries().size() - 1;
		int l_attrIdx = 0;
		int i = 0;

		for (Attribute lt_attr : l_workSheet.getAttributes()) {
			if (lt_attr.getType().equals(AttributeTypes.Number)) {
				l_attrIdx = i;
			}
			i++;
		}

		String l_oldValue = l_workSheet.getEntries().get(l_lastEntryIdx)
				.getAttributeValues().get(l_attrIdx).getValue();

		HtmlAnchor l_currentAnchor = DBExelHtmlUnitSupport.getAnchorById(
				l_currentPage, "editLink_" + (l_lastEntryIdx + 1));

		// Go to the Edit entry page
		l_currentPage = l_currentAnchor.click();
		
		assertEquals(DBExelHtmlUnitSupport.getMetaPageName(l_currentPage),
				DBEXEL_PAGES.EditWorkSheetEntry);

		int l_headerRowIdx = 0;
		int l_typeRowIdx = 1;
		int l_inputRowIdx = l_lastEntryIdx + l_typeRowIdx;

		assertEquals(DBEXEL_PAGES.EditWorkSheetEntry.toString(),
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"));

		HtmlForm l_currentForm = l_currentPage
				.getFormByName("EditWorkSheetEntry");

		l_currentTable = DBExelHtmlUnitSupport.getTableById(l_currentPage,
				"contentTable");


		HtmlInput l_input = DBExelHtmlUnitSupport.getInputsByNameStEnd(
				l_currentForm, "newAttributeValues[", "]").get(l_attrIdx);

		assertEquals(l_oldValue, l_input.getValueAttribute());

		String l_newValue = "text value";
		l_input.setValueAttribute(l_newValue);

		// Press Submit
		l_currentPage = DBExelHtmlUnitSupport.getSubmitButton(l_currentForm)
				.click();

		assertEquals(DBExelHtmlUnitSupport.getMetaPageName(l_currentPage),
				DBEXEL_PAGES.EditWorkSheetEntry);
	}

	// In this test we delete an entry in the worksheet
	@Test
	public void testDeleteAnEntry() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		WorkSheet l_workSheet = DBExelTestTools
				.fetchTestWorkSheet("testEditAnEntry");

		wsDao.createWorkSheet(l_workSheet);

		// Get first page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite") + "/DBEXEL/"));

		// Enter New Worksheet
		l_currentPage = l_currentPage.getAnchorByText(l_workSheet.getWs_Name())
				.click();

		HtmlTable l_currentTable = DBExelHtmlUnitSupport
				.getTableByOrder(l_currentPage);

		WebAssert.assertLinkPresentWithText(l_currentPage, "Delete");
		assertEquals(3, l_currentTable.getRowCount());
		String l_oldValue = "Attr3 valX";

		// Test Before Edit
		assertEquals(l_oldValue, l_currentTable.getRow(2).getCell(2)
				.getTextContent());

		// Start deleting page
		webClient.setConfirmHandler(new ConfirmHandler() {
			public boolean handleConfirm(Page page, String message) {
				return false;
			}
		});

		boolean l_isNotConfirmed = webClient.getConfirmHandler().handleConfirm(
				l_currentPage, "Delete this work sheet?");

		assertFalse("Confirm window has not been invoked", l_isNotConfirmed);

		// Press Delete
		l_currentPage = DBExelHtmlUnitSupport.getAnchorById(l_currentPage,
				"deleteLink_2").click();

		// Now we should be back in the worksheet details page
		assertEquals(DBEXEL_PAGES.WorkSheetDetails.toString(),
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"));

		l_currentTable = DBExelHtmlUnitSupport.getTableByOrder(l_currentPage);

		assertEquals(3, l_currentTable.getRowCount());

		// Start deleting page
		webClient.setConfirmHandler(new ConfirmHandler() {
			public boolean handleConfirm(Page page, String message) {
				return true;
			}
		});

		l_isNotConfirmed = webClient.getConfirmHandler().handleConfirm(
				l_currentPage, "Delete this Entry?");

		assertTrue("Confirm window has not been invoked", l_isNotConfirmed);

		// Press Delete
		l_currentPage = DBExelHtmlUnitSupport.getAnchorById(l_currentPage,
				"deleteLink_2").click();

		// Now we should be back in the worksheet details page
		assertEquals(DBEXEL_PAGES.WorkSheetDetails.toString(),
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"));

		l_currentTable = DBExelHtmlUnitSupport.getTableByOrder(l_currentPage);

		assertEquals(2, l_currentTable.getRowCount());

		// Let's see if the attributes are still there
		l_currentTable = DBExelHtmlUnitSupport.getTableByOrder(l_currentPage);

		assertEquals("Column 3", l_currentTable.getRow(0).getCell(0)
				.getTextContent());
		assertEquals("Column 7", l_currentTable.getRow(0).getCell(2)
				.getTextContent());
		assertEquals("Column 13", l_currentTable.getRow(0).getCell(4)
				.getTextContent());
	}

}
