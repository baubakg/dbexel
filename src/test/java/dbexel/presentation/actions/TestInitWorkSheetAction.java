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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.struts2.StrutsTestCase;
import org.easymock.EasyMock;
import org.hamcrest.core.Is;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionSupport;

import dbexel.model.dao.WorkSheetDao;
import dbexel.model.mapping.WorkSheet;
import dbexel.system.tools.DBExelTools;

public class TestInitWorkSheetAction extends StrutsTestCase {

	@Test
	public void retrieveText() {
		InitWorkSheet l_newWorkSheet = new InitWorkSheet();
		l_newWorkSheet.setWorkSheetName("MyWorkSheet");

		l_newWorkSheet
				.setSentence("Lent Leaugue of Extraordinary Men to Antoine Br�l�");

		// split sentence
		List<String> l_slashedSentence = l_newWorkSheet.slashSentence();

		assertEquals("1. ", 8, l_slashedSentence.size());

		l_newWorkSheet.setAttributeValues(l_slashedSentence);

		assertEquals("2. ", 8, l_newWorkSheet.getAttributeValues().size());

	}

	@Test
	public void testMethodExecution() {
		InitWorkSheet l_newWorkSheet = new InitWorkSheet();
		l_newWorkSheet.setWorkSheetName("MyWorkSheet");

		l_newWorkSheet
				.setSentence("Lent Leaugue of Extraordinary Men to Antoine Br�l�");

		l_newWorkSheet.prepareNewWorkSheet();

		assertEquals("1. ", 8, l_newWorkSheet.getAttributeValues().size());

	}

	@Test
	public void testCellSuggestions() {
		InitWorkSheet l_newWorkSheet = new InitWorkSheet();
		l_newWorkSheet.setWorkSheetName("MyWorkSheet");

		l_newWorkSheet.setSentence("Lent 2 euros to Jack");

		l_newWorkSheet.prepareNewWorkSheet();

		assertEquals("1. ", 5, l_newWorkSheet.getAttributeValues().size());

		assertEquals("2.", 5, l_newWorkSheet.getColumnTypes().size());

		assertThat("3.", l_newWorkSheet.getColumnTypes().get(1),
				is(equalTo(AttributeTypes.Number.toString())));

		assertThat("4.", l_newWorkSheet.getColumnTypes().get(2),
				is(equalTo(AttributeTypes.Text.toString())));

	}

	// Testing the addition of attribute names
	@Test
	public void testAttributeNames() {
		InitWorkSheet l_newWorkSheet = new InitWorkSheet();
		l_newWorkSheet.setWorkSheetName("MyWorkSheet");
		l_newWorkSheet.setSentence("Hello Cruel World");

		l_newWorkSheet.setAttributeValues(l_newWorkSheet.slashSentence());

		l_newWorkSheet.setSentence("First_Word Second_Word Third_Word");

		l_newWorkSheet.setAttributeNames(l_newWorkSheet.slashSentence());

		assertEquals(
				"1. We do not have the correct number of attribute names.", 3,
				l_newWorkSheet.getAttributeNames().size());
	}

	// Testing the deletion of a cell
	@Test
	public void testDeleteCell() throws Exception {
		ActionProxy l_proxy = getActionProxy("/StoreInitWorkSheetAction.action");

		InitWorkSheet l_newWorkSheetAction = (InitWorkSheet) l_proxy
				.getAction();

		l_newWorkSheetAction.setWorkSheetName("MyWorkSheet");
		l_newWorkSheetAction.setSentence("Hello Cruel World bye");

		l_newWorkSheetAction.setAttributeValues(l_newWorkSheetAction
				.slashSentence());

		l_newWorkSheetAction
				.setSentence("First_Word Second_Word Third_Word Fourth_Word");

		l_newWorkSheetAction.setAttributeNames(l_newWorkSheetAction
				.slashSentence());

		List<Boolean> l_keepCell = new ArrayList<Boolean>();

		l_keepCell.add(Boolean.TRUE);
		l_keepCell.add(Boolean.FALSE);
		l_keepCell.add(Boolean.TRUE);
		l_keepCell.add(Boolean.FALSE);

		l_newWorkSheetAction.setKeepColumns(l_keepCell);

		List<String> l_columnTypes = new ArrayList<String>();
		l_columnTypes.add(AttributeTypes.Text.toString());
		l_columnTypes.add(AttributeTypes.Text.toString());
		l_columnTypes.add(AttributeTypes.Text.toString());
		l_columnTypes.add(AttributeTypes.Number.toString());

		l_newWorkSheetAction.setColumnTypes(l_columnTypes);

		/*
		 * BGA
		 * assertNull("2.5. The worksheetId should not have been initialized",
		 * l_newWorkSheetAction.getWorkSheet_Id());
		 */
		assertNull("2.5. The worksheet should not have been initialized",
				l_newWorkSheetAction.getNewWorkSheet());

		WorkSheetDao l_workSheetDaoMOCK = EasyMock
				.createNiceMock(WorkSheetDao.class);
		EasyMock.replay(l_workSheetDaoMOCK);
		l_newWorkSheetAction.setWorkSheetDao(l_workSheetDaoMOCK);

		assertThat(l_proxy.execute(), is(equalTo(ActionSupport.SUCCESS)));

		assertEquals(
				"3. After the cellActions we should have as many values as there are names.",
				l_newWorkSheetAction.getAttributeNames().size(),
				l_newWorkSheetAction.getAttributeValues().size());

		/*
		 * BGA assertNotNull("4. The worksheetId has not been initialized",
		 * l_newWorkSheetAction.getWorkSheet_Id());
		 */

		assertNotNull("4A. The worksheetId has not been initialized",
				l_newWorkSheetAction.getNewWorkSheet());

		assertNotNull("4B. The worksheetId has not been initialized",
				l_newWorkSheetAction.getNewWorkSheet().getWs_Id());

	}

	// Testing the setting of a type #1
	@Test
	public void testColumnType1() throws Exception {
		ActionProxy l_proxy = getActionProxy("/StoreInitWorkSheetAction.action");

		InitWorkSheet l_newWorkSheetAction = (InitWorkSheet) l_proxy
				.getAction();

		l_newWorkSheetAction.setWorkSheetName("MyWorkSheet");
		l_newWorkSheetAction.setSentence("Hello Cruel World 15.2");

		l_newWorkSheetAction.setAttributeValues(l_newWorkSheetAction
				.slashSentence());

		l_newWorkSheetAction
				.setSentence("First_Word Second_Word Third_Word Fourth_Word");

		l_newWorkSheetAction.setAttributeNames(l_newWorkSheetAction
				.slashSentence());

		List<Boolean> l_keepCell = new ArrayList<Boolean>();

		l_keepCell.add(Boolean.TRUE);
		l_keepCell.add(Boolean.TRUE);
		l_keepCell.add(Boolean.TRUE);
		l_keepCell.add(Boolean.TRUE);

		l_newWorkSheetAction.setKeepColumns(l_keepCell);

		List<String> l_columnTypes = new ArrayList<String>();
		l_columnTypes.add(AttributeTypes.Text.toString());
		l_columnTypes.add(AttributeTypes.Text.toString());
		l_columnTypes.add(AttributeTypes.Text.toString());
		l_columnTypes.add(AttributeTypes.Number.toString());

		l_newWorkSheetAction.setColumnTypes(l_columnTypes);

		/*
		 * BGA
		 * assertNull("2.5. The worksheetId should not have been initialized",
		 * l_newWorkSheetAction.getWorkSheet_Id());
		 */
		assertNull("2.5. The worksheetId should not have been initialized",
				l_newWorkSheetAction.getNewWorkSheet());

		WorkSheetDao l_workSheetDaoMOCK = EasyMock
				.createNiceMock(WorkSheetDao.class);
		EasyMock.replay(l_workSheetDaoMOCK);
		l_newWorkSheetAction.setWorkSheetDao(l_workSheetDaoMOCK);

		l_proxy.execute();

		assertEquals(
				"3. After the cellActions we should have as many values as there are names.",
				l_newWorkSheetAction.getAttributeNames().size(),
				l_newWorkSheetAction.getAttributeValues().size());

		/*
		 * BGA assertNotNull("4. The worksheetId has not been initialized",
		 * l_newWorkSheetAction.getWorkSheet_Id());
		 */

		WorkSheet l_newWorkSheet = l_newWorkSheetAction.getNewWorkSheet();

		assertNotNull("4A. The worksheet has not been initialized",
				l_newWorkSheet);

		assertNotNull("4B. The worksheetId has not been initialized",
				l_newWorkSheet.getWs_Id());

		assertThat(l_newWorkSheet.getAttributes().get(0).getType(),
				is(equalTo(AttributeTypes.Text)));

	}

	// Testing the setting of a type #2
	@Test
	public void testColumnType_ValidationFailure() throws Exception {
		ActionProxy l_proxy = getActionProxy("/StoreInitWorkSheetAction.action");

		InitWorkSheet l_newWorkSheetAction = (InitWorkSheet) l_proxy
				.getAction();

		l_newWorkSheetAction.setWorkSheetName("MyWorkSheet");
		l_newWorkSheetAction.setSentence("Hello Cruel World ByeBye");

		l_newWorkSheetAction.setAttributeValues(l_newWorkSheetAction
				.slashSentence());

		l_newWorkSheetAction
				.setSentence("First_Word Second_Word Third_Word Fourth_Word");

		l_newWorkSheetAction.setAttributeNames(l_newWorkSheetAction
				.slashSentence());

		List<Boolean> l_keepCell = new ArrayList<Boolean>();

		l_keepCell.add(Boolean.TRUE);
		l_keepCell.add(Boolean.TRUE);
		l_keepCell.add(Boolean.TRUE);
		l_keepCell.add(Boolean.TRUE);

		l_newWorkSheetAction.setKeepColumns(l_keepCell);

		List<String> l_columnTypes = new ArrayList<String>();
		l_columnTypes.add(AttributeTypes.Text.toString());
		l_columnTypes.add(AttributeTypes.Text.toString());
		l_columnTypes.add(AttributeTypes.Text.toString());
		l_columnTypes.add(AttributeTypes.Number.toString());

		l_newWorkSheetAction.setColumnTypes(l_columnTypes);

		WorkSheetDao l_workSheetDaoMOCK = EasyMock
				.createNiceMock(WorkSheetDao.class);
		EasyMock.replay(l_workSheetDaoMOCK);

		request.setAttribute("workSheetDao", l_workSheetDaoMOCK);

		assertEquals(ActionSupport.INPUT, l_proxy.execute());

		assertTrue(
				"Problem There were no errors present in fieldErrors but there should have been one error present",
				l_newWorkSheetAction.getFieldErrors().size() == 1);

		assertTrue(
				"Problem field AVError_3 not present in fieldErrors but it should have been",
				l_newWorkSheetAction.getFieldErrors().containsKey("AVError_3"));
	}

	// Testing the validation of the work sheet name
	@Test
	public void testWorkSheetName_ValidationFailure() throws Exception {
		ActionProxy l_proxy = getActionProxy("/StoreInitWorkSheetAction.action");

		InitWorkSheet l_newWorkSheetAction = (InitWorkSheet) l_proxy
				.getAction();

		l_newWorkSheetAction.setAttributeValues(Arrays.asList("Hello", "Cruel",
				"World", "12.5"));

		l_newWorkSheetAction.setAttributeNames(Arrays.asList("First_Word",
				"Second_Word", "Third_Word", "Fourth_Word"));

		l_newWorkSheetAction.setKeepColumns(Arrays.asList(Boolean.TRUE,
				Boolean.TRUE, Boolean.TRUE, Boolean.TRUE));

		l_newWorkSheetAction.setColumnTypes(Arrays.asList(
				AttributeTypes.Text.toString(), AttributeTypes.Text.toString(),
				AttributeTypes.Text.toString(),
				AttributeTypes.Number.toString()));

		WorkSheetDao l_workSheetDaoMOCK = EasyMock
				.createNiceMock(WorkSheetDao.class);
		EasyMock.replay(l_workSheetDaoMOCK);

		request.setAttribute("workSheetDao", l_workSheetDaoMOCK);

		assertEquals(ActionSupport.INPUT, l_proxy.execute());

		assertTrue(
				"Problem field workSheetName not present in fieldErrors but it should have been",
				l_newWorkSheetAction.getFieldErrors().containsKey(
						"workSheetName"));

		assertThat(
				"Problem There were no errors present in fieldErrors but there should have been one error present",
				l_newWorkSheetAction.getFieldErrors().size(), is(equalTo(1)));

	}

	// Testing the setting of a type #2
	@Test
	public void testColumnType_ValidationFailure_ErrorMessageProblems()
			throws Exception {
		ActionProxy l_proxy = getActionProxy("/StoreInitWorkSheetAction.action");

		InitWorkSheet l_newWorkSheetAction = (InitWorkSheet) l_proxy
				.getAction();

		l_newWorkSheetAction.setWorkSheetName("MyWorkSheet");
		l_newWorkSheetAction.setSentence("Hello Cruel World ByeBye");

		l_newWorkSheetAction.setAttributeValues(l_newWorkSheetAction
				.slashSentence());

		l_newWorkSheetAction
				.setSentence("First_Word Second_Word Third_Word Fourth_Word");

		l_newWorkSheetAction.setAttributeNames(l_newWorkSheetAction
				.slashSentence());

		List<Boolean> l_keepCell = new ArrayList<Boolean>();

		l_keepCell.add(Boolean.TRUE);
		l_keepCell.add(Boolean.TRUE);
		l_keepCell.add(Boolean.TRUE);
		l_keepCell.add(Boolean.TRUE);

		l_newWorkSheetAction.setKeepColumns(l_keepCell);

		List<String> l_columnTypes = new ArrayList<String>();
		l_columnTypes.add(AttributeTypes.Text.toString());
		l_columnTypes.add(AttributeTypes.Text.toString());
		l_columnTypes.add(AttributeTypes.Text.toString());
		l_columnTypes.add(AttributeTypes.Number.toString());

		l_newWorkSheetAction.setColumnTypes(l_columnTypes);

		WorkSheetDao l_workSheetDaoMOCK = EasyMock
				.createNiceMock(WorkSheetDao.class);
		EasyMock.replay(l_workSheetDaoMOCK);

		request.setAttribute("workSheetDao", l_workSheetDaoMOCK);

		assertEquals(ActionSupport.INPUT, l_proxy.execute());

		assertTrue(
				"Problem There were no errors present in fieldErrors but there should have been one error present",
				l_newWorkSheetAction.getFieldErrors().size() == 1);

		assertEquals(4, l_newWorkSheetAction.getErrorNumberColumns().size());

		assertTrue(
				"Problem field AVError_3 not present in fieldErrors but it should have been",
				l_newWorkSheetAction.getFieldErrors().containsKey("AVError_3"));
	}

	// Testing the setting of a type #2
	@Test
	public void testStandardCreate() throws Exception {
		ActionProxy l_proxy = getActionProxy("/StoreInitWorkSheetAction.action");

		Map<String, Object> l_session = new java.util.HashMap<String, Object>();
		ActionContext.getContext().setSession(l_session);

		assertNull(
				"We expect the session variable not to be set yet",
				ActionContext.getContext().getSession()
						.get(AwareKeys.New_WorkSheet_ID));

		InitWorkSheet l_newWorkSheetAction = (InitWorkSheet) l_proxy
				.getAction();

		l_newWorkSheetAction.setWorkSheetName("MyWorkSheet");
		l_newWorkSheetAction.setSentence("Hello Cruel World");

		l_newWorkSheetAction.setAttributeValues(l_newWorkSheetAction
				.slashSentence());

		l_newWorkSheetAction.setSentence("First_Word Second_Word Third_Word");

		l_newWorkSheetAction.setAttributeNames(l_newWorkSheetAction
				.slashSentence());

		List<Boolean> l_keepCell = new ArrayList<Boolean>();

		l_keepCell.add(Boolean.TRUE);
		l_keepCell.add(Boolean.TRUE);
		l_keepCell.add(Boolean.TRUE);

		l_newWorkSheetAction.setKeepColumns(l_keepCell);

		List<String> l_columnTypes = new ArrayList<String>();
		l_columnTypes.add(AttributeTypes.Text.toString());
		l_columnTypes.add(AttributeTypes.Text.toString());
		l_columnTypes.add(AttributeTypes.Text.toString());

		l_newWorkSheetAction.setColumnTypes(l_columnTypes);

		WorkSheetDao l_workSheetDaoMOCK = EasyMock
				.createMock(WorkSheetDao.class);

		l_workSheetDaoMOCK.createWorkSheet((WorkSheet) EasyMock.anyObject());
		// l_workSheetDaoMOCK.createWorkSheet(l_newWorkSheetAction.getNewWorkSheet());
		// l_workSheetDaoMOCK.createWorkSheet(null);
		EasyMock.expectLastCall().once();
		EasyMock.replay(l_workSheetDaoMOCK);

		// request.setAttribute("workSheetDao", l_workSheetDaoMOCK);
		l_newWorkSheetAction.setWorkSheetDao(l_workSheetDaoMOCK);

		assertEquals(ActionSupport.SUCCESS, l_proxy.execute());

		assertThat(
				"We expect the session variable to be set",
				(Long) ActionContext.getContext().getSession()
						.get(AwareKeys.New_WorkSheet_ID),
				is(equalTo(l_newWorkSheetAction.getNewWorkSheet().getWs_Id())));
	}
	

	/**
	 * 128: Error when entering the same name for more than one column
	 * 
	 * @throws Exception
	 * 
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	@Test
	public void testBugCreatingTwoHomonymousAttributes() throws Exception {
		ActionProxy l_proxy = getActionProxy("/StoreInitWorkSheetAction.action");

		InitWorkSheet l_newWorkSheetAction = (InitWorkSheet) l_proxy
				.getAction();

		l_newWorkSheetAction.setWorkSheetName("MyWorkSheet");
		l_newWorkSheetAction.setSentence("Hello Cruel World");

		l_newWorkSheetAction.setAttributeValues(l_newWorkSheetAction
				.slashSentence());

		l_newWorkSheetAction.setSentence("First_Word Second_Word Second_Word");

		l_newWorkSheetAction.setAttributeNames(l_newWorkSheetAction
				.slashSentence());

		List<Boolean> l_keepCell = new ArrayList<Boolean>();

		l_keepCell.add(Boolean.TRUE);
		l_keepCell.add(Boolean.TRUE);
		l_keepCell.add(Boolean.TRUE);

		l_newWorkSheetAction.setKeepColumns(l_keepCell);

		List<String> l_columnTypes = new ArrayList<String>();
		l_columnTypes.add(AttributeTypes.Text.toString());
		l_columnTypes.add(AttributeTypes.Text.toString());
		l_columnTypes.add(AttributeTypes.Text.toString());

		l_newWorkSheetAction.setColumnTypes(l_columnTypes);

		WorkSheetDao l_workSheetDaoMOCK = EasyMock
				.createMock(WorkSheetDao.class);

		l_workSheetDaoMOCK.createWorkSheet((WorkSheet) EasyMock.anyObject());
		EasyMock.expectLastCall().once();
		EasyMock.replay(l_workSheetDaoMOCK);

		l_newWorkSheetAction.setWorkSheetDao(l_workSheetDaoMOCK);

		assertEquals(ActionSupport.SUCCESS, l_proxy.execute());

		WorkSheet l_createdWorkSheet = l_newWorkSheetAction.getNewWorkSheet();

		assertThat(l_createdWorkSheet.getAttributes().size(), equalTo(2));

		assertThat("The first attribute should not change", l_createdWorkSheet
				.getAttributes().get(0).getAttrName(),
				is(equalTo("First_Word")));

		assertThat("The last attribute should be 'Second_Word'",
				l_createdWorkSheet.getAttributes().get(1).getAttrName(),
				is(equalTo("Second_Word")));

		assertThat("The duplicate attribute should only have one attribute",
				l_createdWorkSheet.getAttributes().get(1).getAttrValues()
						.size(), is(equalTo(1)));

		assertThat(l_createdWorkSheet.getEntries().size(), is(equalTo(1)));

		assertThat(l_createdWorkSheet.getEntries().get(0).getAttributeValues()
				.size(), is(equalTo(2)));

		assertThat(
				"The attribute of the last attribute in the entry should be Second_Word",
				l_createdWorkSheet.getEntries().get(0).getAttributeValues()
						.get(1).getAttribute().getAttrName(),
				is(equalTo("Second_Word")));

		assertThat("The value for Second_Word should be World",
				l_createdWorkSheet.getEntries().get(0).getAttributeValues()
						.get(1).getValue(), is(equalTo("World")));

	}
}
