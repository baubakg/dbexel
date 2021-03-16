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
package dbexel.presentation.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import dbexel.model.dao.WorkSheetDao;
import dbexel.model.dao.WorkSheetDaoImpl;
import dbexel.model.mapping.AttributeValue;
import dbexel.model.mapping.WorkSheet;
import dbexel.model.mapping.Attribute;
import dbexel.model.mapping.Entry;
import dbexel.system.tools.DBExelTools;

public class InitWorkSheet extends ActionSupport implements SessionAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String workSheetName = "";
	private String sentence = "";
	private List<String> attributeValues = new ArrayList<String>();
	private List<String> attributeNames = new ArrayList<String>();
	private List<Boolean> keepColumns = new ArrayList<Boolean>();
	private List<String> columnTypes = new ArrayList<String>();
	private List<Boolean> errorNumberColumns = new ArrayList<Boolean>();

	private Map<String, Object> session;

	/*
	 * BGA private Long workSheet_Id;
	 */
	private WorkSheetDao workSheetDao;
	private WorkSheet newWorkSheet = null;

	// This method divides the given sentence into an array based on the spaces
	// in between
	protected List<String> slashSentence() {

		return Arrays.asList(getSentence().split(" "));
	}

	// This is the action method
	public String prepareNewWorkSheet() {
		// slash the sentence
		setAttributeValues(slashSentence());

		// fill the values for the Column types
		for (String lt_currentAttributeValue : getAttributeValues()) {
			if (DBExelTools.identifyType(lt_currentAttributeValue).equals(
					AttributeTypes.Number)) {
				getColumnTypes().add(AttributeTypes.Number.toString());
			} else {
				getColumnTypes().add(AttributeTypes.Text.toString());
			}
		}

		return ActionSupport.SUCCESS;
	}

	/**
	 * This method applies the cell actions to the attribue value and names
	 */
	public String performCellActions() {

		// create the worksheet
		/*
		 * BGA WorkSheet l_ws = new WorkSheet(getWorkSheetName());
		 */
		setNewWorkSheet(new WorkSheet(getWorkSheetName()));

		getNewWorkSheet().addEntry(new Entry());

		Attribute lt_attr = null;
		AttributeValue lt_av = null;

		for (int i = 0; i < getKeepColumns().size(); i++) {
			// Fetch the action of the current index
			if (getKeepColumns().get(i)) {
				// Get attribute
				lt_attr = new Attribute(getAttributeNames().get(i));
				lt_attr.setType(AttributeTypes.valueOf(getColumnTypes().get(i)));

				// Attach attribute to the worksheet
				getNewWorkSheet().attachAttribute(lt_attr);

				// Create attribute value
				lt_av = new AttributeValue(lt_attr, getAttributeValues().get(i));

				// attach attribute value to entry
				getNewWorkSheet().getEntries().get(0)
						.attachAttributeValue(lt_av);
			}
		}

		// Store the worksheet
		getWorkSheetDao().createWorkSheet(getNewWorkSheet());
		setNewWorkSheet(getNewWorkSheet());

		session.put(AwareKeys.New_WorkSheet_ID, getNewWorkSheet().getWs_Id());

		return ActionSupport.SUCCESS;
	}

	public void validate() {
		if (getAttributeValues().size() > 0) {
			if (getWorkSheetName().length() == 0) {
				addFieldError("workSheetName",
						"The worksheet name should not be left empty");
			}

			for (int i = 0; i < getColumnTypes().size(); i++) {
				// We do not perform validation on columns that are to be
				// ignored.
				if (getKeepColumns().get(i) == true) {
					if (getColumnTypes().get(i).equals(
							AttributeTypes.Number.toString())) {
						String lt_currentValue = getAttributeValues().get(i);

						if (DBExelTools.parseStringToDouble(lt_currentValue) == null) {
							getErrorNumberColumns().add(Boolean.TRUE);

							addFieldError("AVError_" + i,
									"The attribute value in column nr "
											+ (i + 1) + " was not a number");
						} else {
							getErrorNumberColumns().add(Boolean.FALSE);
						}
					} else {
						getErrorNumberColumns().add(Boolean.FALSE);
					}
				} else {
					getErrorNumberColumns().add(Boolean.FALSE);
				}
			}
		}
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getWorkSheetName() {
		return workSheetName;
	}

	public void setWorkSheetName(String workSheetName) {
		this.workSheetName = workSheetName;
	}

	/**
	 * @return the attributeName
	 */
	public List<String> getAttributeNames() {
		return attributeNames;
	}

	/**
	 * @param in_attributeName
	 *            the attributeName to set
	 */
	public void setAttributeNames(List<String> in_attributeName) {
		attributeNames = in_attributeName;
	}

	/**
	 * @return the cellAction
	 */
	public List<Boolean> getKeepColumns() {
		return keepColumns;
	}

	/**
	 * @param in_cellAction
	 *            the cellAction to set
	 */
	public void setKeepColumns(List<Boolean> in_keepColumns) {
		keepColumns = in_keepColumns;
	}

	/**
	 * @param attributeValues
	 *            the attributeValues to set
	 */
	public void setAttributeValues(List<String> attributeValues) {
		this.attributeValues = attributeValues;
	}

	/**
	 * @return the attributeValues
	 */
	public List<String> getAttributeValues() {
		return attributeValues;
	}

	// returns the list of possible attribute types
	public List<String> getAttributeTypeOptions() {
		return AttributeTypes.stringValues();
	}

	/**
	 * @return the workSheetDao
	 */
	public WorkSheetDao getWorkSheetDao() {
		if (workSheetDao == null)
			setWorkSheetDao(new WorkSheetDaoImpl());

		return workSheetDao;
	}

	/**
	 * @param workSheetDao
	 *            the workSheetDao to set
	 */
	public void setWorkSheetDao(WorkSheetDao workSheetDao) {
		this.workSheetDao = workSheetDao;
	}

	/**
	 * @return the columnTypes
	 */
	public List<String> getColumnTypes() {
		return columnTypes;
	}

	/**
	 * @param columnTypes
	 *            the columnTypes to set
	 */
	public void setColumnTypes(List<String> columnTypes) {
		this.columnTypes = columnTypes;
	}

	/**
	 * @return the newWorkSheet
	 */
	protected WorkSheet getNewWorkSheet() {
		return newWorkSheet;
	}

	/**
	 * @param newWorkSheet
	 *            the newWorkSheet to set
	 */
	private void setNewWorkSheet(WorkSheet newWorkSheet) {
		this.newWorkSheet = newWorkSheet;
	}

	public List<Boolean> getErrorNumberColumns() {
		return errorNumberColumns;
	}

	public void setErrorNumberColumns(List<Boolean> errorNumberColumns) {
		this.errorNumberColumns = errorNumberColumns;
	}

	@Override
	public void setSession(Map<String, Object> in_session) {
		session = in_session;

	}

}
