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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

import dbexel.model.dao.JPATestTools;
import dbexel.model.dao.WorkSheetDao;
import dbexel.model.dao.WorkSheetDaoImpl;
import dbexel.model.mapping.WorkSheet;
import dbexel.presentation.actions.AttributeTypes;
import dbexel.system.DBEXEL_PAGES;
import dbexel.system.tools.DBExelHtmlUnitSupport;
import dbexel.system.tools.DBExelTestTools;
import dbexel.system.tools.FileUtils;

import static org.junit.Assert.*;

/**
 * @author gandomi
 * 
 */
public class AttributeDashboard {

	private WebClient webClient;
	private WorkSheet f_ws;

	private URL startPageUrl;

	@BeforeClass
	public static void cleanUp() {
		JPATestTools.cleanAllData();
	}

	@Before
	public void initSystem() throws MalformedURLException {
		webClient = new WebClient(BrowserVersion.FIREFOX_3_6);
		webClient.setJavaScriptEnabled(true);
		webClient.getCookieManager().setCookiesEnabled(true);

		f_ws = DBExelTestTools.fetchTestWorkSheet("AddAttrValue WorkSheet");
		WorkSheetDao l_wsDao = new WorkSheetDaoImpl();
		l_wsDao.createWorkSheet(f_ws);

		startPageUrl = new URL(FileUtils.fetchProperty("dbexel.testsite")
				+ "/DBEXEL/ShowDistinctWorkSheet.action?workSheet_Id="
				+ f_ws.getWs_Id());
	}

	// Test flow
	// I75_AC1 : Given that I have a list of entries. I should be able to go to
	// the Attribute Dashboard by selecting its attribute
	@Test
	public void weShouldBeAbleToBrowseToAttributePageFromTheWorkSheetDetailPageAndGoBackOnSubmit()
			throws FailingHttpStatusCodeException, IOException {
		// 1. Test Ramp-Up
		HtmlPage l_currentPage = webClient.getPage(startPageUrl);

		// l_currentPage.get
		// 2. Validate Starting Point
		assertThat("Are we starting in the right page?",
				l_currentPage.getTitleText(),
				allOf(startsWith("Work Sheet"), endsWith("Details")));

		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"), equalTo(DBEXEL_PAGES.WorkSheetDetails.toString()));

		// 3. Take References
		// Get the page name
		String l_refStartPageTitle = l_currentPage.getTitleText();

		HtmlTableCell l_attributeCell = DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(0)
				.getCell(0);
		assertThat("The attributes should be browsable",
				l_attributeCell.getFirstChild(),
				is(instanceOf(HtmlAnchor.class)));

		HtmlAnchor l_attributeLink = (HtmlAnchor) l_attributeCell
				.getFirstChild();

		// 4. Initiate Tested Action - We should reach the attribute details
		// page
		l_currentPage = l_attributeLink.click();

		// 5. Make Assertions
		assertThat("We should now be in the Attribute Detail Page",
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"),
				is(equalTo(DBEXEL_PAGES.AttributeDetails.toString())));

		HtmlForm l_currentForm = (HtmlForm) l_currentPage
				.getElementById("Attribute_Form");

		// 6. Test Iteration
		// 4. Initiate Tested Action
		l_currentPage = DBExelHtmlUnitSupport.getSubmitButton(l_currentForm)
				.click();

		// 5. Make Assertions
		assertThat("We should now be back in the Worksheet Detail Page",
				l_currentPage.getTitleText(), is(equalTo(l_refStartPageTitle)));

		// 7. Test Ramp-Down
	}

	// I75_AC2 : Given that I am in the attribute dashboard I should be able to
	// enter a new name for the attribute.
	@Test
	public void weShouldBeAbleToChangeTheNameOfAnAttributeInTheAttributeDetailPage()
			throws FailingHttpStatusCodeException, IOException {
		// 1. Test Ramp-Up
		HtmlPage l_currentPage = webClient.getPage(startPageUrl);

		// l_currentPage.get
		// 2. Validate Starting Point
		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"), equalTo(DBEXEL_PAGES.WorkSheetDetails.toString()));

		// 3. Take References
		// Get the page name
		String l_refStartPageTitle = l_currentPage.getTitleText();

		// Since we will be changing an attribute we will get the value of that
		// cell.
		HtmlAnchor l_attributeLink = (HtmlAnchor) DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(0)
				.getCell(0).getFirstChild();

		String l_refAttributeName = l_attributeLink.getTextContent();

		// 4. Initiate Tested Action
		l_currentPage = l_attributeLink.click();

		// 5. Make Assertions
		assertThat("We should now be in the Attribute Detail Page",
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"),
				is(equalTo(DBEXEL_PAGES.AttributeDetails.toString())));

		HtmlForm l_currentForm = (HtmlForm) l_currentPage
				.getElementById("Attribute_Form");

		// Form Validations
		assertThat(DBExelHtmlUnitSupport.fetchVisibleInputFields(l_currentForm)
				.size(), equalTo(1));

		// Validating Attribute Name
		assertThat("We did not find the input field for attribute names",
				l_currentForm.getInputsByName("attributeName").size(),
				is(equalTo(1)));

		HtmlInput l_inputField = l_currentForm.getInputByName("attributeName");

		// Does the attribute have an initial value?
		assertThat(l_inputField.getValueAttribute(),
				is(equalTo(l_refAttributeName)));

		// Validating Attribute Types
		assertThat("We did not find the input field for attribute types",
				l_currentForm.getSelectsByName("attributeType").size(),
				is(equalTo(1)));

		HtmlSelect l_attributeTypeField = l_currentForm
				.getSelectByName("attributeType");

		assertThat(l_attributeTypeField.getSelectedOptions().get(0)
				.getTextContent(), is(equalTo(f_ws.getAttributes().get(0)
				.getType().toString())));

		// Give a new name to the attributes
		String l_refNewAttributeName = l_refAttributeName + "_changed";

		// Set new value
		l_inputField.setValueAttribute(l_refNewAttributeName);

		// 6. Test Iteration
		// 4. Initiate Tested Action
		l_currentPage = DBExelHtmlUnitSupport.getSubmitButton(l_currentForm)
				.click();

		// 5. Make Assertions
		assertThat(DBExelHtmlUnitSupport.getMetaPageName(l_currentPage), equalTo(DBEXEL_PAGES.WorkSheetDetails));
		
		assertThat("We should now be back in the Worksheet Detail Page",
				l_currentPage.getTitleText(), is(equalTo(l_refStartPageTitle)));

		l_attributeLink = (HtmlAnchor) DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(0)
				.getCell(0).getFirstChild();

		l_refAttributeName = l_attributeLink.getTextContent();

		assertThat(l_attributeLink.getTextContent(),
				equalTo(l_refNewAttributeName));

		// 7. Test Ramp-Down
	}

	// I75_AC3 : Given that I am in the attribute dashboard I should see the
	// worksheets it is attached to.

	// I75_AC4 : Given that I am in the attribute dashboard I should see its
	// values.

	// I75_AC4A : The list of values are unique
	// enter a new name for the attribute.
	@Test
	public void weShouldHaveTheCorrectContentsInTheAtrributePage()
			throws FailingHttpStatusCodeException, IOException {
		// 1. Test Ramp-Up
		HtmlPage l_currentPage = webClient.getPage(startPageUrl);

		// l_currentPage.get
		// 2. Validate Starting Point
		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"), equalTo(DBEXEL_PAGES.WorkSheetDetails.toString()));

		// 3. Take References
		// Get the page name
		// retrieve the worksheet name.
		String l_refWorkSheetName = DBExelHtmlUnitSupport.getMetaContentByName(
				l_currentPage, "main_actor.name");

		// Fetch all Values for the workSheet
		List<String> l_refAttributeValues = new ArrayList<String>();

		HtmlTable l_currentTable = DBExelHtmlUnitSupport.getTableById(
				l_currentPage, "Entry_Table");

		// Take a reference of all the entries at the first column
		for (int i = 1; i < l_currentTable.getRows().size(); i++) {
			l_refAttributeValues.add(l_currentTable.getRow(i).getCell(0)
					.getTextContent());
		}

		HtmlTableCell l_attributeCell = l_currentTable.getRow(0).getCell(0);

		assertThat(l_attributeCell.getFirstChild(),
				is(instanceOf(HtmlAnchor.class)));

		HtmlAnchor l_attributeLink = (HtmlAnchor) l_attributeCell
				.getFirstChild();

		// 4. Initiate Tested Action
		l_currentPage = l_attributeLink.click();

		// 5. Make Assertions
		// Are we in the right page?
		assertThat("We should now be in the Attribute Detail Page",
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"),
				is(equalTo(DBEXEL_PAGES.AttributeDetails.toString())));

		// Checking the main categories of information

		// The form object to change the attribute name should be present
		WebAssert.assertElementPresent(l_currentPage, "Attribute_Form");

		// Check WorkSheet Table Contents
		WebAssert.assertElementPresent(l_currentPage, "WorkSheet_Table");
		HtmlTable l_workSheetTable = DBExelHtmlUnitSupport.getTableById(
				l_currentPage, "WorkSheet_Table");

		List<String> l_workSheetList = new ArrayList<String>();
		for (int i = 1; i < l_workSheetTable.getRowCount(); i++) {
			l_workSheetList.add(l_workSheetTable.getRow(i).getCell(0)
					.getTextContent());
		}

		assertThat(
				"The worksheet we came from was not among the worksheet list",
				l_refWorkSheetName, isIn(l_workSheetList));

		// Check Attribute Value Table Contents
		WebAssert.assertElementPresent(l_currentPage, "AttributeValue_Table");

		HtmlTable l_attributeValueTable = DBExelHtmlUnitSupport.getTableById(
				l_currentPage, "AttributeValue_Table");

		List<String> l_attributeValueList = new ArrayList<String>();

		for (int i = 1; i < l_attributeValueTable.getRowCount(); i++) {
			l_attributeValueList.add(l_attributeValueTable.getRow(i).getCell(0)
					.getTextContent());
		}

		assertTrue(
				"Some attribute values are missing in the attribute value table",
				l_refAttributeValues.containsAll(l_attributeValueList));

		// Testing that the attribute values are unique
		List<String> l_uniqueAVList = new ArrayList<String>(
				new HashSet<String>(l_attributeValueList));

		assertThat(l_uniqueAVList.size(),
				is(equalTo(l_attributeValueList.size())));

		// 6. Test Iteration
		// 7. Test Ramp-Down
	}
	
	//This follows the bug 133 - IndexOutOfBoundsException when making changes to attribute in a one entry worksheet
	@Test
	public void testBug133() throws FailingHttpStatusCodeException,
			MalformedURLException, IOException {

		// Get start page
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(new URL(FileUtils
				.fetchProperty("dbexel.testsite") + "/DBEXEL/"));

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

		l_input2.setText("Hello cruel world 15.2");

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

		// Checking that the column types are correctly set
		// 79: I would like to suggest a type for a column
		List<HtmlSelect> l_attributeTypes = DBExelHtmlUnitSupport
				.getSelectsByNameStart(l_currentForm, "columnTypes[");

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
				AttributeTypes.Text.toString(), true);

		l_currentPage = DBExelHtmlUnitSupport.getSubmitButton(l_currentForm)
				.click();

		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
				"pageName"),
				is(equalTo(DBEXEL_PAGES.WelcomeToDBExel.toString())));
		
		//Go to workSheet details
		l_currentPage = l_currentPage.getAnchorByText("First Worksheet").click();

		assertThat(DBExelHtmlUnitSupport.getMetaPageName(l_currentPage),
				is(equalTo(DBEXEL_PAGES.WorkSheetDetails)));
		
		l_currentPage = l_currentPage.getElementById("attributeDetails_3").click();
		
		assertThat(DBExelHtmlUnitSupport.getMetaPageName(l_currentPage), equalTo(DBEXEL_PAGES.AttributeDetails));
		
		assertThat(DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage, "main_actor.name"), is(equalTo("D")));
		
		l_currentForm = l_currentPage.getFormByName("Attribute_Form");
		
		l_currentForm.getSelectByName("attributeType").setSelectedAttribute(AttributeTypes.Number.toString(), true);
		
		l_currentPage = DBExelHtmlUnitSupport.getSubmitButton(l_currentForm)
				.click();
		
		assertThat(DBExelHtmlUnitSupport.getMetaPageName(l_currentPage),
				is(equalTo(DBEXEL_PAGES.WorkSheetDetails)));
	}
}
