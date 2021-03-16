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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 * @author Baubak
 * 
 */
@Entity
public class AttributeValue {
	@Id
	@Column(name = "attrvalue_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long avId;

	@ManyToOne
	// (cascade = CascadeType.ALL)
	// @JoinColumn(nullable=false, name="ATTR_ID", unique=true)
	@JoinColumn(name = "ATTR_ID")
	private Attribute attribute;

	//@Column(unique=true, nullable=false)
	private String value;

	@ManyToMany(mappedBy = "attributeValues")
	// private Set<Entry> entrySet;
	private List<Entry> entryList;

	/**
	 * 
	 */
	public AttributeValue() {
		setEntryList(new ArrayList<Entry>());
	}

	public AttributeValue(Attribute in_wsAttribute, String in_string) {
		if (in_string == null) {
			throw new IllegalArgumentException(
					"Null value given as a value for the attribute value");
		}
		setAttribute(in_wsAttribute);
		setEntryList(new ArrayList<Entry>());

		setValue(in_string);

		// Removed and moved to attach and persist attribute values. No
		// modification of classes Attribute and Attribute value should be done
		// here
		// in_wsAttribute.addAttrValue(this);

	}

	/**
	 * @param attribute
	 *            the attribute to set
	 */
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
		// attribute.addAttrValue(this);
	}

	/**
	 * @return the attribute
	 */
	public Attribute getAttribute() {
		return attribute;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * This method attaches this attribute value to a given entry
	 * 
	 * @param in_wsEntry
	 */
	public void attachToEntry(Entry in_wsEntry) {
		// in_wsEntry.

	}

	/**
	 * Check if the attribute value is empty
	 * 
	 * @return true if empty
	 */
	public boolean isEmpty() {
		if (getValue() == null)
			return true;
		if (getValue().isEmpty())
			return true;
		return false;
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
		result = prime * result
				+ ((attribute == null) ? 0 : attribute.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		AttributeValue other = (AttributeValue) obj;
		if (attribute == null) {
			if (other.attribute != null)
				return false;
		} else if (!attribute.equals(other.attribute))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public void setAvId(Long avId) {
		this.avId = avId;
	}

	public Long getAvId() {
		return avId;
	}

	/**
	 * @return the entryList
	 */
	public List<Entry> getEntryList() {
		return entryList;
	}

	/**
	 * @param entryList
	 *            the entryList to set
	 */
	public void setEntryList(List<Entry> entryList) {
		this.entryList = entryList;
	}
}
