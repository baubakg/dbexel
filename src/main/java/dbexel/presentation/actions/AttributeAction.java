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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import dbexel.model.dao.AttributeDao;
import dbexel.model.dao.AttributeDaoImpl;
import dbexel.model.mapping.AttributeValue;
import dbexel.model.mapping.Attribute;
import dbexel.model.mapping.Entry;
import dbexel.system.tools.DBExelTools;

public class AttributeAction extends ActionSupport implements Preparable {

	private static final long serialVersionUID = 1L;

	private Long attributeId;
	private Long workSheet_Id;

	private String attributeName = "";
	private String attributeType;
	private String attributeDescription = "";

	private AttributeDao attributeDao;
	private Attribute attribute;

	private Set<Entry> entriesPresent = new HashSet<Entry>();

	@Override
	public void prepare() throws Exception {
		setAttribute(getAttributeDao().fetchAttribute(getAttributeId()));

		setEntriesPresent(getAttribute().fetchEntriesUsingThis());
	}

	public String execute() {

		if (getAttributeName().isEmpty()) {

			setAttributeName(getAttribute().getAttrName());
			setAttributeDescription(getAttribute().getDescription());
			return ActionSupport.INPUT;
		}

		boolean l_updateNeeded = false;

		// Update attribute name
		if (!getAttributeName().equals(getAttribute().getAttrName())) {
			getAttribute().setAttrName(getAttributeName());
			l_updateNeeded = true;
		}

		if (!getAttributeType().equals(getAttribute().getType())) {
			getAttribute().setType(AttributeTypes.valueOf(getAttributeType()));
			l_updateNeeded = true;
		}

		if (!getAttributeDescription().equals(getAttribute().getDescription())) {
			getAttribute().setDescription(getAttributeDescription());
			l_updateNeeded = true;
		}

		if (l_updateNeeded)
			getAttributeDao().updateAttribute(getAttribute());

		return ActionSupport.SUCCESS;
	}

	public void validate() {
		if (!getAttributeName().isEmpty()) {
			if (getAttributeType().equals(AttributeTypes.Number.toString())) {
				int i = 0;
				int i_max = getAttribute().getAttrValues().size();
				// parse the attribute values
				while (!getFieldErrors().containsKey("attributeType")
						&& (i < i_max)) {
					AttributeValue lt_attrValue = getAttribute()
							.getAttrValues().get(i);
					if (DBExelTools
							.parseStringToDouble(lt_attrValue.getValue()) == null) {
						addFieldError("attributeType",
								getText("attributeType.validation.error1"));
					}
					i++;
				}
			}

			for (Attribute lt_attr : getAttribute().getWorkSheet()
					.getAttributes()) {
				if (lt_attr.getAttrName().equals(getAttributeName())
						&& !lt_attr.getAttrId().equals(
								getAttribute().getAttrId())) {
					addFieldError("attributeName",
							getText("attributeName.validation.error1"));
				}
			}
		} else {
			if (getAttribute().getType().equals(AttributeTypes.Text)) {

				List<AttributeTypes> l_attrTypes = new ArrayList<AttributeTypes>();

				for (String lt_attrValueString : getAttribute()
						.fetchStringValues()) {
					l_attrTypes.add(DBExelTools
							.identifyType(lt_attrValueString));
				}

				if (!l_attrTypes.contains(AttributeTypes.Text)) {
					addActionMessage(getText(
							"attributeType.validation.hint1",
							new String[] { DBExelTools.identifyType(
									getAttribute().fetchStringValues())
									.toString() }));
				}
			}
		}
	}

	// returns the list of possible attribute types
	public List<String> getAttributeTypeOptions() {
		return AttributeTypes.stringValues();
	}

	/**
	 * @return the attribute_Name
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @param attribute_Name
	 *            the attribute_Name to set
	 */
	public void setAttributeName(String attribute_Name) {
		this.attributeName = attribute_Name;
	}

	/**
	 * @return the workSheet_Id
	 */
	public Long getWorkSheet_Id() {
		return workSheet_Id;
	}

	/**
	 * @param workSheet_Id
	 *            the workSheet_Id to set
	 */
	public void setWorkSheet_Id(Long workSheet_Id) {
		this.workSheet_Id = workSheet_Id;
	}

	/**
	 * @return the attribute_Id
	 */
	public Long getAttributeId() {
		return attributeId;
	}

	/**
	 * @param attribute_Id
	 *            the attribute_Id to set
	 */
	public void setAttributeId(Long attribute_Id) {
		this.attributeId = attribute_Id;
	}

	/**
	 * @return the attributeDao
	 */
	public AttributeDao getAttributeDao() {
		if (attributeDao == null)
			setAttributeDao(new AttributeDaoImpl());

		return attributeDao;
	}

	/**
	 * @param attributeDao
	 *            the attributeDao to set
	 */
	public void setAttributeDao(AttributeDao attributeDao) {
		this.attributeDao = attributeDao;
	}

	/**
	 * @return the attribute
	 */
	public Attribute getAttribute() {
		return attribute;
	}

	/**
	 * @param attribute
	 *            the attribute to set
	 */
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	/**
	 * @return the entriesPresent
	 */
	public Set<Entry> getEntriesPresent() {
		return entriesPresent;
	}

	/**
	 * @param entriesPresent
	 *            the entriesPresent to set
	 */
	public void setEntriesPresent(Set<Entry> entriesPresent) {
		this.entriesPresent = entriesPresent;
	}

	/**
	 * @return the attributeType
	 */
	public String getAttributeType() {
		return attributeType;
	}

	/**
	 * @param attributeType
	 *            the attributeType to set
	 */
	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	/**
	 * @return the attributeDesc
	 */
	public String getAttributeDescription() {
		return attributeDescription;
	}

	/**
	 * @param attributeDesc
	 *            the attributeDesc to set
	 */
	public void setAttributeDescription(String attributeDesc) {
		this.attributeDescription = attributeDesc;
	}
}
