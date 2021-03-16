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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import dbexel.model.mapping.AttributeValue;
import dbexel.model.mapping.WorkSheet;
import dbexel.model.mapping.Attribute;
import dbexel.model.mapping.Entry;

/**
 * This class tests the attribute values
 * 
 * @author Baubak
 * 
 */
public class TestAttributeValue {
	private Entry wsEntry;
	private WorkSheet ws;
	private Attribute wsAttribute;

	@Before
	public void createContext() {
		// Create Worksheet and attach an entry to it
		wsEntry = new Entry();
		ws = new WorkSheet("Test WorkSheet");
		ws.addEntry(wsEntry);
		wsAttribute = new Attribute("Column 1");
		// Attribute
		ws.attachAttribute(wsAttribute);
	}

	/**
	 * Testing the creation of an attribute value
	 */
	@Test
	public void createAttributeValue() {
		AttributeValue l_attributeValue = new AttributeValue();

		// attach attribute
		l_attributeValue.setAttribute(wsAttribute);

		// attaching a value to the attribute value
		l_attributeValue.setValue("Hello World");

		assertEquals("1. We test the correct creation of our attribute value",
				"Column 1", l_attributeValue.getAttribute().getAttrName());

	}
	
	/**
	 * Testing the creation of an attribute value
	 */
	@Test(expected=IllegalArgumentException.class)
	public void createAttributeValueNullValue() {
		AttributeValue l_attributeValue = new AttributeValue(wsAttribute, null);

		assertEquals("1. We test the correct creation of our attribute value",
				"Column 1", l_attributeValue.getAttribute().getAttrName());

	}

	/**
	 * Test attaching attribute value to an entry
	 */
	@Test
	public void attributeValueToEntry() {
		AttributeValue l_attributeValue = new AttributeValue(wsAttribute, "Boo");

		// attach the AV to an entry
		l_attributeValue.attachToEntry(wsEntry);
	}

	/**
	 * Testing to see if the given AttributeValue is empty
	 */
	@Test
	public void checkingToSeeIfAttributeValueIsEmpty() {
		AttributeValue l_attributeValue = new AttributeValue(wsAttribute, "");

		assertTrue("1. We did not correctly identify an empty attribute Value",
				l_attributeValue.isEmpty());
	}

	@Test
	public void attachingAttributeValueToEntry() {
		
		WorkSheet l_ws = new WorkSheet("A");
		AttributeValue l_attrAttributeValue = new AttributeValue(
				new Attribute("Col1"), "col1 Value");
		Attribute l_attribute = l_attrAttributeValue
				.getAttribute();
		assertEquals("Col1", l_attribute.getAttrName());

		Entry l_entry = new Entry();
		l_ws.addEntry(l_entry);

		l_entry.attachAttributeValue(l_attrAttributeValue);

		assertEquals("col1 Value", l_entry.getAttributeValues().get(0)
				.getValue());

		// assertEquals("The Attribute is not attached to the worksheet", 1, );

		// assertEquals(l_entry, l_attrAttributeValue.getEntrySet().r);
	}

	

}
