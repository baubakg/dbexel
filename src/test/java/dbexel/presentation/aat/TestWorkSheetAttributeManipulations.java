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

import static org.hamcrest.Matchers.*;

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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import dbexel.model.dao.JPATestTools;
import dbexel.model.dao.WorkSheetDao;
import dbexel.model.dao.WorkSheetDaoImpl;
import dbexel.model.mapping.WorkSheet;
import dbexel.system.DBEXEL_PAGES;
import dbexel.system.tools.DBExelHtmlUnitSupport;
import dbexel.system.tools.DBExelTestTools;
import dbexel.system.tools.FileUtils;

import static org.junit.Assert.*;

/**
 * @author gandomi
 * 
 */
public class TestWorkSheetAttributeManipulations {

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

	@After
	public void cleanWindows() {
		JPATestTools.cleanAllData();
		webClient.closeAllWindows();

	}

	// Test flow
	// We move from the worksheet detail page
	@Test
	public void deleteAnAttribute() throws FailingHttpStatusCodeException,
			IOException {
		// 1. Test Ramp-Up
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(startPageUrl);

		// l_currentPage.get
		// 2. Validate Starting Point
		assertThat("Are we starting in the right page?",
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"),
				equalTo(DBEXEL_PAGES.WorkSheetDetails.toString()));

		// 3. Take References
		// Get the page name
		String l_refStartPageTitle = l_currentPage.getTitleText();

		int l_lastDataColumnIdx = DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(0)
				.getCells().size() - 2;

		// Make a generic test item
		int l_testCellIdx = l_lastDataColumnIdx > 0 ? (l_lastDataColumnIdx - 1)
				: 0;

		HtmlAnchor l_attributeLink = (HtmlAnchor) DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(0)
				.getCell(l_testCellIdx).getFirstChild();

		String l_attributeValue = DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(1)
				.getCell(l_testCellIdx).getTextContent();

		int l_nrOfRows = DBExelHtmlUnitSupport.getTableById(l_currentPage,
				"Entry_Table").getRowCount();

		int l_nrOfColumns = DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(0)
				.getCells().size();

		WebAssert.assertLinkPresentWithText(l_currentPage,
				"Edit this Work Sheet");

		// 4. Initiate Tested Action
		l_currentPage = ((HtmlAnchor) l_currentPage.getElementById("editWS"))
				.click();

		// 5. Make Assertions
		assertThat("We should now be in the Attribute Detail Page",
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"),
				is(equalTo(DBEXEL_PAGES.UpdateWorkSheet.toString())));

		assertThat(
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(1)
						.getCell(l_testCellIdx).getFirstChild()
						.getTextContent(),
				equalTo(l_attributeLink.getTextContent()));

		assertThat(
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(2)
						.getCell(l_testCellIdx).getTextContent(),
				equalTo(l_attributeValue));

		// 6. Make reiterations

		webClient.setConfirmHandler(new ConfirmHandler() {
			public boolean handleConfirm(Page page, String message) {
				return true;
			}

		});

		// 4. Initiate Tested Action
		l_currentPage = ((HtmlAnchor) l_currentPage
				.getElementById("deleteAttribute_1")).click();

		assertThat("We should still be in the Edit Worksheet Page",
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"),
				is(equalTo(DBEXEL_PAGES.UpdateWorkSheet.toString())));

		assertThat(
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(1)
						.getCell(l_testCellIdx).getFirstChild()
						.getTextContent(),
				not(equalTo(l_attributeLink.getTextContent())));

		assertThat(
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(1)
						.getCell(l_testCellIdx).getTextContent(),
				not(equalTo(l_attributeValue)));

		assertThat(
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table")
						.getRowCount(), equalTo(l_nrOfRows + 1));

		assertThat(
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(0)
						.getCells().size(), equalTo(l_nrOfColumns - 2));

		// 5. Make Assertions

		l_currentPage = ((HtmlSubmitInput) l_currentPage
				.getElementById("backToWorkSheetDetail")).click();

		assertThat("We should now be back in the Worksheet Detail Page",
				l_currentPage.getTitleText(), is(equalTo(l_refStartPageTitle)));

		assertThat(
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(0)
						.getCell(l_testCellIdx).getFirstChild().toString(),
				not(equalTo(l_attributeLink.toString())));

		assertThat(
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(1)
						.getCell(l_testCellIdx).getTextContent(),
				not(equalTo(l_attributeValue)));

		assertThat(
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table")
						.getRowCount(), equalTo(l_nrOfRows));

		assertThat(
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(0)
						.getCells().size(), equalTo(l_nrOfColumns - 1));

		// 7. Test Ramp-Down
	}

	// Testing column movements
	// In this test we move the column to the right
	@Test
	public void moveRight() throws FailingHttpStatusCodeException, IOException {
		// 1. Test Ramp-Up
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(startPageUrl);

		// l_currentPage.get
		// 2. Validate Starting Point
		assertThat("Are we starting in the right page?",
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"),
				equalTo(DBEXEL_PAGES.WorkSheetDetails.toString()));

		// 3. Take References
		// Get the page name
		String l_refStartPageTitle = l_currentPage.getTitleText();

		int l_lastDataColumnIdx = DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(0)
				.getCells().size() - 2;

		// Make a generic test item
		int l_testCellIdx = l_lastDataColumnIdx > 0 ? (l_lastDataColumnIdx - 1)
				: 0;

		HtmlAnchor l_attributeLink = (HtmlAnchor) DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(0)
				.getCell(l_testCellIdx).getFirstChild();

		String l_attributeName = l_attributeLink.getTextContent();

		String l_rightSideAttributeName = ((HtmlAnchor) DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(0)
				.getCell(l_testCellIdx + 1).getFirstChild()).getTextContent();

		String l_attributeValue = DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(1)
				.getCell(l_testCellIdx).getTextContent();

		String l_rightSideAttributeValue = DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(1)
				.getCell(l_testCellIdx + 1).getTextContent();

		int l_nrOfColumns = DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(0)
				.getCells().size();

		WebAssert.assertLinkPresentWithText(l_currentPage,
				"Edit this Work Sheet");

		// 3A. Prepare test start
		l_currentPage = ((HtmlAnchor) l_currentPage.getElementById("editWS"))
				.click();

		// We do not make any verifications since this has been done in the
		// previous test "DeletAnAttribute"

		// 4. Initiate Tested Action
		// Press move right
		l_currentPage = ((HtmlAnchor) l_currentPage.getElementById("moveRight_"
				+ l_testCellIdx)).click();

		// 5. Make Assertions
		assertThat("We should now be in the Edit WorkSheet Page",
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"),
				is(equalTo(DBEXEL_PAGES.UpdateWorkSheet.toString())));

		// Our previous references should now be one column to the right
		assertThat("The columns header has not been shifted to the right",
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(1)
						.getCell(l_testCellIdx + 1).getFirstChild()
						.getTextContent(), is(equalTo(l_attributeName)));

		assertThat("The columns value has not been shifted to the right",
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(2)
						.getCell(l_testCellIdx + 1).getTextContent(),
				equalTo(l_attributeValue));

		// We should now have the old column to the right at the position we
		// took references
		assertThat("The columns header has not been shifted to the right",
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(1)
						.getCell(l_testCellIdx).getFirstChild()
						.getTextContent(),
				is(equalTo(l_rightSideAttributeName)));

		assertThat("The columns value has not been shifted to the right",
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(2)
						.getCell(l_testCellIdx).getTextContent(),
				equalTo(l_rightSideAttributeValue));

		// 6. Make reiterations
		// We now move back to the WorkSheet detail page to see that the changes
		// have taken effect
		l_currentPage = ((HtmlSubmitInput) l_currentPage
				.getElementById("backToWorkSheetDetail")).click();

		assertThat("We should now be back in the Worksheet Detail Page",
				l_currentPage.getTitleText(), is(equalTo(l_refStartPageTitle)));

		assertThat("The columns header has not been shifted to the right",
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(0)
						.getCell(l_testCellIdx + 1).getFirstChild()
						.getTextContent(), is(equalTo(l_attributeName)));

		assertThat("The columns value has not been shifted to the right",
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(1)
						.getCell(l_testCellIdx + 1).getTextContent(),
				equalTo(l_attributeValue));

		// We should now have the old column to the right at the position we
		// took references
		assertThat("The columns header has not been shifted to the right",
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(0)
						.getCell(l_testCellIdx).getFirstChild()
						.getTextContent(),
				is(equalTo(l_rightSideAttributeName)));

		assertThat("The columns value has not been shifted to the right",
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(1)
						.getCell(l_testCellIdx).getTextContent(),
				equalTo(l_rightSideAttributeValue));

		assertThat("The number of columns should not change in this test",
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(0)
						.getCells().size(), is(equalTo(l_nrOfColumns)));

		// 7. Test Ramp-Down
	}

	// In this test we move the column to the left
	@Test
	public void moveLeft() throws FailingHttpStatusCodeException, IOException {
		// 1. Test Ramp-Up
		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(startPageUrl);

		// l_currentPage.get
		// 2. Validate Starting Point
		assertThat("Are we starting in the right page?",
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"),
				equalTo(DBEXEL_PAGES.WorkSheetDetails.toString()));

		// 3. Take References
		// Get the page name
		String l_refStartPageTitle = l_currentPage.getTitleText();

		int l_lastDataColumnIdx = DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(0)
				.getCells().size() - 2;

		// Make a generic test item
		int l_testCellIdx = l_lastDataColumnIdx > 0 ? 1 : l_lastDataColumnIdx;

		HtmlAnchor l_attributeLink = (HtmlAnchor) DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(0)
				.getCell(l_testCellIdx).getFirstChild();

		String l_attributeName = l_attributeLink.getTextContent();

		String l_rightSideAttributeName = ((HtmlAnchor) DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(0)
				.getCell(l_testCellIdx - 1).getFirstChild()).getTextContent();

		String l_attributeValue = DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(1)
				.getCell(l_testCellIdx).getTextContent();

		String l_rightSideAttributeValue = DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(1)
				.getCell(l_testCellIdx - 1).getTextContent();

		int l_nrOfColumns = DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(0)
				.getCells().size();

		WebAssert.assertLinkPresentWithText(l_currentPage,
				"Edit this Work Sheet");

		// 3A. Prepare test start
		l_currentPage = ((HtmlAnchor) l_currentPage.getElementById("editWS"))
				.click();

		// We do not make any verifications since this has been done in the
		// test "DeletAnAttribute"

		// 4. Initiate Tested Action
		// Press move right
		WebAssert.assertElementPresent(l_currentPage, "moveLeft_"
				+ l_testCellIdx);

		WebAssert.assertElementNotPresent(l_currentPage, "moveLeft_0");
		WebAssert.assertElementNotPresent(l_currentPage, "moveRight_"
				+ l_lastDataColumnIdx);

		l_currentPage = ((HtmlAnchor) l_currentPage.getElementById("moveLeft_"
				+ l_testCellIdx)).click();

		// 5. Make Assertions
		assertThat("We should now be in the Edit WorkSheet Page",
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"),
				is(equalTo(DBEXEL_PAGES.UpdateWorkSheet.toString())));

		// Our previous references should now be one column to the left
		assertThat("The columns header has not been shifted to the left",
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(1)
						.getCell(l_testCellIdx - 1).getFirstChild()
						.getTextContent(), is(equalTo(l_attributeName)));

		assertThat("The columns value has not been shifted to the left",
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(2)
						.getCell(l_testCellIdx - 1).getTextContent(),
				equalTo(l_attributeValue));

		// We should now have the old column to the left at the position we
		// took references
		assertThat(
				"The columns header that was to the left has not changed places",
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(1)
						.getCell(l_testCellIdx).getFirstChild()
						.getTextContent(),
				is(equalTo(l_rightSideAttributeName)));

		assertThat(
				"The columns value that was to the left has not changed places",
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(2)
						.getCell(l_testCellIdx).getTextContent(),
				equalTo(l_rightSideAttributeValue));

		// 6. Make reiterations
		// We now move back to the WorkSheet detail page to see that the changes
		// have taken effect
		l_currentPage = ((HtmlSubmitInput) l_currentPage
				.getElementById("backToWorkSheetDetail")).click();

		assertThat("We should now be back in the Worksheet Detail Page",
				l_currentPage.getTitleText(), is(equalTo(l_refStartPageTitle)));

		assertThat("The columns header has not been shifted to the right",
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(0)
						.getCell(l_testCellIdx - 1).getFirstChild()
						.getTextContent(), is(equalTo(l_attributeName)));

		assertThat("The columns value has not been shifted to the right",
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(1)
						.getCell(l_testCellIdx - 1).getTextContent(),
				equalTo(l_attributeValue));

		// We should now have the old column to the right at the position we
		// took references
		assertThat("The columns header has not been shifted to the right",
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(0)
						.getCell(l_testCellIdx).getFirstChild()
						.getTextContent(),
				is(equalTo(l_rightSideAttributeName)));

		assertThat("The columns value has not been shifted to the right",
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(1)
						.getCell(l_testCellIdx).getTextContent(),
				equalTo(l_rightSideAttributeValue));

		assertThat("The number of columns should not change in this test",
				DBExelHtmlUnitSupport
						.getTableById(l_currentPage, "Entry_Table").getRow(0)
						.getCells().size(), is(equalTo(l_nrOfColumns)));

		// 7. Test Ramp-Down
	}

}
