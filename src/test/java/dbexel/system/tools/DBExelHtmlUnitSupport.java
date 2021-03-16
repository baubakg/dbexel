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

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlMeta;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import dbexel.system.DBEXEL_PAGES;

/**
 * @author gandomi
 * 
 */
public class DBExelHtmlUnitSupport {
	private final static String ATTRIBUTE_ID = "id";
	private final static String ATTRIBUTE_NAME = "name";

	/**
	 * This method fetches all the visible input fields
	 * 
	 * @param in_htmlForm
	 *            the current HTMLPage
	 * @return a list of visible "<input type="text" fields
	 */
	public static List<HtmlInput> fetchVisibleInputFields(
			final HtmlForm in_htmlForm) {
		List<HtmlInput> lr_returnList = new ArrayList<HtmlInput>();

		for (HtmlElement lt_HtmlInput : in_htmlForm
				.getElementsByTagName("input")) {

			if (lt_HtmlInput instanceof HtmlTextInput) {
				lr_returnList.add((HtmlInput) lt_HtmlInput);

			}
		}
		// TODO Auto-generated method stub
		return lr_returnList;
	}

	/**
	 * This method returns all Input fields that start with a specific string
	 * 
	 * @param in_htmlForm
	 *            The HTMLForm containing input fields
	 * @param in_nameString
	 *            The prefix to look for
	 * @return
	 */
	public static List<HtmlInput> getInputsByNameStart(HtmlForm in_htmlForm,
			String in_nameString) {
		return getInputsByAttributeStartingWith(in_htmlForm, ATTRIBUTE_NAME,
				in_nameString);
	}

	/**
	 * This method returns all Input fields that start with a specific string
	 * 
	 * @param in_htmlForm
	 *            The HTMLForm containing input fields
	 * @param in_nameStart
	 *            The prefix to look for
	 * @param in_nameEnd
	 *            The suffix to look for
	 * 
	 * @return
	 */
	public static List<HtmlInput> getInputsByNameStEnd(HtmlForm in_htmlForm,
			String in_nameStart, String in_nameEnd) {
		return getInputsByAttributeStEndingWith(in_htmlForm, ATTRIBUTE_NAME,
				in_nameStart, in_nameEnd);
	}

	/**
	 * This method returns all Input fields that start with a specific string
	 * 
	 * @param in_htmlForm
	 *            The HTMLForm containing input fields
	 * @param in_nameStart
	 *            The prefix to look for
	 * @param in_nameEnd
	 *            The suffix to look for
	 * 
	 * @return
	 */
	public static List<HtmlInput> getInputsByIdStEnd(HtmlForm in_htmlForm,
			String in_nameStart, String in_nameEnd) {
		return getInputsByAttributeStEndingWith(in_htmlForm, ATTRIBUTE_ID,
				in_nameStart, in_nameEnd);
	}

	/**
	 * This method returns all Input fields that start with a specific string
	 * 
	 * @param in_htmlForm
	 *            The HTMLForm containing input fields
	 * @param in_nameString
	 *            The prefix to look for
	 * @return
	 */
	public static List<HtmlInput> getInputsByIdStart(HtmlForm in_htmlForm,
			String in_nameString) {

		return getInputsByAttributeStartingWith(in_htmlForm, ATTRIBUTE_ID,
				in_nameString);
	}

	/**
	 * This method returns all Input fields that start with a specific string
	 * 
	 * @param in_htmlForm
	 *            The HTMLForm containing input fields
	 * @param in_AttributeType
	 *            this is the type of the attribute to look for. For now we only
	 *            have "name" and "input
	 * @param in_nameString
	 *            The prefix to look for
	 * @return
	 */
	private static List<HtmlInput> getInputsByAttributeStartingWith(
			HtmlForm in_htmlForm, String in_AttributeType, String in_nameString) {
		List<HtmlInput> lr_returnList = new ArrayList<HtmlInput>();

		for (HtmlElement lt_htmlInput : in_htmlForm
				.getElementsByTagName("input")) {

			if (lt_htmlInput.getAttribute(in_AttributeType).startsWith(
					in_nameString)) {
				lr_returnList.add((HtmlInput) lt_htmlInput);
			}
		}

		return lr_returnList;
	}

	/**
	 * This method checks for inputs with a given start and finished string
	 * 
	 * @param in_htmlForm
	 * @param in_AttributeType
	 * @param in_nameStart
	 * @param in_nameEnd
	 * @return
	 */
	private static List<HtmlInput> getInputsByAttributeStEndingWith(
			HtmlForm in_htmlForm, String in_AttributeType, String in_nameStart,
			String in_nameEnd) {
		List<HtmlInput> lr_returnList = new ArrayList<HtmlInput>();

		for (HtmlElement lt_htmlInput : in_htmlForm
				.getElementsByTagName("input")) {

			if (lt_htmlInput.getAttribute(in_AttributeType).startsWith(
					in_nameStart)
					&& lt_htmlInput.getAttribute(in_AttributeType).endsWith(
							in_nameEnd)) {
				lr_returnList.add((HtmlInput) lt_htmlInput);
			}
		}

		return lr_returnList;
	}

	/**
	 * This method returns all Select fields that start with a specific string
	 * 
	 * @param in_htmlForm
	 *            The HTMLForm containing input fields
	 * @param in_AttributeType
	 *            this is the type of the attribute to look for. For now we only
	 *            have "name" and "input
	 * @param in_nameStart
	 *            The prefix to look for
	 * @return
	 */
	private static List<HtmlSelect> getSelectsByAttributeStEndingWith(
			HtmlForm in_htmlForm, String in_AttributeType, String in_nameStart,
			String in_nameEnd) {
		List<HtmlSelect> lr_returnList = new ArrayList<HtmlSelect>();

		for (HtmlElement lt_htmlSelect : in_htmlForm
				.getElementsByTagName("select")) {

			if (lt_htmlSelect.getAttribute(in_AttributeType).startsWith(
					in_nameStart)) {
				lr_returnList.add((HtmlSelect) lt_htmlSelect);
			}
		}

		return lr_returnList;
	}

	/**
	 * This method returns all Select fields that start with a specific string
	 * for the name
	 * 
	 * @param in_htmlForm
	 * @param in_nameString
	 * @return
	 */
	public static List<HtmlSelect> getSelectsByNameStart(HtmlForm in_htmlForm,
			String in_nameString) {
		return getSelectsByAttributeStEndingWith(in_htmlForm, ATTRIBUTE_NAME,
				in_nameString, "");
	}

	/**
	 * This method returns all Select fields that start with a specific string
	 * for the Id
	 * 
	 * @param in_htmlForm
	 * @param in_nameString
	 * @return
	 */
	public static List<HtmlSelect> getSelectsByIdStart(HtmlForm in_htmlForm,
			String in_nameString) {
		return getSelectsByAttributeStEndingWith(in_htmlForm, ATTRIBUTE_ID,
				in_nameString, "");
	}

	/**
	 * This method returns a select given it's Id
	 * 
	 * @param in_form
	 * @param in_idForSelect
	 * @return
	 */
	public static HtmlSelect getSelectById(HtmlForm in_form,
			String in_idForSelect) {
		if (!in_form.hasHtmlElementWithId(in_idForSelect)) {
			throw new ElementNotFoundException("select", "id", in_idForSelect);
		}
		return (HtmlSelect) in_form.getElementById(in_idForSelect);
	}

	/**
	 * This method returns the submit button of a form. If more than one are
	 * found we return the first submit button
	 * 
	 * @param in_htmlForm
	 * @return a Submit button
	 */
	public static HtmlSubmitInput getSubmitButton(HtmlForm in_htmlForm) {

		// Parse all form input elements
		for (HtmlElement lt_htmlElement : in_htmlForm
				.getElementsByTagName("input")) {

			if (((HtmlInput) lt_htmlElement).getTypeAttribute()
					.equals("submit")) {
				return (HtmlSubmitInput) lt_htmlElement;
			}
		}

		throw new ElementNotFoundException("submit", "input", "");

	}

	/**
	 * This method retrieves a table from a page given it's id. If the id is not
	 * found ElementNotFoundException is thrown
	 * 
	 * @param in_currentPage
	 * 
	 * @param in_id
	 * @return
	 */
	public static HtmlTable getTableById(HtmlPage in_currentPage, String in_id) {
		HtmlTable lr_table = (HtmlTable) in_currentPage.getElementById(in_id);
		if (lr_table == null)
			throw new ElementNotFoundException("Table", "id", in_id);

		return lr_table;
	}

	/**
	 * This method returns a table, depending on its occurence in the given
	 * page. This is decided by the in_order parameter. If the given occurence
	 * is non-existent we throw an ElementNotFoundException
	 * 
	 * @param in_currentPage
	 * @param in_order
	 *            The occurence of the table in the given page
	 * @return the table
	 */
	public static HtmlTable getTableByOrder(HtmlPage in_currentPage,
			int in_order) {
		HtmlTable lr_table = null;

		try {
			lr_table = (HtmlTable) in_currentPage.getElementsByTagName("table")
					.get(in_order);
		} catch (IndexOutOfBoundsException iobe) {
			throw new ElementNotFoundException("Table", "order",
					Integer.toString(in_order));
		}

		return lr_table;
	}

	/**
	 * This method returns the first table in a page. If no tables exist in the
	 * page we throw an ElementNotFoundException
	 * 
	 * @param in_currentPage
	 * @return the table
	 */
	public static HtmlTable getTableByOrder(HtmlPage in_currentPage) {

		return getTableByOrder(in_currentPage, 0);
	}

	/**
	 * Returns the anchor of a page given a its id
	 * 
	 * @param in_page
	 * @param in_idString
	 * @return
	 */
	public static HtmlAnchor getAnchorById(HtmlPage in_page, String in_idString) {

		return in_page.getDocumentElement().getOneHtmlElementByAttribute("a",
				"id", in_idString);
	}

	/**
	 * This method returns the index of the last row in a table
	 * 
	 * @param in_currentTable
	 * @return
	 */
	public static int getLastRowIdx(HtmlTable in_currentTable) {
		return in_currentTable.getRowCount() - 1;

	}

	/**
	 * This method returns the index of the last column in a table. This method
	 * returns the number of columns in the first row
	 * 
	 * @param in_currentTable
	 * @return
	 */
	public static int getLastColumnIdx(HtmlTable in_currentTable) {

		return getLastColumnIdx(in_currentTable, 0);
	}

	/**
	 * This method returns the index of the last column in a table
	 * 
	 * @param in_table
	 * @param in_rowIdx
	 * @return
	 */
	public static int getLastColumnIdx(HtmlTable in_table, int in_rowIdx) {

		return in_table.getRow(in_rowIdx).getCells().size() - 1;
	}

	public static int getLastColumnIdx(HtmlTable in_table, String in_rowId) {
		try {
			return in_table.getRowById(in_rowId).getCells().size() - 1;
		} catch (ElementNotFoundException enf) {
			return getLastColumnIdx(in_table);

		}
	}

	/**
	 * This method returns the last cell in the given row in the given table
	 * 
	 * @param in_table
	 * @param in_rowIdx
	 * @return
	 */
	public static HtmlTableCell getLastCell(HtmlTable in_table, int in_rowIdx) {

		return in_table.getRow(in_rowIdx).getCell(
				getLastColumnIdx(in_table, in_rowIdx));
	}

	/**
	 * This method returns the last cell in the given row in the given table,
	 * given the id of the row. If not rows have id's it is the very first row
	 * that is returned
	 * 
	 * @param in_table
	 * @param in_rowId
	 * @return
	 */
	public static HtmlTableCell getLastCell(HtmlTable in_table, String in_rowId) {
		try {
			HtmlTableRow l_tableRow = in_table.getRowById(in_rowId);
			return l_tableRow.getCell(l_tableRow.getCells().size() - 1);
		} catch (ElementNotFoundException enf) {
			return getLastCell(in_table, 0);
		}
	}

	/**
	 * This method treats a table as a matrix of rows and cells. The index
	 * starts at 0
	 * 
	 * @param in_table
	 * @param in_rowIdx
	 * @param in_cellIdx
	 * @return
	 */
	public static DomElement getCellByCoordinates(HtmlTable in_table,
			int in_rowIdx, int in_cellIdx) {

		// Controls
		// Check rows
		if (in_table.getRowCount() <= in_rowIdx) {
			throw new ElementNotFoundException("Row", "Nr",
					Integer.toString(in_rowIdx));
		}

		if (in_table.getRow(in_rowIdx).getCells().size() <= in_cellIdx) {
			throw new ElementNotFoundException("Cell", "Nr",
					Integer.toString(in_cellIdx));
		}

		return in_table.getRow(in_rowIdx).getCell(in_cellIdx);
	}

	/**
	 * This method returns the meta data in the page
	 * 
	 * @param in_page
	 * @return
	 */
	private static List<HtmlMeta> fetchMetaData(HtmlPage in_page) {
		List<HtmlMeta> lr_metaElements = new ArrayList<HtmlMeta>();

		for (HtmlElement lt_element : in_page.getElementsByTagName("meta")) {
			lr_metaElements.add((HtmlMeta) lt_element);
		}

		return lr_metaElements;

	}

	/**
	 * The method returns the content of a meta tage given its name
	 * 
	 * @param in_page
	 * @param in_name
	 * @return
	 */
	public static String getMetaContentByName(HtmlPage in_page, String in_name) {
		for (HtmlMeta lt_meta : fetchMetaData(in_page)) {
			if (lt_meta.getNameAttribute().equals(in_name)) {
				return lt_meta.getContentAttribute();
			}
		}
		throw new ElementNotFoundException("meta", "name", in_name);
	}

	/**
	 * This method finds a specific meta information that is commonly used
	 * @param in_page
	 * @return the Page name
	 */
	public static DBEXEL_PAGES getMetaPageName(HtmlPage in_page) {
		String l_string = getMetaContentByName(in_page, "pageName");
		// TODO Auto-generated method stub
		return DBEXEL_PAGES.valueOf(l_string);
	}
}
