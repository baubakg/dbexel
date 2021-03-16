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

import org.apache.struts2.StrutsTestCase;
import org.easymock.EasyMock;

import static org.hamcrest.Matchers.*;

import static org.junit.Assert.*;

import org.junit.Test;

import com.opensymphony.xwork2.ActionProxy;

import dbexel.model.dao.WorkSheetDao;
import dbexel.model.mapping.WorkSheet;
import dbexel.model.mapping.Attribute;
import dbexel.model.mapping.Entry;
import dbexel.system.tools.DBExelTestTools;

/**
 * @author baubak
 * 
 */
public class WorkSheetAccessTests extends StrutsTestCase {
	private WorkSheet tstWorkSheet = DBExelTestTools.fetchTestWorkSheetWithId("test WorkSheet");

	@Test
	public void testAction() throws Exception {
		request.setParameter("workSheet_Id", tstWorkSheet.getWs_Id().toString());

		ActionProxy l_proxy = getActionProxy("/EditWorkSheet.action");
		assertNotNull("1.", l_proxy);

		WorkSheetDao l_workSheetDaoMOCK = EasyMock
				.createMock(WorkSheetDao.class);
		WorkSheet l_workSheet = new WorkSheet("Before WorkSheet");
		l_workSheet.setWs_Id(tstWorkSheet.getWs_Id());

		EasyMock.expect(
				l_workSheetDaoMOCK.fetchWorkSheet(tstWorkSheet.getWs_Id()))
				.andReturn(l_workSheet).anyTimes();
		l_workSheetDaoMOCK.updateWorkSheet(l_workSheet);
		EasyMock.expectLastCall().atLeastOnce();

		EasyMock.replay(l_workSheetDaoMOCK);

		DistinctWorkSheetAction l_newAction = (DistinctWorkSheetAction) l_proxy
				.getAction();

		assertNotNull("2.", l_newAction);
		l_newAction.setWorkSheet_Id(tstWorkSheet.getWs_Id());

		l_newAction.setWorkSheetDao(l_workSheetDaoMOCK);

		assertEquals("3. ", "input", l_newAction.editWorkSheet());

		l_newAction.setNewWSName("After WorkSheet");

		assertEquals("4.", "success", l_proxy.execute());

		assertEquals("5. value not changed", "After WorkSheet",
				l_workSheet.getWs_Name());

		assertNotNull("6. ", l_newAction);

	}

	// testing the fetching of a new worksheet
	@Test
	public void testShowWorkSheetA() {
		WorkSheetDao l_workSheetDaoMOCK = EasyMock
				.createMock(WorkSheetDao.class);

		EasyMock.expect(
				l_workSheetDaoMOCK.fetchWorkSheet(tstWorkSheet.getWs_Id()))
				.andReturn(tstWorkSheet).anyTimes();

		EasyMock.replay(l_workSheetDaoMOCK);

		// WorkSheetEntriesAction wsEntryAction = new WorkSheetEntriesAction();
		DistinctWorkSheetAction wsAction = new DistinctWorkSheetAction();

		// inject mock
		wsAction.setWorkSheetDao(l_workSheetDaoMOCK);

		wsAction.setWorkSheet_Id(tstWorkSheet.getWs_Id());

		wsAction.showWorkSheet();
		assertEquals("1. ", "test WorkSheet", wsAction.getWorkSheet()
				.getWs_Name());
	}

	@Test
	public void testDeleteWorkSheet() throws Exception {
		request.setParameter("workSheet_Id", tstWorkSheet.getWs_Id().toString());

		ActionProxy l_proxy = getActionProxy("/DeleteWorkSheet.action");
		assertNotNull("1.", l_proxy);

		WorkSheetDao l_workSheetDaoMOCK = EasyMock
				.createMock(WorkSheetDao.class);

		EasyMock.expect(
				l_workSheetDaoMOCK.fetchWorkSheet(tstWorkSheet.getWs_Id()))
				.andReturn(tstWorkSheet);

		l_workSheetDaoMOCK.deleteWorkSheet(tstWorkSheet);
		EasyMock.expectLastCall().atLeastOnce();

		EasyMock.replay(l_workSheetDaoMOCK);

		DistinctWorkSheetAction l_newAction = (DistinctWorkSheetAction) l_proxy
				.getAction();

		assertNotNull("2.", l_newAction);

		l_newAction.setWorkSheetDao(l_workSheetDaoMOCK);
		l_newAction.setDeleteAcknowledged(Boolean.TRUE);

		assertEquals("4.", "success", l_proxy.execute());

	}

	@Test
	public void testDeleteAttributeInWorkSheet() throws Exception {
		request.setParameter("workSheet_Id", tstWorkSheet.getWs_Id().toString());

		ActionProxy l_proxy = getActionProxy("/EditWorkSheet!deleteAttribute.action");
		assertNotNull("1.", l_proxy);

		WorkSheetDao l_workSheetDaoMOCK = EasyMock
				.createMock(WorkSheetDao.class);

		EasyMock.expect(
				l_workSheetDaoMOCK.fetchWorkSheet(tstWorkSheet.getWs_Id()))
				.andReturn(tstWorkSheet).once();

		l_workSheetDaoMOCK.updateWorkSheet(tstWorkSheet);
		EasyMock.expectLastCall().atLeastOnce();

		EasyMock.replay(l_workSheetDaoMOCK);

		DistinctWorkSheetAction l_newAction = (DistinctWorkSheetAction) l_proxy
				.getAction();

		assertNotNull("2.", l_newAction);
		l_newAction.setWorkSheet_Id(tstWorkSheet.getWs_Id());

		Attribute l_deletedAttribute = tstWorkSheet.getAttributes()
				.get(1);
		l_newAction.setSelectedAttributeId(l_deletedAttribute.getAttrId());

		l_newAction.setWorkSheetDao(l_workSheetDaoMOCK);

		// take references
		int l_refNrOfAttributes = tstWorkSheet.getAttributes().size();

		assertEquals("3. ", "input", l_newAction.removeAttribute());

		assertEquals("The worksheet was not set", tstWorkSheet.getWs_Name(),
				l_newAction.getWorkSheet().getWs_Name());

		// Checking that the attribute has been remoed from the worksheets
		assertThat(l_newAction.getWorkSheet().getAttributes().size(),
				is(equalTo(l_refNrOfAttributes - 1)));

		assertThat("The attribute was not removed from the worksheet itself",
				l_deletedAttribute, not(isIn(l_newAction.getWorkSheet()
						.getAttributes())));

		// Checking that the attribute has been removed from the attribute
		// values of the entry
		for (Entry lt_entry : l_newAction.getWorkSheet().getEntries()) {
			assertThat("The attribute was not removed from the entries",
					l_deletedAttribute,
					not(isIn(lt_entry.fetchSetAttributes())));
		}

	}

	// Testing the moving of attributes within a worksheet
	@Test
	public  void testMoveAttributeToTheLeft() throws Exception {
		
		request.setParameter("selectedAttributeId", tstWorkSheet.getWs_Id().toString());
		request.setParameter("selectedIndex", tstWorkSheet.getWs_Id().toString());

		ActionProxy l_proxy = getActionProxy("/EditWorkSheet!moveAttributeLeft.action");
		assertNotNull("1.", l_proxy);
		
		
		WorkSheetDao l_workSheetDaoMOCK = EasyMock
				.createMock(WorkSheetDao.class);

		EasyMock.expect(
				l_workSheetDaoMOCK.fetchWorkSheet(tstWorkSheet.getWs_Id()))
				.andReturn(tstWorkSheet).once();

		l_workSheetDaoMOCK.updateWorkSheet(tstWorkSheet);
		EasyMock.expectLastCall().atLeastOnce();
		
		EasyMock.replay(l_workSheetDaoMOCK);

		DistinctWorkSheetAction l_newAction = (DistinctWorkSheetAction) l_proxy
				.getAction();

		assertNotNull("2.", l_newAction);
		
		l_newAction.setWorkSheet_Id(tstWorkSheet.getWs_Id());		

		Attribute l_movedAttribute = tstWorkSheet.getAttributes()
				.get(1);
		
		l_newAction.setSelectedAttributeId(l_movedAttribute.getAttrId());
		l_newAction.setSelectedIndex(1);
		
		l_newAction.setWorkSheetDao(l_workSheetDaoMOCK);
		
		assertEquals("3. ", "input", l_newAction.moveAttributeLeft());
		
		assertNotEquals(l_movedAttribute, tstWorkSheet.getAttributes().get(1));
		
		assertEquals(l_movedAttribute, tstWorkSheet.getAttributes().get(0));
		
	}
	
	// Testing the moving of attributes within a worksheet
		@Test
		public  void testMoveAttributeToTheRight() throws Exception {
			
			request.setParameter("selectedAttributeId", tstWorkSheet.getWs_Id().toString());
			request.setParameter("selectedIndex", tstWorkSheet.getWs_Id().toString());

			ActionProxy l_proxy = getActionProxy("/EditWorkSheet!moveAttributeRight.action");
			assertNotNull("1.", l_proxy);
			
			
			WorkSheetDao l_workSheetDaoMOCK = EasyMock
					.createMock(WorkSheetDao.class);

			EasyMock.expect(
					l_workSheetDaoMOCK.fetchWorkSheet(tstWorkSheet.getWs_Id()))
					.andReturn(tstWorkSheet).once();

			l_workSheetDaoMOCK.updateWorkSheet(tstWorkSheet);
			EasyMock.expectLastCall().atLeastOnce();
			
			EasyMock.replay(l_workSheetDaoMOCK);

			DistinctWorkSheetAction l_newAction = (DistinctWorkSheetAction) l_proxy
					.getAction();

			assertNotNull("2.", l_newAction);
			
			l_newAction.setWorkSheet_Id(tstWorkSheet.getWs_Id());		

			Attribute l_movedAttribute = tstWorkSheet.getAttributes()
					.get(1);
			
			l_newAction.setSelectedAttributeId(l_movedAttribute.getAttrId());
			l_newAction.setSelectedIndex(1);
			
			l_newAction.setWorkSheetDao(l_workSheetDaoMOCK);
			
			assertEquals("3. ", "input", l_newAction.moveAttributeRight());
			
			assertNotEquals(l_movedAttribute, tstWorkSheet.getAttributes().get(1));
			
			assertEquals(l_movedAttribute, tstWorkSheet.getAttributes().get(2));
			
		}
		
		@Test
		public void testEditWSAction_withHints() throws Exception {
			
			WorkSheet l_ws = DBExelTestTools
					.fetchTestWorkSheetWithId("Attribute Worksheet");

			Attribute l_attr = l_ws.getAttributes().get(
					l_ws.getAttributes().size() - 1);

			assertThat(l_attr.getType(), is(equalTo(AttributeTypes.Number)));

			// Make change to worksheet to cause a type suggestion
			l_attr.setType(AttributeTypes.Text);
			
			request.setParameter("workSheet_Id", l_ws.getWs_Id().toString());

			ActionProxy l_proxy = getActionProxy("/EditWorkSheet.action");
			assertNotNull("1.", l_proxy);

			WorkSheetDao l_workSheetDaoMOCK = EasyMock
					.createMock(WorkSheetDao.class);
			EasyMock.expect(
					l_workSheetDaoMOCK.fetchWorkSheet(l_ws.getWs_Id()))
					.andReturn(l_ws).anyTimes();
			
			EasyMock.replay(l_workSheetDaoMOCK);

			DistinctWorkSheetAction l_newAction = (DistinctWorkSheetAction) l_proxy
					.getAction();

			assertNotNull("2.", l_newAction);
			l_newAction.setWorkSheet_Id(tstWorkSheet.getWs_Id());

			l_newAction.setWorkSheetDao(l_workSheetDaoMOCK);

			assertEquals("3. ", "input", l_proxy.execute());
			
			assertThat(l_newAction.getActionMessages().size(), is(equalTo(1)));

		}
}
