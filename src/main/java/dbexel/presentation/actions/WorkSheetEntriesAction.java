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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import dbexel.model.dao.WorkSheetDao;
import dbexel.model.dao.WorkSheetDaoImpl;
import dbexel.model.dao.WorkSheetEntryDao;
import dbexel.model.dao.WorkSheetEntryDaoImpl;
import dbexel.model.mapping.AttributeValue;
import dbexel.model.mapping.WorkSheet;
import dbexel.model.mapping.Attribute;
import dbexel.model.mapping.Entry;
import dbexel.system.tools.DBExelTools;

public class WorkSheetEntriesAction extends ActionSupport implements Preparable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long workSheet_Id;

	private WorkSheet workSheet;

	private Map<Long, String> newAttributeValues = new HashMap<Long, String>();

	private List<String> newAddedAttributes = new ArrayList<String>();
	private List<String> newAddedColumnTypes = new ArrayList<String>();
	private List<String> newAddedAttributeValues = new ArrayList<String>();

	private List<Boolean> errorNumberColumns = new ArrayList<Boolean>();

	private Long selectedEntryId;

	// private Boolean deletionAcknowledged;//=Boolean.FALSE;

	private WorkSheetDao workSheetDao;
	private WorkSheetEntryDao workSheetEntryDao;

	/**
	 * @return the workSheet
	 */
	public WorkSheet getWorkSheet() {
		return workSheet;
	}

	/**
	 * @param in_workSheet
	 *            the workSheet to set
	 */
	public void setWorkSheet(WorkSheet in_workSheet) {
		workSheet = in_workSheet;
	}

	public String showWorkSheet() {
		// setWorkSheet(getWorkSheetDao().fetchWorkSheet(getWorkSheet_Id()));

		return ActionSupport.SUCCESS;

	}

	@Override
	public void prepare() throws Exception {

		setWorkSheet(getWorkSheetDao().fetchWorkSheet(getWorkSheet_Id()));
	}

	public String create() {
		// setWorkSheet(getWorkSheetDao().fetchWorkSheet(getWorkSheet_Id()));

		// First create a new entry
		Entry l_newWSEntry = new Entry();
		if (getNewAttributeValues().isEmpty()) {
			return ActionSupport.INPUT;
		}

		getWorkSheet().addEntry(l_newWSEntry);

		// Fetch the newly added entries
		// Parse the attributes of the worksheet
		for (Attribute lt_wsAttribute : getWorkSheet().getAttributes()) {
			if (getNewAttributeValues().containsKey(lt_wsAttribute.getAttrId())) {
				AttributeValue lt_attrValue = new AttributeValue(
						lt_wsAttribute, getNewAttributeValues().get(
								lt_wsAttribute.getAttrId()));
				l_newWSEntry.attachAttributeValue(lt_attrValue);
			}
		}

		attachAttributeValues(l_newWSEntry);
		/*
		 * if (attachAttributeValues(l_newWSEntry) == ActionSupport.INPUT) {
		 * return ActionSupport.INPUT; }
		 */
		getWorkSheetDao().updateWorkSheet(getWorkSheet());

		return ActionSupport.SUCCESS;
	}

	public String delete() {

		Entry l_wsEntry = getWorkSheetEntryDao().fetchWorkSheetEntry(
				getSelectedEntryId());
		// setWorkSheet(l_wsEntry.getWorkSheet());

		getWorkSheetEntryDao().deleteWorkSheetEntry(l_wsEntry);

		return ActionSupport.SUCCESS;
	}

	public String update() {
		// setWorkSheet(getWorkSheetDao().fetchWorkSheet(getWorkSheet_Id()));

		if (getNewAttributeValues().isEmpty()) {

			return ActionSupport.INPUT;
		}

		Entry l_wsEntry = null;
		for (Entry lt_wsEntry : getWorkSheet().getEntries()) {
			if (lt_wsEntry.getEntryId().equals(getSelectedEntryId())) {
				l_wsEntry = lt_wsEntry;
			}
		}

		if (attachAttributeValues(l_wsEntry) == ActionSupport.INPUT) {
			return ActionSupport.INPUT;
		}

		l_wsEntry.updateValues(getNewAttributeValues());
		getWorkSheetDao().updateWorkSheet(getWorkSheet());
		return ActionSupport.SUCCESS;
	}

	// This method retrieves all fields for added attribute values and sets them
	// in the given entry
	private String attachAttributeValues(Entry in_wsEntry) {
		// Fetch the newly added attribute value
		for (int i = 0; i < getNewAddedAttributes().size(); i++) {
			String lt_attributeName = getNewAddedAttributes().get(i);

			/*
			 * removed because we no longer want validation outside the validate
			 * method if (getWorkSheet().findAttributeByName(lt_attributeName)
			 * != null) { return ActionSupport.INPUT; }
			 */

			if (!lt_attributeName.isEmpty()) {
				Attribute lt_attribute = new Attribute(lt_attributeName);

				getWorkSheet().attachAttribute(lt_attribute);

				AttributeValue lt_attributeValue = new AttributeValue(
						lt_attribute, getNewAddedAttributeValues().get(i));

				in_wsEntry.attachAttributeValue(lt_attributeValue);
			}
		}

		return null;
	}

	/**
	 * @param workSheet_Id
	 *            the workSheet_Id to set
	 */
	public void setWorkSheet_Id(Long workSheet_Id) {
		this.workSheet_Id = workSheet_Id;
	}

	/**
	 * @return the workSheet_Id
	 */
	public Long getWorkSheet_Id() {
		return workSheet_Id;
	}

	/**
	 * @param newAttributeValues
	 *            the newAttributeValues to set
	 */
	public void setNewAttributeValues(Map<Long, String> newAttributeValues) {
		this.newAttributeValues = newAttributeValues;
	}

	/**
	 * @return the newAttributeValues
	 */
	public Map<Long, String> getNewAttributeValues() {
		return newAttributeValues;
	}

	/**
	 * @param entryId
	 *            the entryId to set
	 */
	public void setSelectedEntryId(Long entryId) {
		this.selectedEntryId = entryId;
	}

	/**
	 * @return the entryId
	 */
	public Long getSelectedEntryId() {
		return selectedEntryId;
	}

	public void setWorkSheetDao(WorkSheetDao workSheetDao) {
		this.workSheetDao = workSheetDao;
	}

	public WorkSheetDao getWorkSheetDao() {
		if (workSheetDao == null)
			return new WorkSheetDaoImpl();
		return workSheetDao;
	}

	/**
	 * @param workSheetEntryDao
	 *            the workSheetEntryDao to set
	 */
	public void setWorkSheetEntryDao(WorkSheetEntryDao workSheetEntryDao) {
		this.workSheetEntryDao = workSheetEntryDao;
	}

	/**
	 * @return the workSheetEntryDao
	 */
	public WorkSheetEntryDao getWorkSheetEntryDao() {
		if (workSheetEntryDao == null) {
			return new WorkSheetEntryDaoImpl();
		}
		return workSheetEntryDao;
	}

	/**
	 * @return the newAttribute
	 */
	public List<String> getNewAddedAttributes() {
		return newAddedAttributes;
	}

	/**
	 * @param newAttribute
	 *            the newAttribute to set
	 */
	public void setNewAddedAttributes(List<String> newAttribute) {
		this.newAddedAttributes = newAttribute;
	}

	/**
	 * @return the newAttributeValue
	 */
	public List<String> getNewAddedAttributeValues() {
		return newAddedAttributeValues;
	}

	/**
	 * @param newAttributeValue
	 *            the newAttributeValue to set
	 */
	public void setNewAddedAttributeValues(List<String> newAttributeValue) {
		this.newAddedAttributeValues = newAttributeValue;
	}

	// returns the list of possible attribute types
	public List<String> getAttributeTypeOptions() {
		return AttributeTypes.stringValues();
	}

	/**
	 * @return the newAddedColumnTypes
	 */
	public List<String> getNewAddedColumnTypes() {
		return newAddedColumnTypes;
	}

	/**
	 * @param newAddedColumnTypes
	 *            the newAddedColumnTypes to set
	 */
	public void setNewAddedColumnTypes(List<String> newAddedColumnTypes) {
		this.newAddedColumnTypes = newAddedColumnTypes;
	}

	public void validate() {
		if (getNewAttributeValues().size() > 0) {

			int i = 0;

			// type validation
			for (Attribute lt_attribute : getWorkSheet().getAttributes()) {
				if (lt_attribute.getType().equals(AttributeTypes.Number)) {
					String lt_currentValue = getNewAttributeValues().get(
							lt_attribute.getAttrId());

					if (DBExelTools.parseStringToDouble(lt_currentValue) == null) {
						getErrorNumberColumns().add(Boolean.TRUE);

						addFieldError(
								"AVError_" + i,
								getText("entry.validation.error1",
										new String[] { lt_attribute
												.getAttrName() }));
					} else {
						getErrorNumberColumns().add(Boolean.FALSE);
					}
				} else {
					getErrorNumberColumns().add(Boolean.FALSE);
				}
				i++;
			}

			for (int j = 0; j < getNewAddedColumnTypes().size(); j++) {
				if (getNewAddedColumnTypes().get(j).equals(
						AttributeTypes.Number.toString())) {
					String lt_currentValue = getNewAddedAttributeValues()
							.get(j);

					if (DBExelTools.parseStringToDouble(lt_currentValue) == null) {
						getErrorNumberColumns().add(Boolean.TRUE);

						addFieldError(
								"AVError_" + (j + i),
								getText("entry.validation.error2",
										new String[] {
												getNewAddedAttributes().get(j),
												Integer.toString(j + i + 1) }));
					}
				} else {
					getErrorNumberColumns().add(Boolean.FALSE);
				}
			}

			for (String lt_attributeName : getNewAddedAttributes()) {

				if (getWorkSheet().findAttributeByName(lt_attributeName) != null) {
					addFieldError(
							"AVError_",
							getText("entry.validation.error3",
									new String[] { lt_attributeName }));
				}
			}
		}
	}

	/**
	 * @return the errorNumberColumns
	 */
	public List<Boolean> getErrorNumberColumns() {
		return errorNumberColumns;
	}

	/**
	 * @param errorNumberColumns
	 *            the errorNumberColumns to set
	 */
	public void setErrorNumberColumns(List<Boolean> errorNumberColumns) {
		this.errorNumberColumns = errorNumberColumns;
	}
}
