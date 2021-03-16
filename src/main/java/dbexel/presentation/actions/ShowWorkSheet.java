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
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

public class ShowWorkSheet {

	private String workSheetName;
	private List<String> attributeValues = new ArrayList<String>();
	private List<String> attributeNames = new ArrayList<String>();

	

	public String getWorkSheetName() {
		return workSheetName;
	}

	public void setWorkSheetName(String workSheetName) {
		this.workSheetName = workSheetName;
	}

	
	/**
	 * @param seperatedSentence the seperatedSentence to set
	 */
	public void setAttributeValues(List<String> in_attributeValues) {
		this.attributeValues = in_attributeValues;
	}

	/**
	 * @return the seperatedSentence
	 */
	public List<String> getAttributeValues() {
		return attributeValues;
	}

	//This is the action method
	public String showWorkSheetFields() {
		
		return ActionSupport.SUCCESS;
		
	}

	/**
	 * @param attributeName the attributeName to set
	 */
	public void setAttributeName(List<String> in_attributeName) {
		this.attributeNames = in_attributeName;
	}

	/**
	 * @return the attributeName
	 */
	public List<String> getAttributeNames() {
		return attributeNames;
	}

}
