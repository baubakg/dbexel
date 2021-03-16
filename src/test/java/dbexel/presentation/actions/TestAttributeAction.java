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

import org.apache.struts2.StrutsTestCase;
import org.easymock.EasyMock;
import org.junit.Test;

import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionSupport;

import dbexel.model.dao.AttributeDao;
import dbexel.model.mapping.AttributeValue;
import dbexel.model.mapping.WorkSheet;
import dbexel.model.mapping.Attribute;
import dbexel.model.mapping.Entry;
import dbexel.system.tools.DBExelTestTools;

public class TestAttributeAction extends StrutsTestCase {
	// WorkShee

	@Test
	public void testFirst() throws Exception {

		ActionProxy l_proxy = getActionProxy("/ShowDistinctAttribute.action");
		assertNotNull("1.", l_proxy);

	}

	@Test
	public void testFlow() throws Exception {
		ActionProxy l_proxy = getActionProxy("/ShowDistinctAttribute.action");

		WorkSheet l_ws = DBExelTestTools
				.fetchTestWorkSheetWithId("Attribute Worksheet");

		Attribute l_attr = l_ws.getAttributes().get(0);

		AttributeDao l_attributeDaoMOCK = EasyMock
				.createMock(AttributeDao.class);

		EasyMock.expect(l_attributeDaoMOCK.fetchAttribute(l_attr.getAttrId()))
				.andReturn(l_attr).times(2);

		EasyMock.expect(l_attributeDaoMOCK.fetchAttribute(l_attr.getAttrId()))
				.andReturn(l_attr).times(2);

		l_attributeDaoMOCK.updateAttribute(l_attr);
		EasyMock.expectLastCall().atLeastOnce();

		EasyMock.replay(l_attributeDaoMOCK);

		AttributeAction l_attrAction = (AttributeAction) l_proxy.getAction();

		l_attrAction.setAttributeId(l_attr.getAttrId());
		l_attrAction.setAttributeDao(l_attributeDaoMOCK);

		// String l_result = l_attrAction.execute();
		String l_result = l_proxy.execute();

		assertThat(l_result, is(equalTo(ActionSupport.INPUT)));

		assertThat(l_attrAction.getAttributeName(),
				is(equalTo(l_attr.getAttrName())));

		l_attrAction.setAttributeName("blabla");
		l_attrAction.setAttributeType(AttributeTypes.Text.toString());
		l_attrAction.setAttributeDescription("");

		l_attrAction.prepare();
		l_result = l_attrAction.execute();

		assertThat(l_result, is(equalTo(ActionSupport.SUCCESS)));

	}

	@Test
	public void testFetchEntriesUsedByAttribute() throws Exception {
		ActionProxy l_proxy = getActionProxy("/ShowDistinctAttribute.action");

		WorkSheet l_ws = DBExelTestTools
				.fetchTestWorkSheetWithId("Attribute Worksheet");

		Attribute l_attr = l_ws.getAttributes().get(0);

		l_ws.addEntry(new Entry());
		Entry l_entry = l_ws.getEntries().get(l_ws.getEntries().size() - 1);

		for (int i = 1; i < l_ws.getAttributes().size(); i++) {
			l_entry.attachAttributeValue(new AttributeValue(l_ws
					.getAttributes().get(i), "NewVal " + i));

		}

		AttributeDao l_attributeDaoMOCK = EasyMock
				.createMock(AttributeDao.class);

		EasyMock.expect(l_attributeDaoMOCK.fetchAttribute(l_attr.getAttrId()))
				.andReturn(l_attr).times(2);

		l_attributeDaoMOCK.updateAttribute(l_attr);
		EasyMock.expectLastCall().atLeastOnce();

		EasyMock.replay(l_attributeDaoMOCK);

		AttributeAction l_attrAction = (AttributeAction) l_proxy.getAction();

		l_attrAction.setAttributeId(l_attr.getAttrId());
		l_attrAction.setAttributeDao(l_attributeDaoMOCK);

		l_attrAction.prepare();
		l_attrAction.execute();

		assertThat(l_attrAction.getEntriesPresent().size(), is(lessThan(l_ws
				.getEntries().size())));
		assertThat(l_attrAction.getEntriesPresent().size(), is(equalTo(2)));
	}

	// In this example we allow the user to change the type of the attribute as
	// well
	@Test
	public void testTypeChange() throws Exception {
		ActionProxy l_proxy = getActionProxy("/ShowDistinctAttribute.action");

		WorkSheet l_ws = DBExelTestTools
				.fetchTestWorkSheetWithId("Attribute Worksheet");

		Attribute l_attr = l_ws.getAttributes().get(0);

		assertThat(
				"The attribute we need to change should at first be a fumber",
				l_attr.getType(), is(equalTo(AttributeTypes.Text)));

		AttributeDao l_attributeDaoMOCK = EasyMock
				.createMock(AttributeDao.class);

		EasyMock.expect(l_attributeDaoMOCK.fetchAttribute(l_attr.getAttrId()))
				.andReturn(l_attr).times(1);

		l_attributeDaoMOCK.updateAttribute(l_attr);
		EasyMock.expectLastCall().atLeastOnce();

		EasyMock.replay(l_attributeDaoMOCK);

		AttributeAction l_attrAction = (AttributeAction) l_proxy.getAction();

		l_attrAction.setAttributeId(l_attr.getAttrId());
		l_attrAction.setAttributeDao(l_attributeDaoMOCK);
		l_attrAction.setAttributeType(AttributeTypes.Number.toString());
		l_attrAction.setAttributeName("blabla");

		String l_result = l_proxy.execute();

		assertEquals(1, l_attrAction.getFieldErrors().size());

		assertTrue(l_attrAction.getFieldErrors().containsKey("attributeType"));

		assertEquals(1, l_attrAction.getFieldErrors().get("attributeType")
				.size());

		assertThat(l_result, is(equalTo(ActionSupport.INPUT)));

	}

	// In this example we allow the user to change the type of the attribute as
	// well
	@Test
	public void testDescriptionChange() throws Exception {
		ActionProxy l_proxy = getActionProxy("/ShowDistinctAttribute.action");

		AttributeAction l_attrAction = (AttributeAction) l_proxy.getAction();

		WorkSheet l_ws = DBExelTestTools
				.fetchTestWorkSheetWithId("Attribute Worksheet");

		Attribute l_attr = l_ws.getAttributes().get(0);

		AttributeDao l_attributeDaoMOCK = EasyMock
				.createMock(AttributeDao.class);
		/*
		 * AttributeDao l_attributeDaoMOCK = EasyMock
		 * .createNiceMock(AttributeDao.class);
		 */

		EasyMock.expect(l_attributeDaoMOCK.fetchAttribute(l_attr.getAttrId()))
				.andReturn(l_attr);

		l_attributeDaoMOCK.updateAttribute(l_attr);
		EasyMock.expectLastCall().atLeastOnce();

		EasyMock.replay(l_attributeDaoMOCK);

		// request.setAttribute("attributeDao", l_attributeDaoMOCK);
		l_attrAction.setAttributeDao(l_attributeDaoMOCK);
		l_attrAction.setAttributeId(l_attr.getAttrId());
		l_attrAction.setAttributeType(AttributeTypes.Text.toString());
		l_attrAction.setAttributeName("blabla");
		String l_newDesc = "This attribute is very usefull";
		l_attrAction.setAttributeDescription(l_newDesc);

		// String l_result = l_proxy.execute();
		l_attrAction.prepare();
		l_attrAction.execute();

		assertThat(l_attrAction.getAttribute().getDescription(),
				is(equalTo(l_newDesc)));

		assertEquals(0, l_attrAction.getFieldErrors().size());
	}

	// In this example we see if the system correctly hints at the correct type
	// of the attribute value
	@Test
	public void testTypeHints() throws Exception {
		ActionProxy l_proxy = getActionProxy("/ShowDistinctAttribute.action");

		WorkSheet l_ws = DBExelTestTools
				.fetchTestWorkSheetWithId("Attribute Worksheet");

		Attribute l_attr = l_ws.getAttributes().get(
				l_ws.getAttributes().size() - 1);

		assertThat(l_attr.getType(), is(equalTo(AttributeTypes.Number)));

		// Make change to worksheet to cause a type suggestion
		l_attr.setType(AttributeTypes.Text);

		AttributeDao l_attributeDaoMOCK = EasyMock
				.createMock(AttributeDao.class);

		EasyMock.expect(l_attributeDaoMOCK.fetchAttribute(l_attr.getAttrId()))
				.andReturn(l_attr).times(2);

		EasyMock.expect(l_attributeDaoMOCK.fetchAttribute(l_attr.getAttrId()))
				.andReturn(l_attr).times(2);

		l_attributeDaoMOCK.updateAttribute(l_attr);
		EasyMock.expectLastCall().atLeastOnce();

		EasyMock.replay(l_attributeDaoMOCK);

		AttributeAction l_attrAction = (AttributeAction) l_proxy.getAction();

		l_attrAction.setAttributeId(l_attr.getAttrId());
		l_attrAction.setAttributeDao(l_attributeDaoMOCK);

		// String l_result = l_attrAction.execute();
		String l_result = l_proxy.execute();

		assertThat(l_result, is(equalTo(ActionSupport.INPUT)));

		assertThat(l_attrAction.getActionMessages().size(), is(equalTo(1)));
	}

	// This follows the bug 133 - IndexOutOfBoundsException when making changes
	// to attribute in a one entry worksheet
	//@Test
	public void testBug133() throws Exception {
		ActionProxy l_proxy = getActionProxy("/ShowDistinctAttribute.action");

		WorkSheet l_ws = new WorkSheet("My WorkSheet");
		l_ws.addEntry(new Entry());
		Entry l_entry = l_ws.getEntries().get(0);
		Attribute l_attr1 = new Attribute("Attr1");
		Attribute l_attr2 = new Attribute("Attr2");
		l_attr2.setAttrId(new Long(1));
		
		l_entry.attachAttributeValue(new AttributeValue(l_attr1, "a"));
		l_entry.attachAttributeValue(new AttributeValue(l_attr2, "5"));
		
		assertThat(
				"The attribute we need to change should at first be a fumber",
				l_attr2.getType(), is(equalTo(AttributeTypes.Text)));

		AttributeDao l_attributeDaoMOCK = EasyMock
				.createMock(AttributeDao.class);

		EasyMock.expect(l_attributeDaoMOCK.fetchAttribute(l_attr2.getAttrId()))
				.andReturn(l_attr2).times(1);

		l_attributeDaoMOCK.updateAttribute(l_attr2);
		EasyMock.expectLastCall().atLeastOnce();

		EasyMock.replay(l_attributeDaoMOCK);

		AttributeAction l_attrAction = (AttributeAction) l_proxy.getAction();

		l_attrAction.setAttributeId(l_attr2.getAttrId());
		l_attrAction.setAttributeDao(l_attributeDaoMOCK);
		l_attrAction.setAttributeName(l_attr2.getAttrName());
		l_attrAction.setAttributeType(AttributeTypes.Number.toString());

		l_attrAction.prepare();
		l_attrAction.validate();
		String l_result = l_attrAction.execute();

		assertEquals(0, l_attrAction.getFieldErrors().size());

		assertThat(l_result, is(equalTo(ActionSupport.SUCCESS)));

	}
	
	// 117: 	Validation of attributes: Add check that attribute name does not already exist
	// In this test we take the last attribute and rename it to the same name as the first attribute.
	// Expected result : We should have an error 
	@Test
	public void testRenamingAttributeToAlreadyExistingName() throws Exception {
		ActionProxy l_proxy = getActionProxy("/ShowDistinctAttribute.action");

		WorkSheet l_ws = DBExelTestTools
				.fetchTestWorkSheetWithId("Attribute Worksheet");

		assertThat(
				"The test work sheet should have at least two attributes",
				l_ws.getAttributes().size(), is(greaterThanOrEqualTo(2)));

		Attribute l_attr = l_ws.getAttributes().get(0);
		
		assertThat(
				"The attribute we need to change should at first be a fumber",
				l_attr.getType(), is(equalTo(AttributeTypes.Text)));
		
		Attribute l_attrRef = l_ws.getAttributes().get(l_ws.getAttributes().size()-1);

		
		AttributeDao l_attributeDaoMOCK = EasyMock
				.createMock(AttributeDao.class);

		EasyMock.expect(l_attributeDaoMOCK.fetchAttribute(l_attr.getAttrId()))
				.andReturn(l_attr).times(1);

		l_attributeDaoMOCK.updateAttribute(l_attr);
		EasyMock.expectLastCall().atLeastOnce();

		EasyMock.replay(l_attributeDaoMOCK);

		AttributeAction l_attrAction = (AttributeAction) l_proxy.getAction();

		l_attrAction.setAttributeId(l_attr.getAttrId());
		l_attrAction.setAttributeDao(l_attributeDaoMOCK);
		l_attrAction.setAttributeName(l_attrRef.getAttrName());
		l_attrAction.setAttributeType(l_attr.getType().toString());

		String l_result = l_proxy.execute();

		assertThat(l_result, is(equalTo(ActionSupport.INPUT)));
		
		assertEquals("we should have an error here", 1, l_attrAction.getFieldErrors().size());

		assertTrue(l_attrAction.getFieldErrors().containsKey("attributeName"));

		assertEquals(1, l_attrAction.getFieldErrors().get("attributeName")
				.size());		

	}
}
