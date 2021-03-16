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
package dbexel.presentation.actions;

import java.util.HashMap;

import static org.hamcrest.Matchers.*;

import org.apache.struts2.StrutsTestCase;
import org.easymock.EasyMock;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionSupport;

import dbexel.model.dao.WorkSheetDao;
import dbexel.model.dao.WorkSheetEntryDao;
import dbexel.model.mapping.WorkSheet;
import dbexel.model.mapping.Attribute;
import dbexel.model.mapping.Entry;
import dbexel.system.tools.DBExelTestTools;

/**
 * This class tests the WorkSheetAction using a mock object
 * 
 * @author Baubak
 * 
 */
public class TestWorkSheetEntriesAction extends StrutsTestCase {
	private WorkSheetDao workSheetDaoMOCK;
	private WorkSheetEntriesAction wsEntryAction;
	private WorkSheet tstWorkSheet;
	private ActionProxy proxy;

	@Before
	public void setUp() {
		// TODO Auto-generated method stub
		try {
			super.setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// wsEntryAction = new WorkSheetEntriesAction();
		proxy = getActionProxy("/AddNewEntryToWorkSheet.action");
		// wsEntryAction = (WorkSheetEntriesAction) l_proxy.getAction();

		wsEntryAction = (WorkSheetEntriesAction) proxy.getAction();

		workSheetDaoMOCK = EasyMock.createMock(WorkSheetDao.class);

		// instantiate a worksheet
		tstWorkSheet = DBExelTestTools
				.fetchTestWorkSheetWithId("test WorkSheet");

		EasyMock.expect(
				workSheetDaoMOCK.fetchWorkSheet(DBExelTestTools
						.fetchTestWorkSheetWithId("test WorkSheet").getWs_Id()))
				.andReturn(tstWorkSheet).anyTimes();

	}

	// testing the fetching of a new worksheet
	@Test
	public void testShowWorkSheet() throws Exception {

		EasyMock.replay(workSheetDaoMOCK);

		// inject mock
		wsEntryAction.setWorkSheetDao(workSheetDaoMOCK);

		wsEntryAction.setWorkSheet_Id(tstWorkSheet.getWs_Id());

		wsEntryAction.prepare();
		wsEntryAction.showWorkSheet();
		assertEquals("1. ", "test WorkSheet", wsEntryAction.getWorkSheet()
				.getWs_Name());
	}

	/**
	 * Testing the creation of a new Entry for the worksheet create is tested
	 * here. In this case if we have not given any entries yet, we should be
	 * sent to the input screen
	 */
	@Test
	public void testAddEntryNoneGiven() {

		EasyMock.replay(workSheetDaoMOCK);

		// inject mock
		wsEntryAction.setWorkSheetDao(workSheetDaoMOCK);

		wsEntryAction.setWorkSheet_Id(tstWorkSheet.getWs_Id());

		// Initialize and fill the request with the map
		wsEntryAction.setNewAttributeValues(new HashMap<Long, String>());

		// At this point we expect the return value to be INPUT, because the
		// worksheet has no attribute values
		assertEquals("1. We expected INPUT here", "input",
				wsEntryAction.create());

	}

	/**
	 * Testing the creation of a new Entry for the worksheet create is tested
	 * here. In this case if we have given a few entries, we should save the
	 * entries now
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddEntryGiven() throws Exception {

		workSheetDaoMOCK.updateWorkSheet(tstWorkSheet);
		EasyMock.expectLastCall().atLeastOnce();
		EasyMock.replay(workSheetDaoMOCK);

		// inject mock
		wsEntryAction.setWorkSheetDao(workSheetDaoMOCK);

		wsEntryAction.setWorkSheet_Id(tstWorkSheet.getWs_Id());
		wsEntryAction.setWorkSheet(tstWorkSheet);

		// Initialize and fill the request with the map
		wsEntryAction.setNewAttributeValues(new HashMap<Long, String>());

		wsEntryAction.getNewAttributeValues().put(new Long(31), "89");
		wsEntryAction.getNewAttributeValues().put(new Long(27), "is");
		wsEntryAction.getNewAttributeValues().put(new Long(23), "a");
		wsEntryAction.getNewAttributeValues().put(new Long(9), "test");
		wsEntryAction.getNewAttributeValues().put(new Long(8), "entry");

		String actionReturnValue = proxy.execute();

		assertEquals(
				"2. At this point we expect the return value to be success",
				"success", actionReturnValue);

		assertEquals(5, wsEntryAction.getErrorNumberColumns().size());

		// After running the action method we should have one additional Entry
		assertEquals("3. We stil have the same number of af entries ", 3,
				wsEntryAction.getWorkSheet().getEntries().size());

		// Now we test thet our entry is correctly created
		Entry l_newlyAddedWorksheetEntry = wsEntryAction
				.getWorkSheet().getEntries().get(2);

		assertEquals("4. Wrong number of entry values", 5,
				l_newlyAddedWorksheetEntry.getAttributeValues().size());

		// Now we test that the attribute values are correctly set
		for (Attribute lt_attributes : wsEntryAction.getWorkSheet()
				.getAttributes()) {
			assertEquals(
					"5. ",
					lt_attributes.getAttrName(),
					l_newlyAddedWorksheetEntry
							.findValueByAttribute(lt_attributes).getAttribute()
							.getAttrName());

			assertEquals("6. ", 3, lt_attributes.getAttrValues().size());

		}
	}

	/**
	 * Testing the creation of a new Entry for the worksheet create is tested
	 * here. In this case if we have given a few entries, we should save the
	 * entries now
	 * 
	 * In this method we give a string for a number field
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddEntryGiven_typeFailure() throws Exception {

		workSheetDaoMOCK.updateWorkSheet(tstWorkSheet);
		EasyMock.expectLastCall().atLeastOnce();
		EasyMock.replay(workSheetDaoMOCK);

		// inject mock
		wsEntryAction.setWorkSheetDao(workSheetDaoMOCK);
		// request.setAttribute("workSheetDao", workSheetDaoMOCK);

		wsEntryAction.setWorkSheet_Id(tstWorkSheet.getWs_Id());
		wsEntryAction.setWorkSheet(tstWorkSheet);

		// Initialize and fill the request with the map
		wsEntryAction.setNewAttributeValues(new HashMap<Long, String>());

		wsEntryAction.getNewAttributeValues().put(new Long(31), "This");
		wsEntryAction.getNewAttributeValues().put(new Long(27), "is");
		wsEntryAction.getNewAttributeValues().put(new Long(23), "a");
		wsEntryAction.getNewAttributeValues().put(new Long(9), "test");
		wsEntryAction.getNewAttributeValues().put(new Long(8), "entry");

		assertEquals(
				"2. At this point we expect the return value to be success",
				ActionSupport.INPUT, proxy.execute());
	}

	/**
	 * Testing the edition of entries, when we first press Edit
	 */
	@Test
	public void testEditEntry_INPUT() {

		workSheetDaoMOCK.updateWorkSheet(tstWorkSheet);
		EasyMock.expectLastCall().atLeastOnce();
		EasyMock.replay(workSheetDaoMOCK);

		// inject mock
		wsEntryAction.setWorkSheetDao(workSheetDaoMOCK);

		wsEntryAction.setWorkSheet_Id(tstWorkSheet.getWs_Id());
		wsEntryAction.setWorkSheet(tstWorkSheet);

		wsEntryAction.setSelectedEntryId(new Long(41));

		// Initialize and fill the request with the map
		wsEntryAction.setNewAttributeValues(new HashMap<Long, String>());

		// Run the update action once
		// String actionReturnValue = wsEntryAction.tstUpdate();
		String actionReturnValue = wsEntryAction.update();

		assertEquals("1. At this point we expect the return value to be INPUT",
				"input", actionReturnValue);

		// Re-Initialize and fill the request with the map
		wsEntryAction.setNewAttributeValues(new HashMap<Long, String>());

		wsEntryAction.getNewAttributeValues().put(new Long(31), "This");
		wsEntryAction.getNewAttributeValues().put(new Long(27), "is");
		wsEntryAction.getNewAttributeValues().put(new Long(23), "a");
		wsEntryAction.getNewAttributeValues().put(new Long(9), "test");
		wsEntryAction.getNewAttributeValues().put(new Long(8), "entry");

		assertEquals(
				"2. At this point we expect the return value now to be success",
				"success", wsEntryAction.update());
	}

	/**
	 * Testing the edition of entries, when we first press Edit
	 */
	@Test
	public void testEditEntry_UPDATE() {

		workSheetDaoMOCK.updateWorkSheet(tstWorkSheet);
		EasyMock.expectLastCall().atLeastOnce();
		EasyMock.replay(workSheetDaoMOCK);

		// inject mock
		wsEntryAction.setWorkSheetDao(workSheetDaoMOCK);

		wsEntryAction.setWorkSheet_Id(tstWorkSheet.getWs_Id());
		wsEntryAction.setWorkSheet(tstWorkSheet);

		wsEntryAction.setSelectedEntryId(new Long(41));

		// Re-Initialize and fill the request with the map
		wsEntryAction.setNewAttributeValues(new HashMap<Long, String>());

		wsEntryAction.getNewAttributeValues().put(new Long(31), "This");
		wsEntryAction.getNewAttributeValues().put(new Long(27), "is");
		wsEntryAction.getNewAttributeValues().put(new Long(23), "a");
		wsEntryAction.getNewAttributeValues().put(new Long(9), "test");
		wsEntryAction.getNewAttributeValues().put(new Long(8), "entry");

		assertEquals(
				"1. At this point we expect the return value to be success",
				"success", wsEntryAction.update());

		// Find the editable worksheet entry
		Entry l_wsEntry = new Entry();

		for (Entry lt_wsEntry : wsEntryAction.getWorkSheet()
				.getEntries()) {
			if (lt_wsEntry.getEntryId().longValue() == wsEntryAction
					.getSelectedEntryId().longValue()) {
				l_wsEntry = lt_wsEntry;
			}
		}

		assertEquals("2. ", wsEntryAction.getNewAttributeValues().keySet()
				.size(), l_wsEntry.getAttributeValues().size());

		int i = 0;
		for (Long lt_AttributeId : wsEntryAction.getNewAttributeValues()
				.keySet()) {
			Attribute lt_wsAttribute = null;
			for (Attribute ltt_attr : wsEntryAction.getWorkSheet()
					.getAttributes()) {
				if (ltt_attr.getAttrId().longValue() == lt_AttributeId) {
					lt_wsAttribute = ltt_attr;
				}
			}

			assertEquals("3." + i++ + ". The value for attribute "
					+ lt_AttributeId + " does not match.", wsEntryAction
					.getNewAttributeValues().get(lt_AttributeId), l_wsEntry
					.findValueByAttribute(lt_wsAttribute).getValue());
		}

	}

	// Testing the new Deletion function
	@Test
	public void testDeleteEntry() {

		WorkSheetEntryDao workSheetEntryDaoMOCK = EasyMock
				.createMock(WorkSheetEntryDao.class);

		Entry l_wsEntry = tstWorkSheet.getEntries().get(1);

		assertEquals("1. The correct entry was not retrieved", 41, l_wsEntry
				.getEntryId().longValue());

		EasyMock.expect(
				workSheetEntryDaoMOCK.fetchWorkSheetEntry(l_wsEntry
						.getEntryId())).andReturn(l_wsEntry).times(1);

		workSheetEntryDaoMOCK.deleteWorkSheetEntry(l_wsEntry);
		EasyMock.expectLastCall().atLeastOnce();

		EasyMock.replay(workSheetDaoMOCK);
		EasyMock.replay(workSheetEntryDaoMOCK);

		// inject mock
		wsEntryAction.setWorkSheetDao(workSheetDaoMOCK);
		wsEntryAction.setWorkSheetEntryDao(workSheetEntryDaoMOCK);

		wsEntryAction.setWorkSheet_Id(tstWorkSheet.getWs_Id());
		wsEntryAction.setWorkSheet(tstWorkSheet);

		wsEntryAction.setSelectedEntryId(l_wsEntry.getEntryId());

		// assertEquals("2. At this point we expect the return value to be input",
		// "input", wsEntryAction.delete());

		// wsEntryAction.setDeletionAcknowledged(Boolean.TRUE);

		assertEquals(
				"3. At this point we expect the return value to be success",
				"success", wsEntryAction.delete());
	}

	/**
	 * Testing the creation of a new Entry for the worksheet create is tested
	 * here. In this case if we have given a few entries, and also we add an
	 * entry value
	 * 
	 */
	@Test
	public void testAddEntryAddAttrubuteValue() {

		workSheetDaoMOCK.updateWorkSheet(tstWorkSheet);
		EasyMock.expectLastCall().atLeastOnce();
		EasyMock.replay(workSheetDaoMOCK);

		// inject mock
		wsEntryAction.setWorkSheetDao(workSheetDaoMOCK);

		wsEntryAction.setWorkSheet_Id(tstWorkSheet.getWs_Id());
		wsEntryAction.setWorkSheet(tstWorkSheet);

		// Initialize and fill the request with the map
		wsEntryAction.setNewAttributeValues(new HashMap<Long, String>());

		wsEntryAction.getNewAttributeValues().put(new Long(31), "This");
		wsEntryAction.getNewAttributeValues().put(new Long(27), "is");
		wsEntryAction.getNewAttributeValues().put(new Long(23), "a");
		wsEntryAction.getNewAttributeValues().put(new Long(9), "test");
		wsEntryAction.getNewAttributeValues().put(new Long(8), "entry");

		// Fill the worksheet with the new attributeValue
		wsEntryAction.getNewAddedAttributes().add("Author");
		wsEntryAction.getNewAddedAttributeValues().add("Melville");

		String actionReturnValue = wsEntryAction.create();

		assertEquals(
				"2. At this point we expect the return value to be success",
				"success", actionReturnValue);

		// After running the action method we should have one additional Entry
		assertEquals("3. We stil have the same number of af entries ", 3,
				wsEntryAction.getWorkSheet().getEntries().size());

		// Now we test thet our entry is correctly created
		Entry l_newlyAddedWorksheetEntry = wsEntryAction
				.getWorkSheet().getEntries().get(2);

		assertEquals("4. Wrong number of entry values", 6,
				l_newlyAddedWorksheetEntry.getAttributeValues().size());

		// Now we test that the attribute values are correctly set
		for (Attribute lt_attributes : wsEntryAction.getWorkSheet()
				.getAttributes()) {
			assertEquals(
					"5. ",
					lt_attributes.getAttrName(),
					l_newlyAddedWorksheetEntry
							.findValueByAttribute(lt_attributes).getAttribute()
							.getAttrName());

			// We should only have one attribute value for the newly added
			// attribute value
			if (lt_attributes.getAttrName().equals("Author")) {
				assertEquals(
						"6. Bad number of attribute values for the newly added attribute value",
						1, lt_attributes.getAttrValues().size());
			} else {
				assertEquals(
						"6. Bad number of attribute values for the already existing attribute value",
						3, lt_attributes.getAttrValues().size());
			}

		}
	}

	/**
	 * 
	 Acceptance Criteria #2A Given : - I am in the the page Add Entry - I have
	 * left out information in the attribute / attribute value pair When : I
	 * submit the data Then : I don't get to see the worksheet with the added
	 * attribute values
	 * 
	 * @return
	 */
	@Test
	public void testIssue68_AC2A() {

		workSheetDaoMOCK.updateWorkSheet(tstWorkSheet);
		EasyMock.expectLastCall().atLeastOnce();
		EasyMock.replay(workSheetDaoMOCK);

		// inject mock
		wsEntryAction.setWorkSheetDao(workSheetDaoMOCK);

		wsEntryAction.setWorkSheet_Id(tstWorkSheet.getWs_Id());
		wsEntryAction.setWorkSheet(tstWorkSheet);

		// Initialize and fill the request with the map
		wsEntryAction.setNewAttributeValues(new HashMap<Long, String>());

		wsEntryAction.getNewAttributeValues().put(new Long(31), "This");
		wsEntryAction.getNewAttributeValues().put(new Long(27), "is");
		wsEntryAction.getNewAttributeValues().put(new Long(23), "a");
		wsEntryAction.getNewAttributeValues().put(new Long(9), "test");
		wsEntryAction.getNewAttributeValues().put(new Long(8), "entry");

		// Fill the worksheet with an empty attributeValue
		wsEntryAction.getNewAddedAttributes().add("");
		wsEntryAction.getNewAddedAttributeValues().add("");

		// Fill the worksheet with the new attributeValue
		wsEntryAction.getNewAddedAttributes().add("Author");
		wsEntryAction.getNewAddedAttributeValues().add("Melville");

		String actionReturnValue = wsEntryAction.create();

		assertEquals(
				"2. At this point we expect the return value to be success",
				"success", actionReturnValue);

		// After running the action method we should have one additional Entry
		assertEquals("3. We stil have the same number of af entries ", 3,
				wsEntryAction.getWorkSheet().getEntries().size());

		// Now we test thet our entry is correctly created
		Entry l_newlyAddedWorksheetEntry = wsEntryAction
				.getWorkSheet().getEntries().get(2);

		assertEquals("4. Wrong number of entry values", 6, wsEntryAction
				.getWorkSheet().getAttributes().size());

		assertEquals("4. Wrong number of entry values", 6,
				l_newlyAddedWorksheetEntry.getAttributeValues().size());
	}

	/*
	 * Acceptance Criteria #2B Given : - I am in the the page Add Entry - I have
	 * entered an attribute value for an existing attribute. When : I submit the
	 * data Then : An error is generated and we are sent back to the add entry
	 * page
	 */
	/**
	 * 137: 	Maintaining Unique attributes when adding an attribute value
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testIssue68_AC2B() throws Exception {

		workSheetDaoMOCK.updateWorkSheet(tstWorkSheet);
		EasyMock.expectLastCall().atLeastOnce();
		EasyMock.replay(workSheetDaoMOCK);

		// inject mock
		wsEntryAction.setWorkSheetDao(workSheetDaoMOCK);

		wsEntryAction.setWorkSheet_Id(tstWorkSheet.getWs_Id());
		wsEntryAction.setWorkSheet(tstWorkSheet);

		// Initialize and fill the request with the map
		wsEntryAction.setNewAttributeValues(new HashMap<Long, String>());

		wsEntryAction.getNewAttributeValues().put(new Long(31), "This");
		wsEntryAction.getNewAttributeValues().put(new Long(27), "is");
		wsEntryAction.getNewAttributeValues().put(new Long(23), "a");
		wsEntryAction.getNewAttributeValues().put(new Long(9), "test");
		wsEntryAction.getNewAttributeValues().put(new Long(8), "entry");

		// Fill the worksheet with the new attributeValue
		//wsEntryAction.getNewAddedAttributes().add("Column 3");
		wsEntryAction.getNewAddedAttributes().add(tstWorkSheet.getAttributes().get(0).getAttrName());
		wsEntryAction.getNewAddedAttributeValues().add("Melville");

		//String actionReturnValue = wsEntryAction.create();
		String actionReturnValue = proxy.execute();
		assertEquals(
				"2. At this point we expect the return value to be input",
				"input", actionReturnValue);

	}

	// This is regards to the issue:
	// 90: When adding an entry, not filling an attribute value still stores it
	// in the system
	@Test
	public void emptyAttributeValuesShouldNotBeStored() {
		workSheetDaoMOCK.updateWorkSheet(tstWorkSheet);
		EasyMock.expectLastCall().atLeastOnce();
		EasyMock.replay(workSheetDaoMOCK);

		// inject mock
		wsEntryAction.setWorkSheetDao(workSheetDaoMOCK);

		wsEntryAction.setWorkSheet_Id(tstWorkSheet.getWs_Id());
		wsEntryAction.setWorkSheet(tstWorkSheet);

		int l_initialNrOfEntries = tstWorkSheet.getEntries().size();

		// Initialize and fill the request with the map
		wsEntryAction.setNewAttributeValues(new HashMap<Long, String>());

		wsEntryAction.getNewAttributeValues().put(new Long(31), "This");
		wsEntryAction.getNewAttributeValues().put(new Long(27), "is");
		wsEntryAction.getNewAttributeValues().put(new Long(23), "");
		wsEntryAction.getNewAttributeValues().put(new Long(9), null);
		wsEntryAction.getNewAttributeValues().put(new Long(8), "entry");

		wsEntryAction.create();

		Entry l_entry = wsEntryAction.getWorkSheet().getEntries()
				.get(l_initialNrOfEntries);

		assertThat(l_entry.getAttributeValues().size(), is(equalTo(3)));

		assertThat(tstWorkSheet.getAttributes().get(0).fetchEntriesUsingThis()
				.size(), is(equalTo(3)));
		assertThat(tstWorkSheet.getAttributes().get(1).fetchEntriesUsingThis()
				.size(), is(equalTo(2)));
		assertThat(tstWorkSheet.getAttributes().get(2).fetchEntriesUsingThis()
				.size(), is(equalTo(2)));

	}

	/**
	 * Testing the creation of a new Entry for the worksheet create is tested
	 * here. In this case if we have given a few entries, and also we add an
	 * entry value
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testAddEntryAddAttrubuteValue_TestTypeValidationFails()
			throws Exception {

		workSheetDaoMOCK.updateWorkSheet(tstWorkSheet);
		EasyMock.expectLastCall().atLeastOnce();
		EasyMock.replay(workSheetDaoMOCK);

		// inject mock
		wsEntryAction.setWorkSheetDao(workSheetDaoMOCK);

		wsEntryAction.setWorkSheet_Id(tstWorkSheet.getWs_Id());
		wsEntryAction.setWorkSheet(tstWorkSheet);

		// Initialize and fill the request with the map
		wsEntryAction.setNewAttributeValues(new HashMap<Long, String>());

		wsEntryAction.getNewAttributeValues().put(new Long(31), "15");
		wsEntryAction.getNewAttributeValues().put(new Long(27), "is");
		wsEntryAction.getNewAttributeValues().put(new Long(23), "a");
		wsEntryAction.getNewAttributeValues().put(new Long(9), "test");
		wsEntryAction.getNewAttributeValues().put(new Long(8), "entry");

		// Fill the worksheet with the new attributeValue
		wsEntryAction.getNewAddedAttributes().add("Age");
		wsEntryAction.getNewAddedColumnTypes().add(
				AttributeTypes.Number.toString());
		wsEntryAction.getNewAddedAttributeValues().add("Melville");
		/*
		 * String actionReturnValue = wsEntryAction.create();
		 * 
		 * assertEquals(
		 * "2. At this point we expect the return value to be success",
		 * ActionSupport.INPUT, actionReturnValue);
		 */
		assertEquals(
				"2. At this point we expect the return value to be success",
				ActionSupport.INPUT, proxy.execute());

		// After running the action method we should have one additional Entry
		assertEquals("3. We should have the same number of entries ", 2,
				wsEntryAction.getWorkSheet().getEntries().size());
		
		assertEquals(6, wsEntryAction.getErrorNumberColumns().size());

	}	
}
