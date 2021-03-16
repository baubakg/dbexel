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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import dbexel.model.mapping.WorkSheet;
import dbexel.model.mapping.Attribute;
import dbexel.model.mapping.Entry;
import dbexel.system.tools.DBExelTestTools;

public class TestWorkSheet {
	WorkSheet workSheet;

	@Before
	public void prepare() {
		workSheet = new WorkSheet("STD Worksheet");
	}

	// Just testing a simple creation of a worksheet
	@Test
	public void testWorkSheetCreation() {
		WorkSheet l_ws = new WorkSheet("MyFirstWorkSheet");

		assertEquals("MyFirstWorkSheet", l_ws.getWs_Name());
	}

	/**
	 * Testing the addition of attributes
	 */
	@Test
	public void testAddAttributes() {
		workSheet.setAttributes(new ArrayList<Attribute>());

		Attribute l_myAttribute = new Attribute(
				"First Column");

		workSheet.attachAttribute(l_myAttribute);

		assertEquals("1. ", 1, workSheet.getAttributes().size());
	}

	/**
	 * Testing the addition of an entry
	 */
	@Test
	public void testEntryAdditions() {
		// Define Entry
		Entry l_wsEntry = new Entry();
		List<Entry> l_wsEntrySet = new ArrayList<Entry>();
		l_wsEntrySet.add(l_wsEntry);
		workSheet.setEntries(l_wsEntrySet);

		assertEquals("1. We should have the proper number of entries.", 1,
				workSheet.getEntries().size());

		Entry l_wsEntry2 = new Entry();

		workSheet.addEntry(l_wsEntry2);

		assertEquals("2. We should have the proper number of entries.", 2,
				workSheet.getEntries().size());
	}

	/**
	 * Testing the renaming of a worksheet
	 */
	@Test
	public void testRenameWorkSheet() {
		// first check the the original value is the same as before
		assertEquals("1. Initial chek", "STD Worksheet", workSheet.getWs_Name());

		workSheet.setWs_Name("Modified WS Name");

		assertEquals("1. Initial chek", "Modified WS Name",
				workSheet.getWs_Name());

	}

	/**
	 * Testing how we can move an attribute to a specific location in the
	 * attributes list
	 */
	@Test
	public void testMoveAttribute() {
		Attribute l_wsAttribute1 = new Attribute("Column A");
		Attribute l_wsAttribute2 = new Attribute("Column C");
		Attribute l_wsAttribute3 = new Attribute("Column B");
		workSheet.attachAttribute(l_wsAttribute1);
		workSheet.attachAttribute(l_wsAttribute2);
		workSheet.attachAttribute(l_wsAttribute3);

		/* Testing moving an attribute to the left */
		workSheet.moveWorkSheetAttribute(l_wsAttribute2, 0);

		assertEquals("1A.", "Column C", workSheet.getAttributes().get(0)
				.getAttrName());
		assertEquals("1B.", "Column A", workSheet.getAttributes().get(1)
				.getAttrName());
		assertEquals("1C.", "Column B", workSheet.getAttributes().get(2)
				.getAttrName());

		workSheet.moveWorkSheetAttribute(l_wsAttribute3, 0);

		assertEquals("2A.", "Column B", workSheet.getAttributes().get(0)
				.getAttrName());
		assertEquals("2B.", "Column C", workSheet.getAttributes().get(1)
				.getAttrName());
		assertEquals("2C.", "Column A", workSheet.getAttributes().get(2)
				.getAttrName());

		workSheet.moveWorkSheetAttribute(l_wsAttribute1, 1);

		assertEquals("3A.", "Column B", workSheet.getAttributes().get(0)
				.getAttrName());
		assertEquals("3B.", "Column A", workSheet.getAttributes().get(1)
				.getAttrName());
		assertEquals("3C.", "Column C", workSheet.getAttributes().get(2)
				.getAttrName());

		/* Testing moving an attribute to the right */
		workSheet.moveWorkSheetAttribute(l_wsAttribute3, 2);

		assertEquals("4A.", "Column A", workSheet.getAttributes().get(0)
				.getAttrName());
		assertEquals("4B.", "Column C", workSheet.getAttributes().get(1)
				.getAttrName());
		assertEquals("4C.", "Column B", workSheet.getAttributes().get(2)
				.getAttrName());

		workSheet.moveWorkSheetAttribute(l_wsAttribute2, 2);

		assertEquals("5A.", "Column A", workSheet.getAttributes().get(0)
				.getAttrName());
		assertEquals("5B.", "Column B", workSheet.getAttributes().get(1)
				.getAttrName());
		assertEquals("5C.", "Column C", workSheet.getAttributes().get(2)
				.getAttrName());

		/*
		 * Testing that selecting to move an attribute to its own location does
		 * not cause any problems
		 */
		workSheet.moveWorkSheetAttribute(l_wsAttribute3, 1);

		assertEquals("5A.", "Column A", workSheet.getAttributes().get(0)
				.getAttrName());
		assertEquals("5B.", "Column B", workSheet.getAttributes().get(1)
				.getAttrName());
		assertEquals("5C.", "Column C", workSheet.getAttributes().get(2)
				.getAttrName());
	}

	/**
	 * Testing the new method. FindAttributeById
	 */
	@Test
	public void testFindAttributeById() {
		Attribute l_wsAttr1 = new Attribute("Column 3");
		l_wsAttr1.setAttrId(new Long(8));
		Attribute l_wsAttr2 = new Attribute("Column 5");
		l_wsAttr2.setAttrId(new Long(9));
		Attribute l_wsAttr3 = new Attribute("Column 7");
		l_wsAttr3.setAttrId(new Long(23));
		Attribute l_wsAttr4 = new Attribute("Column 11");
		l_wsAttr4.setAttrId(new Long(27));
		Attribute l_wsAttr5 = new Attribute("Column 13");
		l_wsAttr5.setAttrId(new Long(31));
		Attribute l_wsAttr6 = new Attribute("Column 6");
		l_wsAttr6.setAttrId(new Long(37));

		workSheet.attachAttribute(l_wsAttr1);
		workSheet.attachAttribute(l_wsAttr2);
		workSheet.attachAttribute(l_wsAttr3);
		workSheet.attachAttribute(l_wsAttr4);
		Attribute l_wsAttribute = workSheet
				.findAttributeById(new Long(27));
		assertEquals("1. The correct Id was not found", "Column 11",
				l_wsAttribute.getAttrName());

		workSheet.attachAttribute(l_wsAttr5);

		l_wsAttribute = workSheet.findAttributeById(new Long(8));
		assertEquals("2. The correct Id was not found", "Column 3",
				l_wsAttribute.getAttrName());

		assertNull("3. if a value is not found we should return null",
				workSheet.findAttributeById(Long.valueOf(88)));
	}

	/**
	 * Testing the new method. FindAttributeByName
	 */
	@Test
	public void testFindAttributeByName() {
		Attribute l_wsAttr1 = new Attribute("Column 3");
		l_wsAttr1.setAttrId(new Long(8));
		Attribute l_wsAttr2 = new Attribute("Column 5");
		l_wsAttr2.setAttrId(new Long(9));
		Attribute l_wsAttr3 = new Attribute("Column 7");
		l_wsAttr3.setAttrId(new Long(23));
		Attribute l_wsAttr4 = new Attribute("Column 11");
		l_wsAttr4.setAttrId(new Long(27));
		Attribute l_wsAttr5 = new Attribute("Column 13");
		l_wsAttr5.setAttrId(new Long(31));
		Attribute l_wsAttr6 = new Attribute("Column 6");
		l_wsAttr6.setAttrId(new Long(37));

		workSheet.attachAttribute(l_wsAttr1);
		workSheet.attachAttribute(l_wsAttr2);
		workSheet.attachAttribute(l_wsAttr3);
		workSheet.attachAttribute(l_wsAttr4);

		Attribute l_wsAttribute = workSheet
				.findAttributeByName("Column 11");
		assertEquals("1. The correct attribute was not found",
				Long.valueOf(27), l_wsAttribute.getAttrId());

		assertNull("3. if a value is not found we should return null",
				workSheet.findAttributeByName("Column 88"));
	}

	/**
	 * We should have unique attributes for a worksheet
	 */
	@Test
	public void testAddUniqueAttributes() {

		Attribute l_attr1 = new Attribute("Column1");

		workSheet.attachAttribute(l_attr1);

		assertEquals(1, workSheet.getAttributes().size());

		Attribute l_attr2 = new Attribute("Column1");

		workSheet.attachAttribute(l_attr2);

		assertEquals(1, workSheet.getAttributes().size());
	}

	/**
	 * We should have unique attributes for a worksheet. Here the attributes
	 * were made while creating an attribute value
	 */
	@Test
	public void testAddUniqueAttributesCaseAsInAttributeValues() {
		AttributeValue l_av1 = new AttributeValue(new Attribute(
				"Column1"), "val1");
		Attribute l_attr1 = l_av1.getAttribute();

		workSheet.attachAttribute(l_attr1);

		assertEquals(1, workSheet.getAttributes().size());

		AttributeValue l_av2 = new AttributeValue(new Attribute(
				"Column1"), "val2");
		Attribute l_attr2 = l_av2.getAttribute();

		workSheet.attachAttribute(l_attr2);

		assertEquals(1, workSheet.getAttributes().size());
	}

	// Testing the automatism of the attachment of attributes
	@Test
	public void attachingAttributeValueWithNewAttributeToEntry_1() {
		WorkSheet l_workSheet = new WorkSheet("My WorkSheet");
		Entry l_entry = new Entry();

		l_workSheet.addEntry(l_entry);

		AttributeValue l_attrAttributeValue = new AttributeValue(
				new Attribute("Col1"), "col1 Value");

		Attribute l_workSheet_Attribute = l_attrAttributeValue
				.getAttribute();
		
		assertEquals("Col1", l_workSheet_Attribute.getAttrName());

		l_entry.attachAttributeValue(l_attrAttributeValue);

		assertThat(l_workSheet.getAttributes().get(0), is(equalTo(l_workSheet_Attribute)));
		
		assertThat(l_workSheet_Attribute.getAttrValues().size(), equalTo(1));
		
		assertThat(l_entry.getAttributeValues().size(), equalTo(1));
		
		assertEquals("col1 Value", l_entry.getAttributeValues().get(0)
				.getValue());

		assertEquals("The Attribute is not attached to the worksheet", 1,
				l_workSheet.getAttributes().size());

		// assertEquals(l_entry, l_attrAttributeValue.getEntrySet().r);
	}

	// Testing the automatism of the attachment of attributes
	@Test
	public void attachingAttributeValueWithNewAttributeToEntry_2() {
		WorkSheet l_workSheet = new WorkSheet("My WorkSheet");
		Entry l_entry = new Entry();
		l_workSheet.addEntry(l_entry);
		
		AttributeValue l_attrAttributeValue = new AttributeValue(
				new Attribute("Col1"), "col1 Value");

		Attribute l_workSheet_Attribute = l_attrAttributeValue
				.getAttribute();
		assertEquals("Col1", l_workSheet_Attribute.getAttrName());

		l_entry.attachAttributeValue(l_attrAttributeValue);

		assertEquals("col1 Value", l_entry.getAttributeValues().get(0)
				.getValue());
		

		assertEquals("The Attribute is not attached to the worksheet", 1,
				l_workSheet.getAttributes().size());

		// assertEquals(l_entry, l_attrAttributeValue.getEntrySet().r);
	}

	// This method tests the new remove attribute method
	@Test
	public void testRemoveAttribute() {
		WorkSheet l_workSheet = DBExelTestTools
				.fetchTestWorkSheetWithId("Remove Attribute WorkSheet");

		int l_refNrOfAttributes = l_workSheet.getAttributes().size();

		Attribute l_deletedAttribute = l_workSheet.getAttributes()
				.get(l_refNrOfAttributes - 1);

		// remove attribute
		l_workSheet.removeAttribute(l_deletedAttribute);

		assertThat(l_workSheet.getAttributes().size(),
				is(lessThan(l_refNrOfAttributes)));
		assertThat("The attribute was not removed from the worksheet itself",
				l_deletedAttribute, not(isIn(l_workSheet.getAttributes())));

		// Checking that the attribute has been removed from the attribute
		// values of the entry
		for (Entry lt_entry : l_workSheet.getEntries()) {
			assertThat("The attribute was not removed from the entries",
					l_deletedAttribute,
					not(isIn(lt_entry.fetchSetAttributes())));
		}
	}
	
	//Testing fetch attribute
		//This is to ensure unique attributes
		@Test
		public void testFetchAttribute1() {
			WorkSheet l_workSheet = new WorkSheet("My WorkSheet");
			
			l_workSheet.attachAttribute(new Attribute("A"));
			
			assertThat(l_workSheet.fetchAttribute(new Attribute("A")), is(equalTo(l_workSheet.getAttributes().get(0))));
			
			assertTrue(l_workSheet.fetchAttribute(new Attribute("A"))==l_workSheet.getAttributes().get(0));
		}
	
	//Testing fetch attribute
	//This is to ensure unique attributes
	@Test
	public void testFetchAttribute2() {
		WorkSheet l_workSheet = new WorkSheet("My WorkSheet");

		Entry l_entry1 = new Entry();
		Entry l_entry2 = new Entry();
		
		l_workSheet.addEntry(l_entry1);
		l_workSheet.addEntry(l_entry2);
		
		l_entry1.attachAttributeValue(new AttributeValue(new Attribute("a"), "Val 1"));
		l_entry2.attachAttributeValue(new AttributeValue(new Attribute("a"), "Val 1"));
		
		assertThat(l_workSheet.getAttributes().size(), is(equalTo(1)));
		
		assertThat(l_entry1.getAttributeValues().get(0), is(equalTo(l_entry2.getAttributeValues().get(0))));
		
		assertThat(l_entry1.getAttributeValues().get(0).getAttribute(), is(equalTo(l_entry2.getAttributeValues().get(0).getAttribute())));
		
		assertThat(l_entry1.getAttributeValues().get(0).getAttribute(), is(equalTo(l_workSheet.getAttributes().get(0))));
		assertThat(l_entry2.getAttributeValues().get(0).getAttribute(), is(equalTo(l_workSheet.getAttributes().get(0))));
		
		assertTrue(l_entry1.getAttributeValues().get(0).getAttribute() == l_workSheet.getAttributes().get(0));
		assertTrue(l_entry2.getAttributeValues().get(0).getAttribute() == l_workSheet.getAttributes().get(0));

		
		assertTrue(l_entry1.getAttributeValues().get(0)==l_entry2.getAttributeValues().get(0));
		
		
		
	}
	
}
