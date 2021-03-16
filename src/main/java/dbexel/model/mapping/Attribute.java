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
package dbexel.model.mapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import dbexel.presentation.actions.AttributeTypes;

@Entity
@Table(name = "ATTRIBUTE")
public class Attribute {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ATTR_ID")
	private Long attrId;

	@Column(name = "ATTR_NAME")
	private String attrName;

	@OneToMany(mappedBy = "attribute")
	@ForeignKey(name = "FK_ATTRIBUTE_ATTRIBUTEVALUE")
	private List<AttributeValue> attrValues;

	@ManyToOne
	private WorkSheet workSheet;

	@Enumerated(EnumType.STRING)
	@Column(name="ATTR_TYPE", nullable=false)
	private AttributeTypes type = AttributeTypes.Text;

	@Column(name = "ATTR_DESC")
	private String description;

	
	public AttributeValue addAttrValue(AttributeValue in_attributeValue) {
		if (this.fetchAttributeValue(in_attributeValue.getValue())==null) {
			this.getAttrValues().add(in_attributeValue);
			in_attributeValue.setAttribute(this);
		}

		return this.fetchAttributeValue(in_attributeValue.getValue());
				
	}

	public Attribute() {
		attrName = "";
		setAttrValues(new ArrayList<AttributeValue>());
	}

	public Attribute(String in_attrName) {
		attrName = in_attrName;
		setAttrValues(new ArrayList<AttributeValue>());
	}

	public Long getAttrId() {
		return attrId;
	}

	public void setAttrId(Long in_attrId) {
		attrId = in_attrId;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String in_attrName) {
		attrName = in_attrName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attrId == null) ? 0 : attrId.hashCode());
		result = prime * result
				+ ((attrName == null) ? 0 : attrName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attribute other = (Attribute) obj;
		/*
		 * if (attrId == null) { if (other.attrId != null) return false; } else
		 * if (!attrId.equals(other.attrId)) return false;
		 */
		if (attrName == null) {
			if (other.attrName != null)
				return false;
		} else if (!attrName.equals(other.attrName))
			return false;
		return true;
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
	 * @return the attrValues
	 */
	public List<AttributeValue> getAttrValues() {
		return attrValues;
	}

	/**
	 * @param attrValues
	 *            the attrValues to set
	 */
	public void setAttrValues(List<AttributeValue> attrValues) {
		this.attrValues = attrValues;
	}

	public Set<Entry> fetchEntriesUsingThis() {
		Set<Entry> l_entriesUsingThis = new HashSet<Entry>();
		
		// Fetch entries with values for this attribute
		for (AttributeValue lt_av : this.getAttrValues()) {

			for (Entry lt_entry : lt_av.getEntryList()) {
				l_entriesUsingThis.add(lt_entry);
			}
		}
		return l_entriesUsingThis;
	}

	public Set<String> fetchStringValues() {
		Set<String> l_returnSet = new TreeSet<String>();
		
		for (AttributeValue lt_av : this.getAttrValues()) {
			l_returnSet.add(lt_av.getValue());
		}
		
		return l_returnSet;
	}

	/**
	 * @return the type
	 */
	public AttributeTypes getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(AttributeTypes type) {
		this.type = type;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * This method is called when we are attaching an attribute value to an entry.
	 * @param in_attributeValue
	 * @return True if the attribute value is still used, False if we have removed the attribute value
	 */	
	public boolean persistAttributeValue(AttributeValue in_attributeValue) {
		if (!this.equals(in_attributeValue.getAttribute())) {
			throw new IllegalArgumentException("The given attribute value does not belong to this attribute");
		}
		
		if (in_attributeValue.getEntryList().size()>0) {
			addAttrValue(in_attributeValue);
			return true;
		}
		
		this.getAttrValues().remove(in_attributeValue);
		in_attributeValue.setAttribute(null);
		in_attributeValue=null;
		
		return false;
				
	}
	
	/**
	 * Given a String, this method looks among the attribute values of this attribute. If found, it returns the attribute value using this string 
	 * @param in_attributeValue
	 * @return an AttributeValue, null if not found
	 */
	public AttributeValue fetchAttributeValue(AttributeValue in_attributeValue) {
		if (!in_attributeValue.getAttribute().equals(this)) {
			throw new IllegalArgumentException("This attribute value is not of the same attribute type");
		}
		
		return fetchAttributeValue(in_attributeValue.getValue());
	}

	/**
	 * Given a String, this method looks among the attribute values of this attribute. If found, it returns the attribute value using this string 
	 * @param in_stringValue
	 * @return an AttributeValue, null if not found
	 */
	private AttributeValue fetchAttributeValue(String in_stringValue) {
		if (in_stringValue==null || in_stringValue.isEmpty()) {
			throw new IllegalArgumentException("There value for an attribute value connaot be empty or null.");
		}
		
		for (AttributeValue lt_av : this.getAttrValues()) {
			if (lt_av.getValue().equals(in_stringValue))
				return lt_av;
		}
		
		return null;
	}
}
