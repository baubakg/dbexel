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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

/**
 * @author Baubak
 * 
 */
@Entity
@Table(name = "ENTRY")
public class Entry {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "entry_id")
	private Long entryId = new Long(0);

	// Each entry is connected to one worksheet ??
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REMOVE }, fetch = FetchType.EAGER)
	// @ManyToMany(cascade={CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinTable(name = "ENTRY_VALUES", joinColumns = @JoinColumn(name = "entry_id"), inverseJoinColumns = @JoinColumn(name = "attrvalue_id"))
	@ForeignKey(name = "FK_ENTRY_ENTRYVALUE")
	private List<AttributeValue> attributeValues;

	@ManyToOne
	// (targetEntity=WorkSheet.class)
	// @Column(name="ws_id")
	private WorkSheet workSheet;

	/**
	 * 
	 */
	public Entry() {
		setAttributeValues(new ArrayList<AttributeValue>());
	}

	/**
	 * @param in_attributeValues
	 *            the attributeValues to set
	 */
	public void setAttributeValues(List<AttributeValue> in_attributeValues) {
		this.attributeValues = in_attributeValues;
	}

	/**
	 * @return the attributeValues
	 */
	public List<AttributeValue> getAttributeValues() {
		return attributeValues;
	}

	/**
	 * @param entryId
	 *            the entryId to set
	 */
	public void setEntryId(Long entryId) {
		this.entryId = entryId;
	}

	/**
	 * @return the entryId
	 */
	public Long getEntryId() {
		return entryId;
	}

	/**
	 * @param workSheet
	 *            the workSheet to set
	 */
	public void setWorkSheet(WorkSheet workSheet) {
		this.workSheet = workSheet;
	}

	/**
	 * @return the workSheet
	 */
	public WorkSheet getWorkSheet() {
		return workSheet;
	}

	/**
	 * This method given an Attribute Value updates the map of attribute values
	 * associated with the current entry. If non existent it add the attribute
	 * value. If existing it is replaced
	 * 
	 * @param in_attributeValue
	 */
	protected boolean persistAttributeValue(AttributeValue in_attributeValue) {
		int l_index = findIndexByAttribute(in_attributeValue.getAttribute());
		
		Attribute l_wsAttribute=null;
		
		//	The worksheet shouldn't be null here
		//if (getWorkSheet() != null) {
		l_wsAttribute = getWorkSheet().fetchAttribute(
					in_attributeValue.getAttribute());
			
		if (l_wsAttribute!=null) {
			in_attributeValue.setAttribute(l_wsAttribute);
			
		}
				

		// if we found an entry for the used we will replace it
		if (l_index > -1) {
			if (!in_attributeValue.isEmpty()) {
				AttributeValue lt_oldAV = getAttributeValues().get(l_index);
				
				l_wsAttribute = lt_oldAV.getAttribute();

				lt_oldAV.getEntryList().remove(this);

				// Check references of this attribute value in regards to the
				// attribute.
				// Does it still a right to exist? If no attachments to other
				// entries, remove it.
				l_wsAttribute.persistAttributeValue(lt_oldAV);

				in_attributeValue = l_wsAttribute.addAttrValue(in_attributeValue);
				getAttributeValues().set(l_index, in_attributeValue);

			} else {
				/**
				 * The attribute Value == NULL or empty does not exist. We
				 * simply do not save it.
				 */
				getAttributeValues().remove(l_index);
				return false;
			}
		} else {
			// otherwise if not empty it is added
			if ((in_attributeValue.getValue() != null)
					&& (!in_attributeValue.getValue().isEmpty())) {
				
				if (l_wsAttribute==null) {
					l_wsAttribute = in_attributeValue.getAttribute();
				} 
				
				in_attributeValue = l_wsAttribute.addAttrValue(in_attributeValue);
				
				getAttributeValues().add(in_attributeValue);
				
			}

			else {
				return false;
			}
		}
		
		
		// Moved from attachAttributeValue
		in_attributeValue.getEntryList().add(this);
		return true;

	}

	/**
	 * This method attaches the given attribute value to the current Entry
	 * 
	 * @param in_attributeValue
	 */
	public void attachAttributeValue(AttributeValue in_attributeValue) {
		if (getWorkSheet()==null)
			throw new IllegalStateException("We really need to have a worksheet to ensure persistence");

		persistAttributeValue(in_attributeValue);

		this.getWorkSheet().attachAttribute(
					in_attributeValue.getAttribute());

			// this.getWorkSheet().findAttributeByName(in_attributeValue.getAttribute().getAttrName()).addAttrValue(in_attributeValue);
	}

	/**
	 * This method looks through the attached attribute values and returns the
	 * attribute value corresponding to the given Attribute. If the attribute is
	 * found its Attribute_Value is returned If the attribute is missing null is
	 * returned
	 * 
	 * @param in_workSheetAttribute
	 * @return The Attribute Value corresponding to the given Attribute
	 * @return Null is the Attribute is not found
	 */
	public AttributeValue findValueByAttribute(Attribute in_workSheetAttribute) {

		int l_index = findIndexByAttribute(in_workSheetAttribute);

		if (l_index > -1) {
			return getAttributeValues().get(l_index);
		}
		// return new AttributeValue(in_workSheetAttribute, null);
		return null;
	}

	/**
	 * This method returns the index of the searched attribute value when we
	 * search using the index
	 * 
	 * @param in_workSheetAttribute
	 * @return The index of the Attribute Value corresponding to the given
	 *         Attribute
	 * @return -1 is the Attribute is not found
	 */
	private int findIndexByAttribute(Attribute in_workSheetAttribute) {
		for (int i = 0; i < getAttributeValues().size(); i++) {

			if (getAttributeValues().get(i).getAttribute()
					.equals(in_workSheetAttribute)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * This method returns a list containing the attribute values that
	 * correspond to the attributes of the given worksheet.
	 * 
	 * If the given Attribute does not exist NULL is inserted into the List
	 * 
	 * @param in_workSheet
	 *            .
	 * @return List of Attribute Values
	 */
	// public List<AttributeValue> fetchAttributeValues(WorkSheet in_workSheet)
	// {
	public List<AttributeValue> fetchAttributeValues() {
		List<AttributeValue> l_attrValueList = new ArrayList<AttributeValue>();

		for (Attribute lt_wsAttribute : getWorkSheet().getAttributes()) {
			l_attrValueList.add(this.findValueByAttribute(lt_wsAttribute));
		}
		return l_attrValueList;
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
				+ ((attributeValues == null) ? 0 : attributeValues.hashCode());
		result = prime * result + ((entryId == null) ? 0 : entryId.hashCode());
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
		Entry other = (Entry) obj;
		if (attributeValues == null) {
			if (other.attributeValues != null)
				return false;
		} else if (!attributeValues.equals(other.attributeValues))
			return false;
		if (entryId == null) {
			if (other.entryId != null)
				return false;
		} else if (!entryId.equals(other.entryId))
			return false;
		return true;
	}

	/**
	 * This method updates the attribute values attached to this entry. The
	 * entry is a map of : Attribute_Ids and Values.
	 * 
	 * We parse through the attached attribute values. upon matches, we update
	 * the found value.
	 * 
	 * @param in_FormEntryforUpdate
	 */
	public void updateValues(Map<Long, String> in_FormEntryforUpdate) {
		Set<Long> l_listOfSetAttributes = new HashSet<Long>(
				getAttributeValues().size());

		// Make a list of attached attributeIds
		for (Attribute lt_wsAttribute : fetchSetAttributes()) {
			l_listOfSetAttributes.add(lt_wsAttribute.getAttrId());
		}

		// Parse the attribute Value list. This will update existing values
		for (int i = 0; i < getAttributeValues().size(); i++) {
			if (in_FormEntryforUpdate.containsKey(getAttributeValues().get(i)
					.getAttribute().getAttrId())) {
				getAttributeValues().get(i).setValue(
						in_FormEntryforUpdate.get(getAttributeValues().get(i)
								.getAttribute().getAttrId()));
			}
		}

		// Now let us see if there are new attached attribues
		for (Long lt_setAttrId : in_FormEntryforUpdate.keySet()) {
			if (!l_listOfSetAttributes.contains(lt_setAttrId)) {
				getAttributeValues().add(
						new AttributeValue(getWorkSheet().findAttributeById(
								lt_setAttrId), in_FormEntryforUpdate
								.get(lt_setAttrId)));
			}
		}

	}

	/**
	 * Parses the attached attribute values and returns the list of attached
	 * attributes
	 * 
	 * @return List of Attributes
	 */
	public Set<Attribute> fetchSetAttributes() {
		Set<Attribute> l_returnList = new HashSet<Attribute>();

		for (AttributeValue lt_AttributeValue : getAttributeValues())
			l_returnList.add(lt_AttributeValue.getAttribute());
		return l_returnList;
	}

	/**
	 * This method removes all attributes values having the the given attribute
	 * value from the entry
	 * 
	 * @param in_deletedAttribute
	 */
	public void removeAttribute(Attribute in_deletedAttribute) {

		for (Iterator<AttributeValue> lt_attributeValueIterator = this
				.getAttributeValues().iterator(); lt_attributeValueIterator
				.hasNext();) {

			AttributeValue lt_attributeValue = lt_attributeValueIterator.next();

			// Check if this attribute value is of the same given attribute
			if (lt_attributeValue.getAttribute().equals(in_deletedAttribute))
				lt_attributeValueIterator.remove();
		}

	}
}
