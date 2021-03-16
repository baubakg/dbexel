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

import java.util.Set;

import org.hamcrest.core.IsNull;
import org.junit.Test;

import dbexel.presentation.actions.AttributeTypes;
import dbexel.system.tools.DBExelTestTools;

public class TestAttribute {

	// We see if we can maintain a unique list of attributevalues
	// We enter the same attribute value twice we still should have one
	// attribute value
	@Test
	public void testAddAttrValue() {
		WorkSheet l_newWorkSheet = new WorkSheet("My WorkSheet");
		Entry l_entry = new Entry();
		l_newWorkSheet.addEntry(l_entry);
		Attribute l_attr = new Attribute("Col1");

		AttributeValue l_av1 = new AttributeValue(l_attr, "Val 1");
		AttributeValue l_av2 = new AttributeValue(l_attr, "Val 1");

		l_entry.attachAttributeValue(l_av1);
		l_entry.attachAttributeValue(l_av2);

		assertThat(l_attr.getAttrValues().size(), is(equalTo(1)));

	}

	@Test
	public void fetchEntriesUsingThisAttribute() {
		// Create test data
		WorkSheet l_ws = DBExelTestTools
				.fetchTestWorkSheetWithId("Attribute Test");

		Attribute l_attr = l_ws.getAttributes().get(0);

		// In this test all entries have the attribute
		assertThat(l_attr.fetchEntriesUsingThis().size(), is(equalTo(2)));

		l_ws.addEntry(new Entry());
		Entry l_entry = l_ws.getEntries().get(l_ws.getEntries().size() - 1);

		for (int i = 1; i < l_ws.getAttributes().size(); i++) {
			l_entry.attachAttributeValue(new AttributeValue(l_ws
					.getAttributes().get(i), "NewVal " + i));
		}

		assertThat(l_ws.getEntries().size(), is(equalTo(3)));

		assertThat(l_attr.fetchEntriesUsingThis().size(), is(equalTo(2)));

	}

	@Test
	public void fetchDistinctAttributeValues() {
		// Create test data
		WorkSheet l_ws = DBExelTestTools
				.fetchTestWorkSheetWithId("Attribute Test");

		Attribute l_attr = l_ws.getAttributes().get(0);

		Set<String> l_valueSet = l_attr.fetchStringValues();

		assertThat("The set should be filled", l_valueSet.size(),
				is(greaterThan(0)));

		for (AttributeValue lt_av : l_attr.getAttrValues()) {
			assertThat("Missing expected attribute value",
					l_valueSet.contains(lt_av.getValue()), is(equalTo(true)));
		}
	}

	// Create a persist attributeValue for the attributes.
	// It checks if an attribute is used. If not it removes it.
	@Test
	public void testPersistAttributeValue_ThatDoesNotHaveEntries() {
		WorkSheet l_ws = new WorkSheet("My WorkSheet");
		l_ws.attachAttribute(new Attribute("Column 1"));

		Attribute l_attribute = l_ws.getAttributes().get(0);

		l_ws.addEntry(new Entry());

		Entry l_entry = l_ws.getEntries().get(0);

		AttributeValue l_attributeValue1 = new AttributeValue(l_attribute,
				"Val 1");
		l_entry.attachAttributeValue(l_attributeValue1);

		AttributeValue l_attributeValue2 = new AttributeValue(l_attribute,
				"Val2");

		// Now add a new attribute value with the same characteristics as
		assertFalse(l_attribute.persistAttributeValue(l_attributeValue2));

		assertThat(
				"The entry should not contain this attribute value otherwise this test will not work",
				l_entry.getAttributeValues(), not(contains(l_attributeValue2)));

		assertThat(l_attributeValue2.getAttribute(), is(equalTo(null)));

		assertThat(l_attribute.getAttrValues().size(), is(equalTo(1)));
		assertThat(
				"The persisted attribute value should no longer be among the values of this attribute.",
				l_attribute.getAttrValues(), not(hasItem(l_attributeValue2)));

		assertThat(l_entry.getAttributeValues().size(), is(equalTo(1)));
	}

	// Create a persist attributeValue for the attributes.
	// It checks if an attribute is used. If not it removes it.
	@Test
	public void testPersistAttributeValue_ThatHasEntries() {
		WorkSheet l_ws = new WorkSheet("My WorkSheet");
		l_ws.attachAttribute(new Attribute("Column 1"));

		Attribute l_attribute = l_ws.getAttributes().get(0);

		l_ws.addEntry(new Entry());

		Entry l_entry1 = l_ws.getEntries().get(0);

		AttributeValue l_attributeValue1 = new AttributeValue(l_attribute,
				"Val 1");
		l_entry1.attachAttributeValue(l_attributeValue1);

		// Now add a new attribute value with the same characteristics as
		assertTrue(l_attribute.persistAttributeValue(l_attributeValue1));

		assertThat(l_attributeValue1.getAttribute(), is(not(equalTo(null))));

		assertThat(l_attribute.getAttrValues().size(), is(equalTo(1)));
		assertThat(
				"The persisted attribute value should still be among the values of this attribute.",
				l_attribute.getAttrValues(), hasItem(l_attributeValue1));

		assertThat(l_entry1.getAttributeValues().size(), is(equalTo(1)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPersistAttributeValue_Exception() {
		WorkSheet l_ws = new WorkSheet("My WorkSheet");
		l_ws.attachAttribute(new Attribute("Column 1"));
		l_ws.attachAttribute(new Attribute("Column 2"));

		Attribute l_attribute1 = l_ws.getAttributes().get(0);
		Attribute l_attribute2 = l_ws.getAttributes().get(1);

		l_ws.addEntry(new Entry());

		Entry l_entry1 = l_ws.getEntries().get(0);

		AttributeValue l_attributeValue1 = new AttributeValue(l_attribute1,
				"Val 1");
		l_entry1.attachAttributeValue(l_attributeValue1);

		// Now add a new attribute value with the same characteristics as
		assertTrue(l_attribute2.persistAttributeValue(l_attributeValue1));
	}

	// Test Fetch Attribute Value
	// Given a value it returns the attribute value using it
	@Test
	public void testFetchValue() {
		Attribute l_attr = new Attribute("a");
		AttributeValue l_av1 = new AttributeValue(l_attr, "One");
		l_attr.addAttrValue(l_av1);

		AttributeValue l_av2 = new AttributeValue(l_attr, "Two");
		l_attr.addAttrValue(l_av2);

		assertThat(l_attr.fetchAttributeValue(l_av1).getValue(),
				is(equalTo("One")));

		AttributeValue l_av3 = new AttributeValue();
		l_av3.setAttribute(l_attr);
		l_av3.setValue("Three");
		assertThat(l_attr.fetchAttributeValue(l_av3), is(equalTo(null)));
	}

	// Test Fetch Attribute Value
	// Given a value it returns the attribute value using it
	@Test(expected = IllegalArgumentException.class)
	public void testFetchValue_Exception1() {
		Attribute l_attr = new Attribute("a");

		l_attr.addAttrValue(new AttributeValue(l_attr, "One"));

		l_attr.addAttrValue(new AttributeValue(l_attr, "Two"));

		AttributeValue l_av3 = new AttributeValue();
		l_av3.setAttribute(l_attr);
		l_av3.setValue("");
		assertThat(l_attr.fetchAttributeValue(l_av3), is(equalTo(null)));
	}

	// Test Fetch Attribute Value
	// Given a value it returns the attribute value using it
	@Test(expected = IllegalArgumentException.class)
	public void testFetchValue_Exception2() {
		Attribute l_attr = new Attribute("a");

		l_attr.addAttrValue(new AttributeValue(l_attr, "One"));

		l_attr.addAttrValue(new AttributeValue(l_attr, "Two"));

		AttributeValue l_av3 = new AttributeValue();
		l_av3.setAttribute(l_attr);
		assertThat(l_attr.fetchAttributeValue(l_av3), is(equalTo(null)));

	}

	// Testing Unique attribute values
	@Test
	public void testUniqeAttributeValues_1() {
		Attribute l_attr = new Attribute("My Attr");

		AttributeValue l_av1 = new AttributeValue(l_attr, "one");
		l_attr.addAttrValue(l_av1);

		assertThat(l_attr.getAttrValues().size(), is(equalTo(1)));

		AttributeValue l_av2 = new AttributeValue(l_attr, "one");
		l_av2 = l_attr.addAttrValue(l_av2);

		assertThat(l_attr.getAttrValues().size(), is(equalTo(1)));

		assertTrue(l_av1 == l_av2);
	}

	// Testing Unique attribute values
	@Test
	public void testUniqeAttributeValues_2() {
		WorkSheet l_newWorkSheet = new WorkSheet("My WorkSheet");

		Attribute l_attr = new Attribute("Column 1");
		Entry l_entry1 = new Entry();
		Entry l_entry2 = new Entry();

		l_newWorkSheet.attachAttribute(l_attr);

		l_newWorkSheet.addEntry(l_entry1);
		l_newWorkSheet.addEntry(l_entry2);

		AttributeValue l_av1 = new AttributeValue(l_attr, "Val 1");
		l_entry1.attachAttributeValue(l_av1);

		assertThat(l_attr.getAttrValues().size(), is(equalTo(1)));

		l_entry2.attachAttributeValue(new AttributeValue(l_attr, "Val 1"));
		assertTrue(l_entry1.getAttributeValues().get(0) == l_entry2.getAttributeValues().get(0));
		assertThat(l_attr.getAttrValues().size(), is(equalTo(1)));

	}
}
