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

import com.gargoylesoftware.htmlunit.ConfirmHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import dbexel.model.dao.JPATestTools;
import dbexel.model.dao.WorkSheetDao;
import dbexel.model.dao.WorkSheetDaoImpl;
import dbexel.model.mapping.WorkSheet;
import dbexel.presentation.actions.AttributeTypes;
import dbexel.system.DBEXEL_PAGES;
import dbexel.system.tools.DBExelHtmlUnitSupport;
import dbexel.system.tools.DBExelTestTools;
import dbexel.system.tools.FileUtils;

public class WorkSheetAAT {
	private static WorkSheetDao wsDao = new WorkSheetDaoImpl();
	private static WebClient webClient;

	@BeforeClass
	public static void cleanSystem() {
		JPATestTools.cleanAllData();
	}

	@Before
	public void initSystem() {

		webClient = new WebClient();
		webClient.setCssEnabled(false);
	}

	@After
	public void cleanWindows() {
		JPATestTools.cleanAllData();
		webClient.closeAllWindows();
	}

	@Test
	public void testCreate1() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		// Get start page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite") + "/DBEXEL/"));

		WebAssert.assertLinkPresent(l_currentPage, "createWorkSheet");

		// Second page
		l_currentPage = ((HtmlAnchor) l_currentPage
				.getElementById("createWorkSheet")).click();

		// We should be at the first page of the WorkSheet Creation
		assertEquals(DBEXEL_PAGES.CreateWorkSheetStep1.toString(),
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"));

		HtmlForm l_form = (HtmlForm) l_currentPage
				.getElementById("wsCreateSentance");

		HtmlTextArea l_input2 = l_form.getTextAreaByName(FileUtils
				.fetchProperty("dbexel.testsite.TestCreate.page2.input2"));

		assertEquals("2A", "Hello cruel World", l_input2.getDefaultValue());

		l_input2.setText("Hello cruel World 15.2");

		HtmlSubmitInput l_submitButton = l_form.getInputByValue("Submit");

		l_currentPage = l_submitButton.click();

		// Checking the new page
		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"),
				is(equalTo(DBEXEL_PAGES.CreateWorkSheetStep2.toString())));

		WebAssert.assertFormPresent(l_currentPage, "StoreInitWorkSheetAction");

		// Ensure that we have now the workSheet Input Field
		HtmlForm l_currentForm = l_currentPage
				.getFormByName("StoreInitWorkSheetAction");

		WebAssert.assertInputPresent(l_currentPage, "workSheetName");
		l_currentForm.getInputByName("workSheetName").setValueAttribute(
				"First Worksheet");

		assertThat(
				"There are no type selects available.",
				DBExelHtmlUnitSupport.getSelectsByNameStart(l_currentForm,
						"columnTypes[").size(), is(greaterThan(0)));

		// Checking the correct division of the sentence
		WebAssert.assertInputContainsValue(l_currentPage, "attributeValues[0]",
				"Hello");
		WebAssert.assertInputContainsValue(l_currentPage, "attributeValues[1]",
				"cruel");
		WebAssert.assertInputContainsValue(l_currentPage, "attributeValues[2]",
				"World");

		WebAssert.assertInputContainsValue(l_currentPage, "attributeValues[3]",
				"15.2");

		// Checking that the column types are correctly set
		// 79: I would like to suggest a type for a column
		List<HtmlSelect> l_attributeTypes = DBExelHtmlUnitSupport
				.getSelectsByNameStart(l_currentForm, "columnTypes[");
		assertThat("There is a correct number of selects.",
				l_attributeTypes.size(), is(equalTo(4)));

		assertThat(l_attributeTypes.get(0).getSelectedOptions().get(0)
				.getText(), is(equalTo(AttributeTypes.Text.toString())));
		
		assertThat(l_attributeTypes.get(1).getSelectedOptions().get(0)
				.getText(), is(equalTo(AttributeTypes.Text.toString())));
		
		assertThat(l_attributeTypes.get(2).getSelectedOptions().get(0)
				.getText(), is(equalTo(AttributeTypes.Text.toString())));
		
		assertThat(l_attributeTypes.get(3).getSelectedOptions().get(0)
				.getText(), is(equalTo(AttributeTypes.Number.toString())));

		// 8 Inputs
		// 6 normal ones
		// 1 submit button
		// 1 hidden
		assertEquals("Bad number of inputs", 9, DBExelHtmlUnitSupport
				.fetchVisibleInputFields(l_currentForm).size());

		assertEquals(
				"Bad number of header fields",
				DBExelHtmlUnitSupport.getInputsByNameStart(l_currentForm,
						"attributeNames[").size(),
				DBExelHtmlUnitSupport.getInputsByNameStart(l_currentForm,
						"attributeValues[").size());

		assertEquals(
				"Bad number of select fields",
				DBExelHtmlUnitSupport.getInputsByNameStart(l_currentForm,
						"attributeNames[").size(),
				DBExelHtmlUnitSupport.getInputsByNameStart(l_currentForm,
						"keepColumns").size());

		l_currentForm.getInputByName("attributeNames[0]")
				.setValueAttribute("A");
		l_currentForm.getInputByName("attributeNames[1]")
				.setValueAttribute("B");
		l_currentForm.getInputByName("attributeNames[2]")
				.setValueAttribute("C");
		l_currentForm.getInputByName("attributeNames[3]")
				.setValueAttribute("D");

		l_currentForm.getSelectByName("columnTypes[0]").setSelectedAttribute(
				AttributeTypes.Text.toString(), true);
		l_currentForm.getSelectByName("columnTypes[1]").setSelectedAttribute(
				AttributeTypes.Text.toString(), true);
		l_currentForm.getSelectByName("columnTypes[2]").setSelectedAttribute(
				AttributeTypes.Text.toString(), true);
		l_currentForm.getSelectByName("columnTypes[3]").setSelectedAttribute(
				AttributeTypes.Number.toString(), true);

		l_currentPage = DBExelHtmlUnitSupport.getSubmitButton(l_currentForm)
				.click();

		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"),
				is(equalTo(DBEXEL_PAGES.WelcomeToDBExel.toString())));

		WebAssert.assertLinkPresentWithText(l_currentPage, "First Worksheet");
		
		HtmlAnchor l_anchor = l_currentPage.getAnchorByText("First Worksheet");
		String l_className = l_anchor.getAttribute("class");
		assertThat(l_className, is(equalTo("newWorkSheet")));
	}

	@Test
	public void testCreate2() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		// Get start page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite") + "/DBEXEL/"));

		// Second page
		l_currentPage = ((HtmlAnchor) l_currentPage
				.getElementById("createWorkSheet")).click();

		HtmlForm l_currentForm = (HtmlForm) l_currentPage
				.getElementById("wsCreateSentance");

		String l_thisWorkSheet = "Loans WorkSheet";

		l_currentForm
				.getTextAreaByName(
						FileUtils
								.fetchProperty("dbexel.testsite.TestCreate.page2.input2"))
				.setText("Lended 5� to Johnathan on 15/10/2012");

		// Go to the next Page
		l_currentPage = DBExelHtmlUnitSupport.getSubmitButton(l_currentForm)
				.click();

		l_currentForm = l_currentPage.getFormByName("StoreInitWorkSheetAction");

		WebAssert.assertInputPresent(l_currentPage, "workSheetName");

		l_currentForm.getInputByName("workSheetName").setValueAttribute(
				l_thisWorkSheet);

		assertEquals(
				"1. Bad number of sentance divisions",
				6,
				DBExelHtmlUnitSupport.getInputsByNameStart(l_currentForm,
						"attributeNames[").size());

		l_currentForm.getInputByName("attributeNames[0]")
				.setValueAttribute("A");
		l_currentForm.getInputByName("attributeNames[1]").setValueAttribute(
				"Amount");
		l_currentForm.getInputByName("attributeNames[2]")
				.setValueAttribute("C");
		l_currentForm.getInputByName("attributeNames[3]").setValueAttribute(
				"Person");
		l_currentForm.getInputByName("attributeNames[4]")
				.setValueAttribute("C");
		l_currentForm.getInputByName("attributeNames[5]").setValueAttribute(
				"Loan Date");

		((HtmlCheckBoxInput) l_currentPage.getElementByName("keepColumns[0]"))
				.setChecked(false);
		((HtmlCheckBoxInput) l_currentPage.getElementByName("keepColumns[2]"))
				.setChecked(false);
		((HtmlCheckBoxInput) l_currentPage.getElementByName("keepColumns[4]"))
				.setChecked(false);

		// Define the types

		// Go to the next page
		l_currentPage = DBExelHtmlUnitSupport.getSubmitButton(l_currentForm)
				.click();

		// Enter New Worksheet
		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"),
				is(equalTo(DBEXEL_PAGES.WelcomeToDBExel.toString())));

		WebAssert.assertLinkPresentWithText(l_currentPage, l_thisWorkSheet);

		l_currentPage = l_currentPage.getAnchorByText(l_thisWorkSheet).click();

		assertEquals(DBEXEL_PAGES.WorkSheetDetails.toString(),
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"));
		
		WebAssert.assertTitleEquals(l_currentPage, "Work Sheet "
				+ l_thisWorkSheet + " Details");
		WebAssert.assertTextPresent(l_currentPage, "Work Sheet "
				+ l_thisWorkSheet);
		final HtmlTable l_currentTable = (HtmlTable) l_currentPage
				.getElementById("Entry_Table");

		// We have one row per entry + 1 column for headers
		assertEquals("1. Wrong number of rows", 2, l_currentTable.getRowCount());

		// We have one column per attribute + 1 column for actions
		assertEquals("1. wrong number of columns", 4, l_currentTable.getRow(0)
				.getCells().size());

	}

	@Test
	public void testDeleteWorkSheet() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		// Go to start page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite") + "/DBEXEL/"));

		WorkSheet l_workSheet = DBExelTestTools
				.fetchTestWorkSheet("testDeleteWorkSheet");

		wsDao.createWorkSheet(l_workSheet);

		// Go to start page
		l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite") + "/DBEXEL/"));

		// select New Worksheet
		l_currentPage = l_currentPage.getAnchorByText(l_workSheet.getWs_Name())
				.click();

		String l_deleteLink = "Delete this Work Sheet";
		WebAssert.assertLinkPresentWithText(l_currentPage, l_deleteLink);

		// Start deleting page
		webClient.setConfirmHandler(new ConfirmHandler() {
			public boolean handleConfirm(Page page, String message) {
				return false;
			}

		});
		boolean l_isNotConfirmed = webClient.getConfirmHandler().handleConfirm(
				l_currentPage, "Delete this work sheet?");
		assertFalse("Confirm window has not been invoked", l_isNotConfirmed);

		l_currentPage = l_currentPage.getAnchorByText(l_deleteLink).click();

		// Now we should be back in the worksheet details page
		assertEquals(DBEXEL_PAGES.WorkSheetDetails.toString(),
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"));

		// Now let us acknowledge the deletion
		// Start deleting page
		webClient.setConfirmHandler(new ConfirmHandler() {
			public boolean handleConfirm(Page page, String message) {
				return true;
			}

		});

		l_currentPage = l_currentPage.getAnchorByText(l_deleteLink).click();
		// We should now be back at the start page
		WebAssert.assertTitleEquals(l_currentPage, "Welcome to DBExel");
		assertEquals(DBEXEL_PAGES.WelcomeToDBExel.toString(),
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"));
	}

	@Test
	public void testEdit() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		// We will be changing the work sheet name
		String l_thisWorkSheet = "Edit WorkSheet";
		// to the worksheet name
		String l_newWSName = "Edited WorkSheet";

		// Get first page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite") + "/DBEXEL/"));

		WorkSheet l_workSheet = DBExelTestTools
				.fetchTestWorkSheet(l_thisWorkSheet);

		wsDao.createWorkSheet(l_workSheet);

		l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite") + "/DBEXEL/"));

		// Check that our newly created link is there
		WebAssert.assertLinkPresentWithText(l_currentPage, l_thisWorkSheet);
		WebAssert.assertLinkNotPresentWithText(l_currentPage, l_newWSName);

		// go to worksheet details
		l_currentPage = l_currentPage.getAnchorByText(l_thisWorkSheet).click();

		assertEquals(DBEXEL_PAGES.WorkSheetDetails.toString(),
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"));

		WebAssert.assertLinkPresentWithText(l_currentPage,
				"Edit this Work Sheet");

		// Go to the Edit page
		l_currentPage = l_currentPage.getAnchorByText("Edit this Work Sheet")
				.click();

		assertEquals(DBEXEL_PAGES.UpdateWorkSheet.toString(),
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"));

		// give new name to Work Sheet
		HtmlForm l_currentForm = l_currentPage.getForms().get(0);

		final HtmlTextInput textField = l_currentForm
				.getInputByName("newWSName");

		// Change the value of the text field
		textField.setValueAttribute(l_newWSName);
		// l_currentForm.setAttribute("newWSName",l_newWSName);

		// Submit form
		// l_currentPage =
		// DBExelHtmlUnitSupport.getSubmitButton(l_currentForm).click();
		l_currentPage = ((HtmlSubmitInput) l_currentPage
				.getElementById("editWorkSheet")).click();

		DBExelHtmlUnitSupport.getSubmitButton(l_currentForm).click();

		assertEquals("Work Sheet " + l_newWSName + " Details",
				l_currentPage.getTitleText());

		// Go to the start page is the new link present?
		l_currentPage = l_currentPage.getAnchorByText("See the Work Sheets")
				.click();

		WebAssert.assertLinkPresentWithText(l_currentPage, l_newWSName);
		WebAssert.assertLinkNotPresentWithText(l_currentPage, l_thisWorkSheet);

		// Now for the final test
		// Use the new link. Do we go to the expected page?
		l_currentPage = l_currentPage.getAnchorByText(l_newWSName).click();
		assertEquals("Work Sheet " + l_newWSName + " Details",
				l_currentPage.getTitleText());

	}

	@Test
	public void testView() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		// WebClient webClient = new WebClient();
		// Get first page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite") + "/DBEXEL/"));

		WorkSheet l_workSheet = DBExelTestTools
				.fetchTestWorkSheet("testView2 WorkSheet");

		wsDao.createWorkSheet(l_workSheet);

		l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite") + "/DBEXEL/"));

		// Enter New Worksheet
		l_currentPage = l_currentPage.getAnchorByText(l_workSheet.getWs_Name())
				.click();

		final HtmlTable l_currentTable = (HtmlTable) l_currentPage
				.getElementById("Entry_Table");

		// We have one row per entry + 1 column for headers
		assertEquals("1. Wrong number of rows", 3, l_currentTable.getRowCount());

		// We have one column per attribute + 1 column for actions
		assertEquals("1. wrong number of columns", 6, l_currentTable.getRow(0)
				.getCells().size());

		// assertTrue("Implement link validations", false);
	}

	
	//Buggs
	
	/**
	 * 128: 	Error when entering the same name for more than one column
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	@Test
	public void testBugCreatingTwoHomonymousAttributes() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		// Get start page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite") + "/DBEXEL/"));

		// Second page
		l_currentPage = ((HtmlAnchor) l_currentPage
				.getElementById("createWorkSheet")).click();

		HtmlForm l_form = (HtmlForm) l_currentPage
				.getElementById("wsCreateSentance");

		HtmlTextArea l_input2 = l_form.getTextAreaByName(FileUtils
				.fetchProperty("dbexel.testsite.TestCreate.page2.input2"));

		l_input2.setText("Hello cruel World 15.2");

		HtmlSubmitInput l_submitButton = l_form.getInputByValue("Submit");

		l_currentPage = l_submitButton.click();

		// Checking the new page
		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"),
				is(equalTo(DBEXEL_PAGES.CreateWorkSheetStep2.toString())));

		// Ensure that we have now the workSheet Input Field
		HtmlForm l_currentForm = l_currentPage
				.getFormByName("StoreInitWorkSheetAction");

		l_currentForm.getInputByName("workSheetName").setValueAttribute(
				"First Worksheet");

		l_currentForm.getInputByName("attributeNames[0]")
				.setValueAttribute("A");
		l_currentForm.getInputByName("attributeNames[1]")
				.setValueAttribute("A");
		l_currentForm.getInputByName("attributeNames[2]")
				.setValueAttribute("C");
		l_currentForm.getInputByName("attributeNames[3]")
				.setValueAttribute("D");

		l_currentForm.getSelectByName("columnTypes[0]").setSelectedAttribute(
				AttributeTypes.Text.toString(), true);
		l_currentForm.getSelectByName("columnTypes[1]").setSelectedAttribute(
				AttributeTypes.Text.toString(), true);
		l_currentForm.getSelectByName("columnTypes[2]").setSelectedAttribute(
				AttributeTypes.Text.toString(), true);
		l_currentForm.getSelectByName("columnTypes[3]").setSelectedAttribute(
				AttributeTypes.Number.toString(), true);

		l_currentPage = DBExelHtmlUnitSupport.getSubmitButton(l_currentForm)
				.click();

		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"),
				is(equalTo(DBEXEL_PAGES.WelcomeToDBExel.toString())));
	}
	
	// Template for UI Tests
	// 1. Test Ramp-Up
	// 2. Validate Starting Point
	// 3. Take References
	// 4. Initiate Tested Action
	// 5. Make Assertions
	// 6. Make reiterations
	// 7. Test Ramp-Down
}
