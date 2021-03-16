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
package dbexel.presentation.aat.bis;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
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
public class BTest_AddAttributeValue {
	private WebClient webClient;
	private WorkSheet f_ws = DBExelTestTools.fetchTestWorkSheet("AddAttrValue WorkSheet");

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
		int l_refNrOfAttrCols = l_currentTable.getRow(0).getCells()
				.size();

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
		assertEquals(l_refNrOfAttrCols + 1,
				l_numberOfAttributeColumns);

		assertEquals(l_newAttributeName, l_currentTable.getRow(0)
				.getCell(l_numberOfAttributeColumns-2).getTextContent());

		// 7. Test Ramp-Down
	}

}
