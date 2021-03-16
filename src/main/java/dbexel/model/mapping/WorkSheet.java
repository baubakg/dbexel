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
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name = "WORKSHEET")
public class WorkSheet {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "WS_ID")
	private Long ws_Id = new Long(0);

	@Column(name = "WS_NAME")
	private String ws_Name;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	// @OneToMany(mappedBy="workSheet")
	@JoinTable(name = "WORKSHEET_ATTRIBUTES", joinColumns = @JoinColumn(name = "WS_ID"), inverseJoinColumns = @JoinColumn(name = "ATTR_ID"))
	@ForeignKey(name = "FK_WORKSHEET_ATTRIBUTE")
	private List<Attribute> attributes;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REMOVE })
	// @OneToMany(mappedBy="workSheet")
	// @OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	@JoinTable(name = "WORKSHEET_ENTRIES")
	@JoinColumn(name = "entry_id")
	@ForeignKey(name = "FK_WORKSHEET_ENTRY")
	private List<Entry> entries;

	public WorkSheet() {
		attributes = new ArrayList<Attribute>();
		entries = new ArrayList<Entry>();
	}

	public WorkSheet(final String in_wsName) {
		ws_Name = in_wsName;
		attributes = new ArrayList<Attribute>();
		entries = new ArrayList<Entry>();
	}

	public Long getWs_Id() {
		return ws_Id;
	}

	public void setWs_Id(Long in_wsId) {
		ws_Id = in_wsId;
	}

	public String getWs_Name() {
		return ws_Name;
	}

	public void setWs_Name(String in_wsName) {
		ws_Name = in_wsName;
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(ArrayList<Attribute> in_attributes) {
		this.attributes = in_attributes;
	}

	/**
	 * @return the attributes
	 */
	public List<Attribute> getAttributes() {
		return attributes;
	}

	/**
	 * This method add an attribute to the Worksheet
	 * 
	 * @param in_myAttribute
	 */
	public void attachAttribute(Attribute in_myAttribute) {
		/*
		getAttributes().add(in_myAttribute);

		in_myAttribute.setWorkSheet(this);
		 */
		if (!getAttributes().contains(in_myAttribute)) {
			getAttributes().add(in_myAttribute);
			in_myAttribute.setWorkSheet(this);
		}
	}

	/**
	 * @param entries
	 *            the entries to set
	 */
	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	/**
	 * @return the entries
	 */
	public List<Entry> getEntries() {
		return entries;
	}

	/**
	 * This method adds an entry to the current worksheet.
	 * 
	 * We should also persist all attributes
	 * 
	 * @param in_WorkSheetEntry
	 */
	public void addEntry(Entry in_WorkSheetEntry) {
		getEntries().add(in_WorkSheetEntry);
		in_WorkSheetEntry.setWorkSheet(this);

		//Parse the attributes of the attached entry and attach them if needed
		for (Attribute lt_attribute : in_WorkSheetEntry.fetchSetAttributes()) {
			this.attachAttribute(lt_attribute);
		}
	}

	/**
	 * This method given an attribute and an index, moves the attribute to that
	 * index
	 * 
	 * @param in_wsAttribute
	 * @param to_index
	 */
	public void moveWorkSheetAttribute(Attribute in_wsAttribute,
			int to_index) {
		int from_index = findWorkSheetAttribute(in_wsAttribute);
		if ((to_index > getAttributes().size()) || (to_index < 0)) {
			throw new IndexOutOfBoundsException("The given index " + to_index
					+ " is not within the domain of the attached attributes.");
		}

		if (from_index < 0) {
			throw new IndexOutOfBoundsException("The given attribute "
					+ in_wsAttribute.getAttrName()
					+ " is not attached to the worksheet yet.");
		}

		// when the destination is to the left
		if (from_index > to_index) {
			for (int i = from_index; i > to_index; i--) {
				// move the item to the left to the current position
				getAttributes().set(i, getAttributes().get(i - 1));
			}
			// ow that all attributes from from_index to to_index have been
			// shifted right
			getAttributes().set(to_index, in_wsAttribute);

		}

		// when the destination is to the left
		if (from_index < to_index) {
			for (int i = from_index; i < to_index; i++) {
				// move the item to the left to the current position
				getAttributes().set(i, getAttributes().get(i + 1));
			}
			// ow that all attributes from from_index to to_index have been
			// shifted right
			getAttributes().set(to_index, in_wsAttribute);

		}
	}

	/**
	 * This method will find the index of the given attribute
	 * 
	 * @param in_wsAttribute
	 * @return the index of the attribute
	 */
	private int findWorkSheetAttribute(Attribute in_wsAttribute) {
		for (int i = 0; i < getAttributes().size(); i++) {
			if (getAttributes().get(i) == in_wsAttribute) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * This method searches the list of attached attributes for a given
	 * AttributeId
	 * 
	 * @param in_WorkSheetAttributeId
	 * @return the corresponding Attribute. Otherwise null
	 */
	public Attribute findAttributeById(Long in_WorkSheetAttributeId) {
		// parse attached attributes
		for (Attribute lt_WorkSheetAttribute : getAttributes()) {
			if (lt_WorkSheetAttribute.getAttrId().equals(
					in_WorkSheetAttributeId))
				return lt_WorkSheetAttribute;
		}
		return null;
	}

	/**
	 * This method returns an attribute given it's name
	 * 
	 * @param in_attributeName
	 * @return the corresponding Attribute. Otherwise null
	 */
	public Attribute findAttributeByName(String in_attributeName) {
		// parse attached attributes
		for (Attribute lt_WorkSheetAttribute : getAttributes()) {
			if (lt_WorkSheetAttribute.getAttrName().equals(
					in_attributeName))
				return lt_WorkSheetAttribute;
		}
		
		return null;
	}

	/**
	 * This method removes a given attribute from the worksheet
	 * @param in_deletedAttribute
	 */
	public void removeAttribute(Attribute in_deletedAttribute) {
		// remove attribute from WorkSheet		
		for (Iterator<Attribute> lt_attribIterator = this
				.getAttributes().iterator(); lt_attribIterator.hasNext();) {

			Attribute lt_attribute = lt_attribIterator.next();

			if (lt_attribute.equals(in_deletedAttribute))
				lt_attribIterator.remove();
		}
		
		//remove attribute values of this attribute
		for (Entry lt_entry : this.getEntries()) {
			lt_entry.removeAttribute(in_deletedAttribute);
		}

		
	}

	/**
	 * This looks for the given attribute among its attached attributes. When found it returns it. Otherwise it will return null
	 * @param in_attribute
	 * @return
	 */
	public Attribute fetchAttribute(Attribute in_attribute) {
		for (Attribute lt_attribute : this.getAttributes()) {
			if (lt_attribute.equals(in_attribute))
				return lt_attribute;
		}

		return null;
	}

}
