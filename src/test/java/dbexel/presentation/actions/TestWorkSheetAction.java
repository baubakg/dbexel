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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.StrutsTestCase;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionProxy;

import dbexel.model.dao.WorkSheetDao;
import dbexel.model.mapping.WorkSheet;

/**
 * This class tests the WorkSheetAction using a mock object
 * 
 * @author Baubak
 * 
 */
public class TestWorkSheetAction extends StrutsTestCase {
	List<WorkSheet> wsList = new ArrayList<WorkSheet>();

	@Before
	public void setUp() {
		try {
			super.setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wsList.add(new WorkSheet("Worskeet Nr. 1"));
		wsList.add(new WorkSheet("Worskeet Nr. 2"));
		wsList.add(new WorkSheet("Worskeet Nr. 3"));
		wsList.get(2).setWs_Id(new Long(5));
		wsList.add(new WorkSheet("Worskeet Nr. 4"));	

	}

	// we test that our Dao methods are actually called
	@Test
	public void testInstantiateCalled() throws Exception {
				
		ActionProxy l_proxy = getActionProxy("/ShowWorkSheets.action");

		Map<String, Object> l_session = new java.util.HashMap<String, Object>();		
		ActionContext.getContext().setSession(l_session);
		
		WorkSheetDao workSheetDaoMOCK = EasyMock.createMock(WorkSheetDao.class);

		EasyMock.expect(workSheetDaoMOCK.fetchAllWorkSheets())
				.andReturn(wsList);

		EasyMock.replay(workSheetDaoMOCK);
		
		WorkSheetAction wsAction = (WorkSheetAction) l_proxy.getAction();
		
		// inject mock
		wsAction.setWorkSheetDao(workSheetDaoMOCK);
		
		//initialize session variable
		ActionContext.getContext().getSession().put(AwareKeys.New_WorkSheet_ID, wsList.get(2).getWs_Id());
		
		assertEquals("1. ", "success", l_proxy.execute());

		assertEquals("2. ", 4, wsAction.getWorkSheetList().size());
		
		assertEquals("3. ", wsList.get(2).getWs_Id(), wsAction.getNewWorkSheetId());
		
	}

}
