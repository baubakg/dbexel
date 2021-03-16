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

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import dbexel.model.dao.JPATestTools;
import dbexel.model.dao.WorkSheetDao;
import dbexel.model.dao.WorkSheetDaoImpl;
import dbexel.model.mapping.AttributeValue;
import dbexel.model.mapping.WorkSheet;
import dbexel.model.mapping.Entry;
import dbexel.system.DBEXEL_PAGES;
import dbexel.system.tools.DBExelHtmlUnitSupport;
import dbexel.system.tools.DBExelTestTools;
import dbexel.system.tools.FileUtils;

import static org.junit.Assert.*;

/**
 * @author gandomi
 * 
 */
public class TestEmptyWorkSheet {

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
		webClient.setCssEnabled(false);
	}

	// Test flow
	// We move from the worksheet detail page
	@Test
	public void emptyTablePresentation() throws FailingHttpStatusCodeException,
			IOException {

		// 1. Test Ramp-Up
		f_ws = new WorkSheet("Empty Worksheet");
		WorkSheetDao l_wsDao = new WorkSheetDaoImpl();
		l_wsDao.createWorkSheet(f_ws);

		startPageUrl = new URL(FileUtils.fetchProperty("dbexel.testsite")
				+ "/DBEXEL/Start.action");

		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(startPageUrl);

		// l_currentPage.get
		// 2. Validate Starting Point
		assertThat("Are we starting in the right page?",
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"),
				equalTo(DBEXEL_PAGES.WelcomeToDBExel.toString()));

		// 3. Take References
		// 4. Initiate Tested Action
		l_currentPage = l_currentPage.getAnchorByText("Empty Worksheet")
				.click();

		// 5. Make Assertions

		WebAssert.assertElementNotPresent(l_currentPage, "Entry_Table");

		HtmlElement l_warningParagraph = l_currentPage
				.getElementById("warningNoEntries");

		assertThat(
				l_warningParagraph.getTextContent().trim(),
				is(equalTo("The current worksheet is empty. Start filling it here.")));
		// WebAssert.assertTextPresent(l_currentPage,
		// "The current worksheet is empty. Start filling it here.");

		WebAssert.assertLinkPresent(l_currentPage, "AddEntry_Warn");
		WebAssert.assertLinkPresent(l_currentPage, "AddEntry");

		l_currentPage = ((HtmlAnchor) l_currentPage
				.getElementById("AddEntry_Warn")).click();

		assertThat("Are we starting in the add entry page",
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"),
				equalTo(DBEXEL_PAGES.AddNewEntry.toString()));

		// 6. Make reiterations
		// 7. Test Ramp-Down

	}

	// We should be able to see an entry table with entries that have a value
	// for this attribute
	@Test
	public void showEntriesForAttributeDetails()
			throws FailingHttpStatusCodeException, IOException {

		// 1. Test Ramp-Up
		f_ws = DBExelTestTools
				.fetchTestWorkSheet("Worksheet Attribute Entries");

		int l_testAttrIdx = (f_ws.getAttributes().size() - 1) > 0 ? (f_ws
				.getAttributes().size() - 2) : 0;

		f_ws.addEntry(new Entry());
		Entry l_entry = f_ws.getEntries().get(
				f_ws.getEntries().size() - 1);

		for (int i = 0; i < f_ws.getAttributes().size(); i++) {
			if (i != l_testAttrIdx) {
				l_entry.attachAttributeValue(new AttributeValue(f_ws
						.getAttributes().get(i), "NewVal " + i));
			}
		}

		WorkSheetDao l_wsDao = new WorkSheetDaoImpl();
		l_wsDao.createWorkSheet(f_ws);

		startPageUrl = new URL(FileUtils.fetchProperty("dbexel.testsite")
				+ "/DBEXEL/ShowDistinctWorkSheet.action?workSheet_Id="
				+ f_ws.getWs_Id());

		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(startPageUrl);

		// l_currentPage.get
		// 2. Validate Starting Point
		assertThat("Are we starting in the right page?",
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"),
				equalTo(DBEXEL_PAGES.WorkSheetDetails.toString()));

		// 3. Take References
		int l_lastDataColumnIdx = DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(0)
				.getCells().size() - 2;

		WebAssert.assertLinkPresent(l_currentPage, "attributeDetails_"
				+ l_lastDataColumnIdx);

		HtmlTable l_currentTable = DBExelHtmlUnitSupport.getTableById(
				l_currentPage, "Entry_Table");

		int l_nrOfRows = l_currentTable.getRowCount();

		int l_testCellIdx = l_lastDataColumnIdx > 0 ? (l_lastDataColumnIdx - 1)
				: 0;

		int l_nrOfAttributeRows = 0;
		for (HtmlTableRow lt_currentRow : l_currentTable.getRows()) {
			String lt_examinedCell = lt_currentRow.getCell(l_testCellIdx)
					.getTextContent();

			if (!lt_examinedCell.isEmpty()) {
				l_nrOfAttributeRows++;
			}
		}

		assertThat(l_nrOfAttributeRows, is(lessThan(l_nrOfRows)));
		assertThat(l_nrOfAttributeRows, is(greaterThan(0)));

		// 4. Initiate Tested Action

		l_currentPage = ((HtmlAnchor) l_currentPage
				.getElementById("attributeDetails_" + l_testCellIdx)).click();

		// 5. Make Assertions
		assertThat("We should now be in the Attribute Detail Page",
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"),
				is(equalTo(DBEXEL_PAGES.AttributeDetails.toString())));

		WebAssert.assertElementPresent(l_currentPage, "Entry_Table");

		l_currentTable = DBExelHtmlUnitSupport.getTableById(l_currentPage,
				"Entry_Table");

		assertThat(l_currentTable.getRowCount(),
				is(equalTo(l_nrOfAttributeRows)));

		// 6. Make reiterations
		// 7. Test Ramp-Down

	}

	// This is be
	@Test
	public void showEntriesForAttributeDetails_headerProblem()
			throws FailingHttpStatusCodeException, IOException {

		// 1. Test Ramp-Up
		f_ws = DBExelTestTools
				.fetchTestWorkSheet("Worksheet Attribute Entries");

		WorkSheetDao l_wsDao = new WorkSheetDaoImpl();
		l_wsDao.createWorkSheet(f_ws);

		startPageUrl = new URL(FileUtils.fetchProperty("dbexel.testsite")
				+ "/DBEXEL/ShowDistinctWorkSheet.action?workSheet_Id="
				+ f_ws.getWs_Id());

		HtmlPage l_currentPage = (HtmlPage) webClient.getPage(startPageUrl);

		// l_currentPage.get
		// 2. Validate Starting Point
		assertThat("Are we starting in the right page?",
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"),
				equalTo(DBEXEL_PAGES.WorkSheetDetails.toString()));

		// 3. Take References
		int l_lastDataColumnIdx = DBExelHtmlUnitSupport
				.getTableById(l_currentPage, "Entry_Table").getRow(0)
				.getCells().size() - 2;

		WebAssert.assertLinkPresent(l_currentPage, "attributeDetails_"
				+ l_lastDataColumnIdx);

		HtmlTable l_currentTable = DBExelHtmlUnitSupport.getTableById(
				l_currentPage, "Entry_Table");

		int l_nrOfRows = l_currentTable.getRowCount();

		int l_testCellIdx = l_lastDataColumnIdx > 0 ? (l_lastDataColumnIdx - 1)
				: 0;

		String l_testCellValue = l_currentTable.getRow(0)
				.getCell(l_testCellIdx).getTextContent();

		int l_nrOfAttributeRows = l_nrOfRows;

		// 4. Initiate Tested Action

		l_currentPage = ((HtmlAnchor) l_currentPage
				.getElementById("attributeDetails_" + l_testCellIdx)).click();

		// 5. Make Assertions
		assertThat("We should now be in the Attribute Detail Page",
				DBExelHtmlUnitSupport.getMetaContentByName(l_currentPage,
						"pageName"),
				is(equalTo(DBEXEL_PAGES.AttributeDetails.toString())));

		WebAssert.assertElementPresent(l_currentPage, "Entry_Table");

		l_currentTable = DBExelHtmlUnitSupport.getTableById(l_currentPage,
				"Entry_Table");

		assertThat(l_currentTable.getRowCount(),
				is(equalTo(l_nrOfAttributeRows)));

		assertThat(l_currentTable.getRow(0).getCells().size(), is(equalTo(f_ws
				.getAttributes().size())));

		assertThat(l_currentTable.getRow(0).getCell(l_testCellIdx)
				.getTextContent(), is(equalTo(l_testCellValue)));
		// 6. Make reiterations
		// 7. Test Ramp-Down

	}
}
