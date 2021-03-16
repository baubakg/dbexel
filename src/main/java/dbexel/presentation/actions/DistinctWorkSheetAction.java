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
import java.util.Collections;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import dbexel.model.dao.WorkSheetDao;
import dbexel.model.dao.WorkSheetDaoImpl;
import dbexel.model.mapping.WorkSheet;
import dbexel.model.mapping.Attribute;
import dbexel.system.tools.DBExelTools;

/**
 * @author baubak
 * 
 */
public class DistinctWorkSheetAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 498737995107494781L;

	private Long workSheet_Id;
	private WorkSheetDao workSheetDao;

	private String newWSName;
	private WorkSheet workSheet;
	private Boolean deletionAcknowledged = Boolean.FALSE;
	private Long selectedAttributeId;
	private int selectedIndex;

	/**
	 * This action method allows the editing of the work sheet
	 * 
	 * @return Input if we haven't filled in the necessary fields
	 */
	public String editWorkSheet() {
		// fetch worksheet
		setWorkSheet(getWorkSheetDao().fetchWorkSheet(getWorkSheet_Id()));

		if (getNewWSName() == null) {
			// parse attributes
			for (Attribute lt_attr : getWorkSheet().getAttributes()) {
				AttributeTypes l_proposedType = DBExelTools
						.identifyType(lt_attr.fetchStringValues());

				if (!l_proposedType.equals(lt_attr.getType())) {
					addActionMessage(getText("attributeType.validation.hint1",
							new String[] { lt_attr.getAttrName(),
									l_proposedType.toString(),
									lt_attr.getType().toString() }));
				}

			}

			return ActionSupport.INPUT;
		}

		// edit worksheet
		if (getNewWSName() != getWorkSheet().getWs_Name()) {
			getWorkSheet().setWs_Name(getNewWSName());

			// commit changes
			getWorkSheetDao().updateWorkSheet(getWorkSheet());
		}
		return ActionSupport.SUCCESS;
	}

	// This method moved the attributes to the right
	public String moveAttributeLeft() {

		// Fetch work sheet
		setWorkSheet(getWorkSheetDao().fetchWorkSheet(getWorkSheet_Id()));

		// move attribute to the left
		Collections.swap(getWorkSheet().getAttributes(), getSelectedIndex(),
				getSelectedIndex() - 1);

		getWorkSheetDao().updateWorkSheet(getWorkSheet());
		return ActionSupport.INPUT;
	}

	public String moveAttributeRight() {
		// Fetch work sheet
		setWorkSheet(getWorkSheetDao().fetchWorkSheet(getWorkSheet_Id()));

		// move attribute to the left
		Collections.swap(getWorkSheet().getAttributes(), getSelectedIndex(),
				getSelectedIndex() + 1);

		getWorkSheetDao().updateWorkSheet(getWorkSheet());
		return ActionSupport.INPUT;

	}

	/**
	 * this method deletes the given attribute
	 * 
	 * @return
	 */
	public String removeAttribute() {
		// fetch worksheet
		setWorkSheet(getWorkSheetDao().fetchWorkSheet(getWorkSheet_Id()));

		getWorkSheet().removeAttribute(
				getWorkSheet().findAttributeById(selectedAttributeId));

		getWorkSheetDao().updateWorkSheet(getWorkSheet());

		return ActionSupport.INPUT;
	}

	// Puts the worksheet in the context
	public String showWorkSheet() {
		setWorkSheet(getWorkSheetDao().fetchWorkSheet(getWorkSheet_Id()));

		return ActionSupport.SUCCESS;

	}

	// Deletes the given worksheet worksheet
	public String deleteWorkSheet() {
		setWorkSheet(getWorkSheetDao().fetchWorkSheet(getWorkSheet_Id()));

		getWorkSheetDao().deleteWorkSheet(getWorkSheet());

		return ActionSupport.SUCCESS;

	}

	public Long getWorkSheet_Id() {
		return workSheet_Id;
	}

	// public void setWorkSheet_Id(long workSheet_Id) {
	public void setWorkSheet_Id(Long workSheet_Id) {
		this.workSheet_Id = workSheet_Id;
	}

	public WorkSheetDao getWorkSheetDao() {
		if (workSheetDao == null)
			setWorkSheetDao(new WorkSheetDaoImpl());

		return workSheetDao;
	}

	public void setWorkSheetDao(WorkSheetDao workSheetDao) {
		this.workSheetDao = workSheetDao;
	}

	public String getNewWSName() {
		return newWSName;
	}

	public void setNewWSName(String newName) {
		this.newWSName = newName;
	}

	/**
	 * @return the workSheet
	 */
	public WorkSheet getWorkSheet() {

		return workSheet;
	}

	/**
	 * @param workSheet
	 *            the workSheet to set
	 */
	public void setWorkSheet(WorkSheet workSheet) {
		this.workSheet = workSheet;
	}

	/**
	 * @return the deleteAcknowledged
	 */
	public Boolean isDeleteAcknowledged() {
		return deletionAcknowledged;
	}

	/**
	 * @param in_bool
	 *            the deleteAcknowledged to set
	 */
	public void setDeleteAcknowledged(Boolean in_bool) {
		this.deletionAcknowledged = in_bool;
	}

	/**
	 * @return the selectedAttributeId
	 */
	public Long getSelectedAttributeId() {
		return selectedAttributeId;
	}

	/**
	 * @param selectedAttributeId
	 *            the selectedAttributeId to set
	 */
	public void setSelectedAttributeId(Long selectedAttributeId) {
		this.selectedAttributeId = selectedAttributeId;
	}

	/**
	 * @return the selectedIndex
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}

	/**
	 * @param selectedIndex
	 *            the selectedIndex to set
	 */
	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

}
