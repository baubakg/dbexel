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
package dbexel.model.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

import dbexel.model.mapping.AttributeValue;
import dbexel.model.mapping.WorkSheet;
import dbexel.model.mapping.Attribute;
import dbexel.model.mapping.Entry;

public class TestWorkSheetEntryDao {

	WorkSheet ws;
	Entry wsEntry;
	private Attribute wsAttribute;
	
	private WorkSheetEntryDao entryDao;
	
	@Before
	public void prepare() {
		JPATestTools.cleanAllData();
		
		ws = new WorkSheet("STD Worksheet");
		wsEntry = new Entry();

		wsAttribute = new Attribute("Column 1");

		// Attribute
		ws.attachAttribute(wsAttribute);
		WorkSheetDao l_wsDao= new WorkSheetDaoImpl();
		l_wsDao.createWorkSheet(ws);
		
		entryDao = new WorkSheetEntryDaoImpl();
	}

	/**
	 * This method tests that we can correctly create a work sheet entry
	 */
	@Test
	public void testCreateEntry() {
		Entry l_wsEntry = new Entry();

		// attach Entry to worksheet
		ws.addEntry(l_wsEntry);
		assertEquals("1. The entryId should not be set yet", 0, l_wsEntry.getEntryId().intValue());
		entryDao.createWorkSheetEntry(l_wsEntry);
		assertTrue("2. The entryId should be set after persistence", l_wsEntry.getEntryId().intValue() > 0);
		
		// l_wsEntry.getWorksheet().getWs_Name());
	}


	/**
	 * Testing the addition of an AttributeValue
	 */
	@Test
	public void testAttachAttributeValue() {
		AttributeValue l_av = new AttributeValue(wsAttribute,
				"My BloddyValentine");
		
		ws.addEntry(wsEntry);
		wsEntry.attachAttributeValue(l_av);
		
		entryDao.createWorkSheetEntry(wsEntry);
		
		Entry l_storedWSEntry = entryDao.fetchWorkSheetEntry(wsEntry.getEntryId());
		
		assertEquals("1A. See that we correctly added the Attribute Value", 1,
				l_storedWSEntry.getAttributeValues().size());
		assertEquals("1B. See that we correctly added the Attribute Value",
				"My BloddyValentine", l_storedWSEntry.findValueByAttribute(wsAttribute)
						.getValue());
		assertEquals("1C. See that we correctly added the Attribute Value",
				"Column 1", l_storedWSEntry.findValueByAttribute(wsAttribute)
						.getAttribute().getAttrName());
	}

	/**
	 * Testing the addition of an AttributeValue Part 2
	 */
	@Test
	public void testUpdateEntry() {
		ws.addEntry(wsEntry);

		AttributeValue l_av = new AttributeValue(wsAttribute,
				"My Bloddy Valentine");

		wsEntry.attachAttributeValue(l_av);

		WorkSheetDao wsDao = new WorkSheetDaoImpl();
		wsDao.updateWorkSheet(ws);
		
		Entry l_storedWSEntry = entryDao.fetchWorkSheetEntry(wsEntry.getEntryId());

		assertEquals("1A. See that we correctly added the Attribute Value", 1,
				l_storedWSEntry.getAttributeValues().size());
		assertEquals("1B. See that we correctly added the Attribute Value",
				"My Bloddy Valentine", l_storedWSEntry
						.findValueByAttribute(wsAttribute).getValue());
		assertEquals("1C. See that we correctly added the Attribute Value",
				"Column 1", l_storedWSEntry.findValueByAttribute(wsAttribute)
						.getAttribute().getAttrName());

		// Testing that the attribute are correctly replaced
		AttributeValue l_av2 = new AttributeValue(wsAttribute, "Ashes to Ashes");

		// Here the attribute value should just replace the old value because
		// the attribute already exists
		wsEntry.attachAttributeValue(l_av2);
		
		entryDao.updateWorkSheet(wsEntry);
		l_storedWSEntry = entryDao.fetchWorkSheetEntry(wsEntry.getEntryId());
		
		assertEquals("2A. See that we correctly added the Attribute Value", 1,
				l_storedWSEntry.getAttributeValues().size());
		assertEquals("2B. See that we correctly added the Attribute Value",
				"Ashes to Ashes", l_storedWSEntry.findValueByAttribute(wsAttribute)
						.getValue());
		assertEquals("2C. See that we correctly added the Attribute Value",
				"Column 1", l_storedWSEntry.findValueByAttribute(wsAttribute)
						.getAttribute().getAttrName());
		
		//We should not add an empty attribute value to the entry
		
		/** Should we implment this? **/
		WorkSheetDao l_wsDao= new WorkSheetDaoImpl();
		l_wsDao.deleteWorkSheet(ws);
		/*
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ENTRY));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET_ATTRIBUTES));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTE));
		*/
	}

	/**
	 * Testing the getAttributeValues(WorkSheet) It returns the
	 * attribute values of an entry in regards to a Worksheet
	 */
	@Test
	public void test_getworkSheetAttributeValues() {
		Attribute l_wsAttr2 = new Attribute("Column 2");
		Attribute l_wsAttr3 = new Attribute("Column 3");
		Attribute l_wsAttr4 = new Attribute("Column 4");
		//Attribute l_wsAttr5 = new Attribute("Column 5");
		//Attribute l_wsAttr6 = new Attribute("Column 6");

		ws.attachAttribute(l_wsAttr2);
		ws.attachAttribute(l_wsAttr3);
		ws.attachAttribute(l_wsAttr4);
		
		ws.addEntry(wsEntry);	
		
		wsEntry.attachAttributeValue(new AttributeValue(wsAttribute, "Value 1"));
		wsEntry.attachAttributeValue(new AttributeValue(l_wsAttr3, "Value 3"));
		wsEntry.attachAttributeValue(new AttributeValue(l_wsAttr4, "Value 4"));
		//wsEntry.attachAttributeValue(new AttributeValue(l_wsAttr5, "Value 5"));
		//wsEntry.attachAttributeValue(new AttributeValue(l_wsAttr6, "Value 6"));

		//entryDao.createWorkSheetEntry(wsEntry);
		WorkSheetDao l_wsDao = new WorkSheetDaoImpl();
		l_wsDao.updateWorkSheet(ws);
		
		Entry l_storedWSEntry = entryDao.fetchWorkSheetEntry(wsEntry.getEntryId());

		List<AttributeValue> l_wsAttrValues = l_storedWSEntry
				.fetchAttributeValues();

		assertEquals("1. We did not get the expected number of attribute values", 4, l_wsAttrValues.size());
		
		assertEquals("2. We do not have the expected number of attached attributes", 4, l_wsDao.fetchWorkSheet(ws.getWs_Id()).getAttributes().size());
		

	}
	
	/**
	 * Testing the update of an entry
	 */
	@Test
	public void test_updateEntryValues() {
		
		/* Initializing entry */
		Attribute l_wsAttr1 = new Attribute("Column 3");
		//l_wsAttr1.setAttrId(new Long(8));
		Attribute l_wsAttr2 = new Attribute("Column 5");
		//l_wsAttr2.setAttrId(new Long(9));
		Attribute l_wsAttr3 = new Attribute("Column 7");
		//l_wsAttr3.setAttrId(new Long(23));
		Attribute l_wsAttr4 = new Attribute("Column 11");
		//l_wsAttr4.setAttrId(new Long(27));
		Attribute l_wsAttr5 = new Attribute("Column 13");
		//l_wsAttr5.setAttrId(new Long(31));
		//Attribute l_wsAttr6 = new Attribute("Column 6");
		//l_wsAttr6.setAttrId(new Long(37));

		ws.attachAttribute(l_wsAttr1);
		ws.attachAttribute(l_wsAttr2);
		ws.attachAttribute(l_wsAttr3);
		ws.attachAttribute(l_wsAttr4);
		ws.attachAttribute(l_wsAttr5);
		
		//we need to persist the worksheet
		WorkSheetDao l_wsDao = new WorkSheetDaoImpl();
		l_wsDao.updateWorkSheet(ws);
	
		//attach entry to worksheet
		ws.addEntry(wsEntry);
		
		wsEntry.attachAttributeValue(new AttributeValue(wsAttribute, "Value 1"));
		wsEntry.attachAttributeValue(new AttributeValue(l_wsAttr3, "Value 3"));
		wsEntry.attachAttributeValue(new AttributeValue(l_wsAttr4, "Value 4"));
		wsEntry.attachAttributeValue(new AttributeValue(l_wsAttr5, "Value 5"));
		//wsEntry.attachAttributeValue(new AttributeValue(l_wsAttr6, "Value 6"));
		
		entryDao.createWorkSheetEntry(wsEntry);
		
		/* Creating a form list */
		Map<Long, String> in_formEntryforUpdate = new HashMap<Long, String>();

		in_formEntryforUpdate.put(l_wsAttr5.getAttrId(), "This");
		in_formEntryforUpdate.put(l_wsAttr4.getAttrId(), "is");
		in_formEntryforUpdate.put(l_wsAttr3.getAttrId(), "a");
		in_formEntryforUpdate.put(l_wsAttr2.getAttrId(), "test");
		in_formEntryforUpdate.put(l_wsAttr1.getAttrId(), "entry");
		
		wsEntry.updateValues(in_formEntryforUpdate);

		Entry l_updatedWSEntry = entryDao.fetchWorkSheetEntry(wsEntry.getEntryId());

		
		int i = 1;
		for (AttributeValue lt_av : l_updatedWSEntry.fetchAttributeValues()) {
			if (in_formEntryforUpdate.containsKey(lt_av.getAttribute()
					.getAttrId()))
				assertEquals(++i + ". ", in_formEntryforUpdate.get(lt_av
						.getAttribute().getAttrId()), lt_av.getValue());
		}

		JPATestTools.cleanAllData();
	}
	
	//Testing the deletion of a worksheet entry
	@Test
	public void testDeleteEntry1() {
		ws.addEntry(wsEntry);
		
		assertEquals("1.a. The entry is in an unexpected state", 0, wsEntry.getEntryId().intValue());
		WorkSheetDao l_wsDao = new WorkSheetDaoImpl();
		l_wsDao.updateWorkSheet(ws);
		assertTrue("1.b. The entry is in an unexpected state", wsEntry.getEntryId().intValue() > 0);
		//We make our initial assertions
		assertEquals("1.c. ", 1, l_wsDao.fetchWorkSheet(ws.getWs_Id()).getEntries().size());
		

		ws.getEntries().remove(wsEntry);
		
		//Now start tests
		entryDao.deleteWorkSheetEntry(wsEntry);
		
		assertEquals("2. ", 0, l_wsDao.fetchWorkSheet(ws.getWs_Id()).getEntries().size());
	}
	
	//Testing the deletion of a worksheet entry with attribute Values
	@Test
	public void testDeleteEntry2() {
		ws.addEntry(wsEntry);
		wsEntry.attachAttributeValue(new AttributeValue(wsAttribute, "blooop"));
		
		
		assertEquals("1.a. The entry is in an unexpected state", 0, wsEntry.getEntryId().intValue());
		WorkSheetDao l_wsDao = new WorkSheetDaoImpl();
		l_wsDao.updateWorkSheet(ws);
		assertTrue("1.b. The entry is in an unexpected state", wsEntry.getEntryId().intValue() > 0);
		//We make our initial assertions
		assertEquals("1.c. ", 1, l_wsDao.fetchWorkSheet(ws.getWs_Id()).getEntries().size());
		
		//Now start tests
		entryDao.deleteWorkSheetEntry(wsEntry);
		
		assertEquals("2. ", 0, l_wsDao.fetchWorkSheet(ws.getWs_Id()).getEntries().size());
	/*	
		assertEquals(1, JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ENTRY));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ENTRY_VALUES));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTEVALUE));
		assertEquals(1, JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET_ATTRIBUTES));
		assertEquals(1, JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTE));
	*/	
		l_wsDao.deleteWorkSheet(ws);
	/*	
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ENTRY));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ENTRY_VALUES));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTEVALUE));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET_ATTRIBUTES));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTE));
*/
		
	}
	
	// javax.persistence.PersistenceException: org.hibernate.exception.ConstraintViolationException: could not delete: [dbexel.model.mapping.WorkSheet#1]
	// Caused by: org.hibernate.exception.ConstraintViolationException: could not delete: [dbexel.model.mapping.WorkSheet#1]
	// Caused by: java.sql.SQLIntegrityConstraintViolationException: DELETE on table 'WORKSHEET' caused a violation of foreign key constraint 'FK3F110522BA0100' for key (1).  The statement has been rolled back.
	// Caused by: org.apache.derby.client.am.SqlException: DELETE on table 'WORKSHEET' caused a violation of foreign key constraint 'FK3F110522BA0100' for key (1).  The statement has been rolled back.
	@Test
	public void testDeletionBug() {
		WorkSheet l_workSheet = fetchTestWorkSheet("testAddAnEntry WorkSheet");

		WorkSheetDao l_wsDao = new WorkSheetDaoImpl();
		l_wsDao.createWorkSheet(l_workSheet);
		
		WorkSheet l_storedWorkSheet = l_wsDao.fetchWorkSheet(l_workSheet.getWs_Id());
		
		assertEquals("testAddAnEntry WorkSheet", l_storedWorkSheet.getWs_Name());
		
		
		//checking before entry addition
		assertEquals("The initial status will not let the test to conclude",2, l_storedWorkSheet.getEntries().size());
		
		l_storedWorkSheet.addEntry(new Entry());
		Entry l_newEntry = l_storedWorkSheet.getEntries().get(l_storedWorkSheet.getEntries().size()-1);
		
		int i = 0;
		for (Attribute lt_attribute : l_storedWorkSheet.getAttributes()) {
			l_newEntry.attachAttributeValue(new AttributeValue(lt_attribute, Integer.toString(i)));
			i+=10;
		}
		
		l_wsDao.updateWorkSheet(l_storedWorkSheet);
		
		assertEquals("The entry was not correctly added",3, l_storedWorkSheet.getEntries().size());
		
		//Start deleting the worksheet
		l_wsDao.deleteWorkSheet(l_storedWorkSheet);
		
		WorkSheet l_deletedWorkSheet = l_wsDao.fetchWorkSheet(l_workSheet.getWs_Id());
		
		assertEquals(null, l_deletedWorkSheet);
		
		WorkSheet l_newWorkSheet = fetchTestWorkSheet("testAddAnEntry WorkSheet");
		l_wsDao.createWorkSheet(l_newWorkSheet);
		
		assertEquals("The new object does not have the expected entries",2, l_newWorkSheet.getEntries().size());
		
		l_wsDao.deleteWorkSheet(l_newWorkSheet);
		/*
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ENTRY));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET_ATTRIBUTES));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTE));
		*/
	}
	
	
	private WorkSheet fetchTestWorkSheet(String in_string) {
		WorkSheet l_workSheet = new WorkSheet(in_string);
		// l_workSheet.setWs_Id(new Long(17));

		Attribute l_wsAttr1 = new Attribute("Column 3");
		// l_wsAttr1.setAttrId(new Long(8));
		Attribute l_wsAttr2 = new Attribute("Column 5");
		// l_wsAttr2.setAttrId(new Long(9));
		Attribute l_wsAttr3 = new Attribute("Column 7");
		// l_wsAttr3.setAttrId(new Long(23));
		Attribute l_wsAttr4 = new Attribute("Column 11");
		// l_wsAttr4.setAttrId(new Long(27));
		Attribute l_wsAttr5 = new Attribute("Column 13");
		// l_wsAttr5.setAttrId(new Long(31));

		// Add attributes
		l_workSheet.attachAttribute(l_wsAttr1);
		l_workSheet.attachAttribute(l_wsAttr2);
		l_workSheet.attachAttribute(l_wsAttr3);
		l_workSheet.attachAttribute(l_wsAttr4);
		l_workSheet.attachAttribute(l_wsAttr5);

		// Add Antries
		l_workSheet.addEntry(new Entry());
		l_workSheet.addEntry(new Entry());

		// l_workSheet.getEntries().get(0).setEntryId(new Long(37));
		// l_workSheet.getEntries().get(1).setEntryId(new Long(41));

		// Fill the entries
		l_workSheet
				.getEntries()
				.get(0)
				.attachAttributeValue(
						new AttributeValue(l_wsAttr1, "Attr1 valA"));
		l_workSheet
				.getEntries()
				.get(0)
				.attachAttributeValue(
						new AttributeValue(l_wsAttr2, "Attr2 valB"));
		l_workSheet
				.getEntries()
				.get(0)
				.attachAttributeValue(
						new AttributeValue(l_wsAttr3, "Attr3 valC"));
		l_workSheet
				.getEntries()
				.get(0)
				.attachAttributeValue(
						new AttributeValue(l_wsAttr4, "Attr4 valD"));
		l_workSheet
				.getEntries()
				.get(0)
				.attachAttributeValue(
						new AttributeValue(l_wsAttr5, "Attr5 valE"));

		l_workSheet
				.getEntries()
				.get(1)
				.attachAttributeValue(
						new AttributeValue(l_wsAttr1, "Attr1 valZ"));
		l_workSheet
				.getEntries()
				.get(1)
				.attachAttributeValue(
						new AttributeValue(l_wsAttr2, "Attr2 valY"));
		l_workSheet
				.getEntries()
				.get(1)
				.attachAttributeValue(
						new AttributeValue(l_wsAttr3, "Attr3 valX"));
		l_workSheet
				.getEntries()
				.get(1)
				.attachAttributeValue(
						new AttributeValue(l_wsAttr4, "Attr4 valW"));
		l_workSheet
				.getEntries()
				.get(1)
				.attachAttributeValue(
						new AttributeValue(l_wsAttr5, "Attr5 valV"));

		return l_workSheet;
	}

}
