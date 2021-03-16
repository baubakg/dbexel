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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import dbexel.model.mapping.AttributeValue;
import dbexel.model.mapping.WorkSheet;
import dbexel.model.mapping.Attribute;
import dbexel.model.mapping.Entry;
import dbexel.system.tools.DBExelTestTools;

public class TestWorkSheetEntry {

	WorkSheet ws;
	Entry wsEntry;
	private Attribute wsAttribute;

	@Before
	public void prepare() {
		ws = new WorkSheet("STD Worksheet");
		wsEntry = new Entry();

		wsAttribute = new Attribute("Column 1");
		wsAttribute.setAttrId(new Long(3));

		// Attribute
		ws.attachAttribute(wsAttribute);
		ws.addEntry(wsEntry);
	}

	/**
	 * This method tests that we can correctly create a work sheet entry
	 */
	@Test
	public void testCreateEntry() {
		Entry l_wsEntry = new Entry();

		// attach Entry to worksheet
		ws.addEntry(l_wsEntry);
		assertEquals("1. ", "STD Worksheet", l_wsEntry.getWorkSheet()
				.getWs_Name());
	}

	/**
	 * We want to find an attached attribute value given an Attribute
	 */
	@Test
	public void testFindAttributeValue() {
		AttributeValue l_av = new AttributeValue(wsAttribute,
				"My BloddyValentine");

		Attribute l_wsattrib2 = new Attribute("Column 2");
		AttributeValue l_av2 = new AttributeValue(l_wsattrib2, "Ashes to Ashes");

		wsEntry.persistAttributeValue(l_av);
		wsEntry.persistAttributeValue(l_av2);

		AttributeValue l_foundAttribute = wsEntry
				.findValueByAttribute(wsAttribute);

		assertEquals("1. ", l_av, l_foundAttribute);

		l_foundAttribute = wsEntry.findValueByAttribute(l_wsattrib2);

		assertEquals("2. ", l_av2, l_foundAttribute);

		assertNull(
				"3. when looking for an unknown attribute we should return NULL",
				wsEntry.findValueByAttribute(new Attribute("Column 3")));
	}

	/**
	 * Testing the addition of an AttributeValue
	 */
	@Test
	public void testAttachAttributeValue() {
		
		//New av attached to old attribute
		AttributeValue l_av = new AttributeValue(wsAttribute,
				"My BloddyValentine");
		
		//List<AttributeValue> l_avSet = new ArrayList<AttributeValue>();
		//l_avSet.add(l_av);
		wsEntry.attachAttributeValue(l_av);

		assertEquals("1A. See that we correctly added the Attribute Value", 1,
				wsEntry.getAttributeValues().size());
		assertEquals("1B. See that we correctly added the Attribute Value",
				"My BloddyValentine", wsEntry.findValueByAttribute(wsAttribute)
						.getValue());
		assertEquals("1C. See that we correctly added the Attribute Value",
				"Column 1", wsEntry.findValueByAttribute(wsAttribute)
						.getAttribute().getAttrName());
		
		assertThat(ws.getAttributes().size(),is(equalTo(1)));
		assertThat(ws.getAttributes().get(0).getAttrValues().size(), is(equalTo(1)));
		
		assertThat(l_av.getEntryList().size(), equalTo(1));
	}

	/**
	 * Testing the addition of an AttributeValue Part 2
	 */
	@Test
	public void testAttachAttributeValue2() {
		AttributeValue l_av = new AttributeValue(wsAttribute,
				"My Bloddy Valentine");

		wsEntry.persistAttributeValue(l_av);

		assertEquals("1A. See that we correctly added the Attribute Value", 1,
				wsEntry.getAttributeValues().size());
		assertEquals("1B. See that we correctly added the Attribute Value",
				"My Bloddy Valentine", wsEntry
						.findValueByAttribute(wsAttribute).getValue());
		assertEquals("1C. See that we correctly added the Attribute Value",
				"Column 1", wsEntry.findValueByAttribute(wsAttribute)
						.getAttribute().getAttrName());

		// Testing that the attribute are correctly replaced
		AttributeValue l_av2 = new AttributeValue(wsAttribute, "Ashes to Ashes");

		// Here the attribute value should just replace the old value because
		// the attribute already exists
		wsEntry.persistAttributeValue(l_av2);

		assertEquals("2A. See that we correctly added the Attribute Value", 1,
				wsEntry.getAttributeValues().size());
		assertEquals("2B. See that we correctly added the Attribute Value",
				"Ashes to Ashes", wsEntry.findValueByAttribute(wsAttribute)
						.getValue());
		assertEquals("2C. See that we correctly added the Attribute Value",
				"Column 1", wsEntry.findValueByAttribute(wsAttribute)
						.getAttribute().getAttrName());

		// We should not add an empty attribute value to the entry

		/** Should we implment this? **/

		assertThat(ws.getAttributes().size(),is(equalTo(1)));
		assertThat(ws.getAttributes().get(0).getAttrValues().size(), is(equalTo(1)));
		assertThat(ws.getAttributes().get(0).getAttrValues().get(0).getValue(), equalTo(l_av2.getValue()));
	}
	
	
	// Create a persist attributeValue for the attributes.
			// It checks if an attribute is used. If not it removes it.
			@Test
			public void testPersistAttributeValues_WhereAttributesCreatedOnTheFlow() {
				WorkSheet l_ws = new WorkSheet("My WorkSheet");

				l_ws.addEntry(new Entry());

				Entry l_entry = l_ws.getEntries().get(0);

				AttributeValue l_attributeValue1 = new AttributeValue(new Attribute("Column 1"),
						"Val 1");
				l_entry.attachAttributeValue(l_attributeValue1);

				Attribute l_attribute1 = l_ws.getAttributes().get(0);

				AttributeValue l_attributeValue2 = new AttributeValue(new Attribute("Column 1"),
						"Val2");
				
				assertEquals(l_attributeValue1.getAttribute(), l_attributeValue2.getAttribute());

				// Now add a new attribute value with the same characteristics as
				l_entry.attachAttributeValue(l_attributeValue2);

				assertThat("The old attribute value should be now deleted",l_attributeValue1.getAttribute(), is(equalTo(null)));

				assertThat(l_attributeValue2.getAttribute().getAttrValues().size(), is(equalTo(1)));
				assertThat(l_attribute1.getAttrValues().size(), is(equalTo(1)));
				
				assertThat(
						"The persisted attribute value should no longer be among the values of this attribute.",
						l_attribute1.getAttrValues(), hasItem(l_attributeValue2));
				
				assertThat(
						"The persisted attribute value should no longer be among the values of this attribute.",
						l_attribute1.getAttrValues(), not(hasItem(l_attributeValue1)));

				assertThat(l_entry.getAttributeValues().size(), is(equalTo(1)));
			}
			

	// We need to make sure that we do not attach empty values
	// 90: When adding an entry, not filling an attribute value still stores it
	@Test
	public void testAttachEmptyAttributeValue() {
		AttributeValue l_av = new AttributeValue(wsAttribute, "");

		wsEntry.attachAttributeValue(l_av);

		assertEquals(
				"1. We should not have this attribute value added to the entry because it is empty",
				0, wsEntry.getAttributeValues().size());

		assertEquals(
				"The attribute value should not be connected to this entry", 0,
				l_av.getEntryList().size());
	}

	/**
	 * Testing the getAttributeValues(WorkSheet) It returns the attribute values
	 * of an entry in regards to a Worksheet
	 */
	@Test
	public void test_getworkSheetAttributeValues() {
		Attribute l_wsAttr2 = new Attribute("Column 2");
		Attribute l_wsAttr3 = new Attribute("Column 3");
		Attribute l_wsAttr4 = new Attribute("Column 4");
		Attribute l_wsAttr5 = new Attribute("Column 5");
		Attribute l_wsAttr6 = new Attribute("Column 6");

		ws.attachAttribute(l_wsAttr2);
		ws.attachAttribute(l_wsAttr3);
		ws.attachAttribute(l_wsAttr4);

		wsEntry.persistAttributeValue(new AttributeValue(wsAttribute, "Value 1"));
		wsEntry.persistAttributeValue(new AttributeValue(l_wsAttr3, "Value 3"));
		wsEntry.persistAttributeValue(new AttributeValue(l_wsAttr4, "Value 4"));
		wsEntry.persistAttributeValue(new AttributeValue(l_wsAttr5, "Value 5"));
		wsEntry.persistAttributeValue(new AttributeValue(l_wsAttr6, "Value 6"));
		ws.addEntry(wsEntry);

		List<AttributeValue> l_wsAttrValues = wsEntry.fetchAttributeValues();

		assertEquals("1. ", 6, l_wsAttrValues.size());

	}

	/**
	 * Testing the update of an entry
	 */
	@Test
	public void test_updateEntryValues() {

		/* Initializing entry */
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

		ws.attachAttribute(l_wsAttr1);
		ws.attachAttribute(l_wsAttr2);
		ws.attachAttribute(l_wsAttr3);
		ws.attachAttribute(l_wsAttr4);
		ws.attachAttribute(l_wsAttr5);

		wsEntry.persistAttributeValue(new AttributeValue(wsAttribute, "Value 1"));
		wsEntry.persistAttributeValue(new AttributeValue(l_wsAttr3, "Value 3"));
		wsEntry.persistAttributeValue(new AttributeValue(l_wsAttr4, "Value 4"));
		wsEntry.persistAttributeValue(new AttributeValue(l_wsAttr5, "Value 5"));
		wsEntry.persistAttributeValue(new AttributeValue(l_wsAttr6, "Value 6"));

		/* Creating a form list */
		Map<Long, String> in_formEntryforUpdate = new HashMap<Long, String>();

		in_formEntryforUpdate.put(new Long(31), "This");
		in_formEntryforUpdate.put(new Long(27), "is");
		in_formEntryforUpdate.put(new Long(23), "a");
		in_formEntryforUpdate.put(new Long(9), "test");
		in_formEntryforUpdate.put(new Long(8), "entry");

		// Testing method fetchSetAttributes
		Set<Attribute> l_wsAttrSet = wsEntry.fetchSetAttributes();

		assertEquals("1. ", 5, l_wsAttrSet.size());

		ws.addEntry(wsEntry);
		wsEntry.updateValues(in_formEntryforUpdate);
		int i = 1;
		for (AttributeValue lt_av : wsEntry.fetchAttributeValues()) {
			if (in_formEntryforUpdate.containsKey(lt_av.getAttribute()
					.getAttrId()))
				assertEquals(++i + ". ", in_formEntryforUpdate.get(lt_av
						.getAttribute().getAttrId()), lt_av.getValue());
		}

	}

	/**
	 * Testing the update of an entry
	 */
	@Test
	public void test_persistEntryValue() {

		/* Initializing entry */
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

		ws.attachAttribute(l_wsAttr1);
		ws.attachAttribute(l_wsAttr2);
		ws.attachAttribute(l_wsAttr3);
		ws.attachAttribute(l_wsAttr4);
		ws.attachAttribute(l_wsAttr5);

		wsEntry.persistAttributeValue(new AttributeValue(wsAttribute, "Value 1"));
		wsEntry.persistAttributeValue(new AttributeValue(l_wsAttr3, "Value 3"));
		wsEntry.persistAttributeValue(new AttributeValue(l_wsAttr4, "Value 4"));
		wsEntry.persistAttributeValue(new AttributeValue(l_wsAttr5, "Value 5"));
		wsEntry.persistAttributeValue(new AttributeValue(l_wsAttr6, "Value 6"));

		assertEquals("1A. ", wsEntry.getAttributeValues().size(), 5);
		assertEquals("1B. ", wsEntry.getAttributeValues().get(2).getValue(),
				"Value 4");

		/**
		 * The attribute Value == NULL or empty does not exist. We simply do not
		 * save it.
		 */
		wsEntry.persistAttributeValue(new AttributeValue(l_wsAttr4, ""));

		assertEquals("2A. ", wsEntry.getAttributeValues().size(), 4);
		assertEquals(
				"2B. The old index is filled with the contents of the next entry value.",
				wsEntry.getAttributeValues().get(2).getValue(), "Value 5");

		wsEntry.persistAttributeValue(new AttributeValue(l_wsAttr5, ""));

		assertEquals("3A. ", wsEntry.getAttributeValues().size(), 3);
		assertEquals(
				"3B. The old index is filled with the contents of the next entry value.",
				wsEntry.getAttributeValues().get(2).getValue(), "Value 6");

	}

	/**
	 * Testing the update of an entry
	 * 90: When adding an entry, not filling an attribute value still stores it	
	 */
	@Test
	public void test_persistEmptyEntryValue() {

		/* Initializing entry */
		AttributeValue l_av1 = new AttributeValue(wsAttribute, "");

		assertFalse("Persistence of empty values should return false",wsEntry.persistAttributeValue(l_av1));

	}

	/**
	 * In this test we try to remove the attribute alues of the attribute from
	 * the entry
	 */
	@Test
	public void testRemovingAttributesFromEntries() {
		WorkSheet l_workSheet = DBExelTestTools
				.fetchTestWorkSheetWithId("Remove Attribute from Entry");

		int l_refNrOfAttributes = l_workSheet.getAttributes().size();

		Attribute l_deletedAttribute = l_workSheet.getAttributes()
				.get(l_refNrOfAttributes - 1);

		Entry l_refEntry = l_workSheet.getEntries().get(
				l_workSheet.getEntries().size() - 1);

		assertThat(
				"That the attribute is among the attribute values of the test entry",
				l_refEntry.fetchSetAttributes().size(),
				is(equalTo(l_refNrOfAttributes)));

		l_refEntry.removeAttribute(l_deletedAttribute);

		assertThat(
				"There are still attribute values for the deleted attribute",
				l_refEntry.fetchSetAttributes().size(),
				is(lessThan(l_refNrOfAttributes)));

	}
}
