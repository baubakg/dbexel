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

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import dbexel.model.mapping.AttributeValue;
import dbexel.model.mapping.DBEXEL_TABLES;
import dbexel.model.mapping.WorkSheet;
import dbexel.model.mapping.Attribute;
import dbexel.model.mapping.Entry;
import dbexel.presentation.actions.AttributeTypes;

public class WorkSheetDaoTest {

	private WorkSheet ws;
	private WorkSheetDao wsDao = new WorkSheetDaoImpl();

	@Before
	public void init() {
		ws = new WorkSheet("MyWorkSheet 1");
		JPATestTools.cleanAllData();
	}

	@AfterClass
	public static void close() {

		JPATestTools.cleanAllData();
	}

	/* This method tests the findAllWorkSheets method */
	@Test
	public void testFindAll() {
		wsDao.createWorkSheet(ws);
		List<WorkSheet> l_wsList = wsDao.fetchAllWorkSheets();

		assertEquals("1. ", 1, l_wsList.size());

		WorkSheet l_wsAdd1 = new WorkSheet("My WorkSheet 2");
		// l_wsAdd1.setWs_Id(Long.valueOf(17));
		WorkSheet l_wsAdd2 = new WorkSheet("My WorkSheet 3");

		wsDao.createWorkSheet(l_wsAdd1);
		wsDao.createWorkSheet(l_wsAdd2);

		l_wsList = wsDao.fetchAllWorkSheets();

		assertEquals("2. ", 3, l_wsList.size());

	}

	/* This method tests the deleteAll method */
	@Test
	public void testDeleteTwoWorkSheets() {

		List<WorkSheet> l_wsList = wsDao.fetchAllWorkSheets();
		assertEquals("1. ", 0, l_wsList.size());

		WorkSheet l_wsAdd1 = new WorkSheet("My WorkSheet 2");
		WorkSheet l_wsAdd2 = new WorkSheet("My WorkSheet 3");

		// We add two worksheets to see that we surely fill the database
		wsDao.createWorkSheet(l_wsAdd1);
		wsDao.createWorkSheet(l_wsAdd2);

		wsDao.deleteWorkSheet(l_wsAdd1);
		wsDao.deleteWorkSheet(l_wsAdd2);

		l_wsList = wsDao.fetchAllWorkSheets();

		assertEquals("2. ", 0, l_wsList.size());

		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ENTRY));
		assertEquals(0,
				JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET_ATTRIBUTES));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTE));
		assertEquals(0,
				JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTEVALUE));

	}

	/*
	 * This method tests the delete method Testing delete with work sheet having
	 * an attribute
	 * 
	 * If the attribute is not attached to any other worksheet, it should be
	 * deleted as well
	 */
	@Test
	public void testDeleteWithAttribute() {
		WorkSheet l_wsAdd1 = new WorkSheet("My WorkSheet 2");
		WorkSheet l_wsAdd2 = new WorkSheet("My WorkSheet 3");
		l_wsAdd1.attachAttribute(new Attribute("My Attribute"));

		// We add two worksheets to see that we surely fill the database
		wsDao.createWorkSheet(l_wsAdd1);
		wsDao.createWorkSheet(l_wsAdd2);

		assertEquals(2, JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ENTRY));

		assertEquals(1,
				JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET_ATTRIBUTES));

		assertEquals(1, JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTE));

		wsDao.deleteWorkSheet(l_wsAdd1);

		List<WorkSheet> l_wsList = wsDao.fetchAllWorkSheets();
		assertEquals("3. ", 1, l_wsList.size());
		assertEquals(1, JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ENTRY));
		assertEquals(0,
				JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET_ATTRIBUTES));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTE));
		assertEquals(0,
				JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTEVALUE));

	}

	// Testing a more complex delete
	@Test
	public void testDeleteWithEntry() {
		ws.addEntry(new Entry());
		wsDao.createWorkSheet(ws);

		// we test that our entry data is correctly there
		List<WorkSheet> l_wsList = wsDao.fetchAllWorkSheets();
		assertEquals("1.a. ", 1, l_wsList.size());
		assertEquals("1.b. ", 1, l_wsList.get(0).getEntries().size());

		// We now attempt at deleting the worksheet
		wsDao.deleteWorkSheet(ws);

		// we test that our entry data is correctly removed
		l_wsList = wsDao.fetchAllWorkSheets();
		assertEquals("2.a. ", 0, l_wsList.size());
		// assertEquals("2.b. ", 0, l_wsList.get(0).getEntries().size());

		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ENTRY));
		assertEquals(0,
				JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET_ATTRIBUTES));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTE));
		assertEquals(0,
				JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTEVALUE));

	}

	/*
	 * Testing delete on a real worksheet with both entries and attributes +
	 * attribute values
	 */
	@Test
	public void testDeleteFilledWorksheet() {
		ws.addEntry(new Entry());

		ws.attachAttribute(new Attribute("My Attribute"));

		ws.getEntries()
				.get(0)
				.attachAttributeValue(
						new AttributeValue(ws.getAttributes().get(0),
								"My Value"));
		wsDao.createWorkSheet(ws);

		// we test that our entry data is correctly there
		List<WorkSheet> l_wsList = wsDao.fetchAllWorkSheets();
		assertEquals("1.a. ", 1, l_wsList.size());
		assertEquals("1.b. ", 1, l_wsList.get(0).getEntries().size());
		assertEquals("1.c. ", 1, l_wsList.get(0).getAttributes().size());

		// We now attempt at deleting the worksheet
		wsDao.deleteWorkSheet(ws);

		// we test that our entry data is correctly removed
		l_wsList = wsDao.fetchAllWorkSheets();
		assertEquals("2.a. ", 0, l_wsList.size());

		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ENTRY));
		assertEquals(0,
				JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET_ATTRIBUTES));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTE));
		assertEquals(0,
				JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTEVALUE));

	}

	/** This method tests that creating an object using the Dao works correctly */
	@Test
	public void testCreate() {
		assertEquals(
				"1. The id of the work sheet should not be instantiated yet.",
				Long.valueOf(0), ws.getWs_Id());
		wsDao.createWorkSheet(ws);
		assertTrue("2. The id of the WorkSheet should by now be instantiated ",
				!Long.valueOf(0).equals(ws.getWs_Id()));
		assertEquals(
				"3. At this state the worksheet should have no attributes", 0,
				ws.getAttributes().size());
		assertEquals("4. At this state the worksheet should have no entries",
				0, ws.getEntries().size());
	}

	/** This method tests a more complex creation of a worksheet */
	@Test
	public void testCreateWithAttribute() {
		Attribute l_attrib1 = new Attribute(
				"My Attribute Nr 1");

		l_attrib1.setType(AttributeTypes.Text);

		ws.attachAttribute(l_attrib1);

		Attribute l_myAttribute = new Attribute(
				"Second Column");

		l_myAttribute.setType(AttributeTypes.Number);

		ws.attachAttribute(l_myAttribute);

		Entry l_wsEntry = new Entry();

		ws.addEntry(l_wsEntry);

		l_wsEntry.attachAttributeValue(new AttributeValue(l_attrib1, "A"));

		wsDao.createWorkSheet(ws);

		assertEquals(1, wsDao.fetchAllWorkSheets().size());

		assertThat("The attributes were not correctly stored", wsDao
				.fetchWorkSheet(ws.getWs_Id()).getAttributes().size(),
				is(equalTo(2)));

		assertThat("Wrong attribute types were stored",
				wsDao.fetchWorkSheet(ws.getWs_Id()).getAttributes().get(0)
						.getType(), is(equalTo(AttributeTypes.Text)));

		assertThat("Wrong attribute types were stored",
				wsDao.fetchWorkSheet(ws.getWs_Id()).getAttributes().get(1)
						.getType(), is(equalTo(AttributeTypes.Number)));
		
		//wsDao.deleteWorkSheet(ws);

	}

	/** This method tests that finding an object given its id works fine */
	@Test
	public void testFindById() {
		WorkSheet l_wsAdd1 = new WorkSheet("My WorkSheet 2");
		// l_wsAdd1.setWs_Id(Long.valueOf(17));
		WorkSheet l_wsAdd2 = new WorkSheet("My WorkSheet 3");
		// l_wsAdd2.setWs_Id(Long.valueOf(13));

		// we make sure that we have a pool of worksheets
		wsDao.createWorkSheet(ws);
		wsDao.createWorkSheet(l_wsAdd1);
		wsDao.createWorkSheet(l_wsAdd2);

		// now let us fetch the work sheet 17
		WorkSheet l_wsFetched1 = wsDao.fetchWorkSheet(l_wsAdd1.getWs_Id());
		assertNotNull("1A. We could not find the correct worksheet",
				l_wsFetched1);
		assertEquals("1B. We did not fetch the correct worksheet",
				l_wsAdd1.getWs_Name(), l_wsFetched1.getWs_Name());

	}

	/** Testing the update method */
	@Test
	public void testUpdateWorkSheet() {
		WorkSheet l_wsAdd1 = new WorkSheet("My WorkSheet 2");
		// l_wsAdd1.setWs_Id(Long.valueOf(17));
		WorkSheet l_wsAdd2 = new WorkSheet("My WorkSheet 3");
		// l_wsAdd2.setWs_Id(Long.valueOf(13));

		// we make sure that we have a pool of worksheets
		wsDao.createWorkSheet(ws);

		assertEquals("1. The to-be-inserted worksheet should have a 0 id",
				Long.valueOf(0), l_wsAdd1.getWs_Id());

		wsDao.createWorkSheet(l_wsAdd1);
		wsDao.createWorkSheet(l_wsAdd2);

		// now let us update the worksheet nr2
		assertTrue("2. The inserted worksheet did not get its id updated",
				Long.valueOf(0) < l_wsAdd1.getWs_Id());

		l_wsAdd1.setWs_Name("My Worksheet 2X2");
		wsDao.updateWorkSheet(l_wsAdd1);

		WorkSheet l_updatedWS = wsDao.fetchWorkSheet(l_wsAdd1.getWs_Id());

		assertEquals("3. ", "My Worksheet 2X2", l_updatedWS.getWs_Name());

		assertEquals(3, JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ENTRY));
		assertEquals(0,
				JPATestTools.fetchTableSize(DBEXEL_TABLES.WORKSHEET_ATTRIBUTES));
		assertEquals(0, JPATestTools.fetchTableSize(DBEXEL_TABLES.ATTRIBUTE));

	}

	// In this case we create a new entry. We also add a new attribute value
	@Test
	public void testUpdateWithNewAttributeValue() {

		Attribute l_wsAtt = new Attribute("Column 1");
		ws.attachAttribute(l_wsAtt);
		ws.addEntry(new Entry());
		AttributeValue l_av1 = new AttributeValue(l_wsAtt, "col1 value 1");
		ws.getEntries().get(0).attachAttributeValue(l_av1);

		WorkSheetDao l_wsDao = new WorkSheetDaoImpl();
		l_wsDao.createWorkSheet(ws);

		assertEquals(1, ws.getAttributes().size());
		assertEquals(1, ws.getEntries().size());
		assertEquals(1, ws.getEntries().get(0).getAttributeValues().size());

		Attribute l_wsAtt2 = new Attribute("Column 2");
		ws.attachAttribute(l_wsAtt2);
		Entry l_newEntry = new Entry();
		ws.addEntry(l_newEntry);
		l_newEntry.attachAttributeValue(new AttributeValue(l_wsAtt2,
				"Col2 Value"));

		l_wsDao.updateWorkSheet(ws);

		assertEquals(2, ws.getAttributes().size());
		assertEquals(2, ws.getEntries().size());
		assertEquals(1, ws.getEntries().get(1).getAttributeValues().size());

	}

	/** Testing the addition of new attributes */
	@Test
	public void testWorkSheetAttribute() {
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

		assertEquals("3B. The attribute was not properly removed",
				"My Attribute Nr 1", wsDao.fetchWorkSheet(ws.getWs_Id())
						.getAttributes().get(0).getAttrName());

	}
	
	/**
	 * 128: 	Error when entering the same name for more than one column
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	@Test
	public void testBugCreatingTwoHomonymousAttributes() {
		Attribute l_attrib1 = new Attribute(
				"Second Column");
		ws.attachAttribute(l_attrib1);

		Attribute l_myAttribute = new Attribute(
				"Second Column");

		ws.attachAttribute(l_myAttribute);

		assertEquals("1. ", 1, ws.getAttributes().size());
		
		// Test the addition of a worksheet attribute
		wsDao.createWorkSheet(ws);

		WorkSheet l_insertedWS = wsDao.fetchWorkSheet(ws.getWs_Id());

		assertEquals("2. ", 1, l_insertedWS.getAttributes().size());
	}


	/**
	 * Testing the addition of an entry
	 */
	@Test
	public void testEntryAdditions() {
		// Define Entry
		Entry l_wsEntry = new Entry();

		ws.addEntry(l_wsEntry);

		Entry l_wsEntry2 = new Entry();

		ws.addEntry(l_wsEntry2);

		wsDao.createWorkSheet(ws);

		WorkSheet l_insertedWS = wsDao.fetchWorkSheet(ws.getWs_Id());

		assertEquals("1. We should have the proper number of entries.", 2,
				l_insertedWS.getEntries().size());

	}

	/**
	 * Testing the full mounty. We will create a worksheet along with entries
	 * and attribute values
	 */
	@Test
	public void testFullEntryCreation() {
		// Define Entry
		Entry l_wsEntry = new Entry();
		ws.addEntry(l_wsEntry);

		// Create attributeValues to attach to the entry
		AttributeValue l_av1 = new AttributeValue(new Attribute(
				"Column 1"), "col1 value 1");
		AttributeValue l_av2 = new AttributeValue(new Attribute(
				"Column 2"), "col1 value 2");

		ws.attachAttribute(l_av1.getAttribute());
		ws.attachAttribute(l_av2.getAttribute());

		l_wsEntry.attachAttributeValue(l_av1);
		l_wsEntry.attachAttributeValue(l_av2);

		wsDao.createWorkSheet(ws);

		WorkSheet l_fetchedWS = wsDao.fetchWorkSheet(ws.getWs_Id());

		assertEquals("1.", 2, l_fetchedWS.getAttributes().size());

		assertEquals("2. ", 1, l_fetchedWS.getEntries().size());

		assertEquals("3. ", 2, l_fetchedWS.getEntries().get(0)
				.getAttributeValues().size());

	}

}
