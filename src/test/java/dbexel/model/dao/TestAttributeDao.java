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

import static org.hamcrest.Matchers.*;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import dbexel.model.mapping.AttributeValue;
import dbexel.model.mapping.DBEXEL_TABLES;
import dbexel.model.mapping.WorkSheet;
import dbexel.model.mapping.Attribute;
import dbexel.model.mapping.Entry;
import dbexel.system.tools.DBExelTestTools;

/**
 * @author gandomi
 * 
 */
public class TestAttributeDao {
	@BeforeClass
	public static void init() {
		JPATestTools.cleanAllData();
	}

	@After
	public void close() {
		JPATestTools.cleanAllData();
	}

	// Test update attribute
	/*
	 * @Test public void testUpdateAttribute() { WorkSheet l_newWorkSheet = new
	 * WorkSheet("My WorkSheet"); Attribute l_attribute = new
	 * Attribute("Column 1");
	 * l_newWorkSheet.attachAttribute(l_attribute);
	 * 
	 * l_newWorkSheet.addEntry(new Entry());
	 * 
	 * l_newWorkSheet.getEntries().get(0) .attachAttributeValue(new
	 * AttributeValue(l_attribute, "Blaa"));
	 * 
	 * assertNull(l_attribute.getAttrId()); WorkSheetDao l_wsDao = new
	 * WorkSheetDaoImpl(); l_wsDao.createWorkSheet(l_newWorkSheet);
	 * 
	 * assertNotNull(l_attribute);
	 * 
	 * l_attribute.setAttrName("Column 2");
	 * 
	 * l_wsDao.updateWorkSheet(l_newWorkSheet);
	 * 
	 * assertEquals("Column 2", l_attribute.getAttrName());
	 * 
	 * assertEquals(1, JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTE,
	 * "where ATTR_NAME='Column 2'")); assertEquals(0,
	 * JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTE,
	 * "where ATTR_NAME='Column 1'"));
	 * 
	 * }
	 */

	// Test delete Attribute with entry
	@Test
	public void testDeleteAttribute() {
		WorkSheet l_newWorkSheet = new WorkSheet("My WorkSheet");
		Attribute l_attribute = new Attribute("Column 1");
		l_newWorkSheet.attachAttribute(l_attribute);

		l_newWorkSheet.addEntry(new Entry());

		l_newWorkSheet.getEntries().get(0)
				.attachAttributeValue(new AttributeValue(l_attribute, "Blaa"));

		WorkSheetDao l_wsDao = new WorkSheetDaoImpl();
		l_wsDao.createWorkSheet(l_newWorkSheet);

		assertEquals(1, JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET));
		assertEquals(1, JPATestTools.fetchTableSize(DBEXEL_TABLES.ENTRY));
		assertEquals(1,
				JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET_ATTRIBUTES));
		assertEquals(1, JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTE));
		assertEquals(1,
				JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTEVALUE));
		// assertEquals(1,
		// JPATestTools.fetchTableSize(DBEXEL_TABLES.ENTRY_VALUES));

		AttributeDao l_attributeDao = new AttributeDaoImpl();

		l_attributeDao.deleteAttribute(l_attribute);

		assertEquals(1, JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET));
		assertEquals(1, JPATestTools.fetchTableSize(DBEXEL_TABLES.ENTRY));
		assertEquals(0,
				JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET_ATTRIBUTES));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTE));
		assertEquals(0,
				JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTEVALUE));
		// assertEquals(0,
		// JPATestTools.fetchTableSize(DBEXEL_TABLES.ENTRY_VALUES));

	}

	/** This method tests a more complex creation of a worksheet */
	// @Test
	public void testDeletingUnattachedAttribute() {
		WorkSheetDao wsDao = new WorkSheetDaoImpl();
		WorkSheet ws = new WorkSheet(
				"testDeletingUnattachedAttribute Work Sheet");
		Attribute l_attrib1 = new Attribute(
				"My Attribute Nr 1");
		ws.attachAttribute(l_attrib1);

		Attribute l_myAttribute = new Attribute(
				"Second Column");

		ws.attachAttribute(l_myAttribute);

		assertEquals("1. ", 2, ws.getAttributes().size());
		// Test the addition of a worksheet attribute
		wsDao.createWorkSheet(ws);

		WorkSheet l_insertedWS = wsDao.fetchWorkSheet(ws.getWs_Id());

		assertEquals("2. ", 2, l_insertedWS.getAttributes().size());

		// Test the deletion of a worksheet attribute
		ws.getAttributes().remove(l_myAttribute);
		wsDao.updateWorkSheet(ws);
		// l_insertedWS = null;
		// l_insertedWS = wsDao.fetchWorkSheet(ws.getWs_Id());

		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.WORKSHEET);
		JPATestTools.assertRowNumbers(0, DBEXEL_TABLES.ENTRY);
		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.WORKSHEET_ATTRIBUTES);
		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.ATTRIBUTE);
		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.ATTRIBUTEVALUE);

		assertEquals("3A. The attribute was not properly removed", 1,
				l_insertedWS.getAttributes().size());

		assertEquals("3B. The attribute was not properly removed",
				"My Attribute Nr 1", wsDao.fetchWorkSheet(ws.getWs_Id())
						.getAttributes().get(0).getAttrName());

		wsDao.deleteWorkSheet(ws);

		JPATestTools.assertRowNumbers(0, DBEXEL_TABLES.WORKSHEET);
		JPATestTools.assertRowNumbers(0, DBEXEL_TABLES.ENTRY);
		JPATestTools.assertRowNumbers(0, DBEXEL_TABLES.WORKSHEET_ATTRIBUTES);
		JPATestTools.assertRowNumbers(0, DBEXEL_TABLES.ATTRIBUTE);
		JPATestTools.assertRowNumbers(0, DBEXEL_TABLES.ATTRIBUTEVALUE);

	}

	@Test
	public void testFetchAttribute() {
		// Prepare tests
		WorkSheetDao wsDao = new WorkSheetDaoImpl();
		WorkSheet l_workSheet = DBExelTestTools
				.fetchTestWorkSheet("Attribute WorkSheet");
		wsDao.createWorkSheet(l_workSheet);

		Attribute l_attributeInSystem = l_workSheet.getAttributes()
				.get(0);

		// Start tests
		AttributeDao l_attrDao = new AttributeDaoImpl();
		Attribute l_newAttribute = l_attrDao
				.fetchAttribute(l_attributeInSystem.getAttrId());
		assertNotNull("1. We should have found a new attribute", l_newAttribute);

		assertEquals(l_attributeInSystem, l_newAttribute);

	}

	@Test
	public void testUpdateAttribute() {
		// Prepare tests
		WorkSheetDao wsDao = new WorkSheetDaoImpl();
		WorkSheet l_workSheet = DBExelTestTools
				.fetchTestWorkSheet("Attribute WorkSheet");
		wsDao.createWorkSheet(l_workSheet);

		Attribute l_attributeInSystem = l_workSheet.getAttributes()
				.get(0);

		// Start tests
		AttributeDao l_attrDao = new AttributeDaoImpl();
		String l_oldName = l_attributeInSystem.getAttrName();

		l_attributeInSystem.setAttrName("New Name");

		l_attrDao.updateAttribute(l_attributeInSystem);

		// assertNotSame(l_oldName, l_attributeInSystem.getAttrName());

		String l_newAttributeName = JPATestTools.fetchTableData("ATTR_NAME",
				DBEXEL_TABLES.ATTRIBUTE.toString() + " where ATTR_ID="
						+ l_attributeInSystem.getAttrId());

		assertThat(l_oldName, is(not(equalTo(l_newAttributeName))));
	}
	
	
	@Test
	public void testDuplicateAttribute() {
		// Prepare tests
		WorkSheetDao wsDao = new WorkSheetDaoImpl();
		WorkSheet l_workSheet = new WorkSheet("Double Attribute Test");
		l_workSheet.attachAttribute(new Attribute("Attr1"));
		
		assertEquals("First object level check of the number of attached attributes",1, l_workSheet.getAttributes().size());
		
		wsDao.createWorkSheet(l_workSheet);

		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.ATTRIBUTE);
		
		l_workSheet.attachAttribute(new Attribute("Attr1"));
		
		assertEquals("Second object level check of the number of attached attributes", 1, l_workSheet.getAttributes().size());
		
		wsDao.updateWorkSheet(l_workSheet);
		
		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.ATTRIBUTE);
	}
}
