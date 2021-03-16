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

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import dbexel.model.mapping.AttributeValue;
import dbexel.model.mapping.DBEXEL_TABLES;
import dbexel.model.mapping.WorkSheet;
import dbexel.model.mapping.Attribute;
import dbexel.model.mapping.Entry;

/**
 * This class tests the creation of the AttributeValues
 * 
 * @author Baubak
 * 
 */
public class TestAttrValueDao {
	@BeforeClass
	public static void clean() {
		JPATestTools.cleanAllData();
	}
	
	@After
	public void cleanAll() {
		JPATestTools.cleanAllData();
	}

	// test simple creation, and how cascading works here
	@Test
	public void testCreateAttributeValue() {

		WorkSheet l_newWorkSheet = new WorkSheet("My WorkSheet");
		Attribute l_attribute = new Attribute("Column 1");
		l_newWorkSheet.attachAttribute(l_attribute);

		l_newWorkSheet.addEntry(new Entry());

		l_newWorkSheet.getEntries().get(0)
				.attachAttributeValue(new AttributeValue(l_attribute, "Blaa"));

		assertNull(l_attribute.getAttrId());
		WorkSheetDao l_wsDao = new WorkSheetDaoImpl();
		l_wsDao.createWorkSheet(l_newWorkSheet);

		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.WORKSHEET);
		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.ENTRY);
		// JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.ENTRY_VALUES);
		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.WORKSHEET_ATTRIBUTES);
		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.ATTRIBUTE);
		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.ATTRIBUTEVALUE);

	}

	@Test
	public void testCreateTwoValuesForEntry() {
		WorkSheet l_newWorkSheet = new WorkSheet("My WorkSheet");

		Attribute l_wsAtt = new Attribute("Column 1");

		l_newWorkSheet.attachAttribute(l_wsAtt);

		l_newWorkSheet.addEntry(new Entry());

		AttributeValue l_av1 = new AttributeValue(l_wsAtt, "col1 value 1");
		AttributeValue l_av2 = new AttributeValue(l_wsAtt, "col1 value 2");

		l_newWorkSheet.getEntries().get(0).attachAttributeValue(l_av1);
		l_newWorkSheet.getEntries().get(0).attachAttributeValue(l_av2);

		WorkSheetDao l_wsDao = new WorkSheetDaoImpl();
		l_wsDao.createWorkSheet(l_newWorkSheet);

		assertEquals(1, l_newWorkSheet.getEntries().get(0)
				.getAttributeValues().size());
		
		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.WORKSHEET);
		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.ENTRY);
		// JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.ENTRY_VALUES);
		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.WORKSHEET_ATTRIBUTES);
		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.ATTRIBUTE);
		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.ATTRIBUTEVALUE);

	}
	
	//81: 	An attribute value for the same attribute should be unique by its value
	@Test
	public void testCreateOneValueForTwoEntries_1() {
		WorkSheet l_newWorkSheet = new WorkSheet("My WorkSheet");

		Attribute l_wsAtt = new Attribute("Column 1");
		Entry l_entry1 = new Entry();
		Entry l_entry2 = new Entry();

		l_newWorkSheet.attachAttribute(l_wsAtt);

		l_newWorkSheet.addEntry(l_entry1);
		l_newWorkSheet.addEntry(l_entry2);

		AttributeValue l_av1 = new AttributeValue(l_wsAtt, "Val 1");
		l_entry1.attachAttributeValue(l_av1);
		l_entry2.attachAttributeValue(l_av1);
			
		WorkSheetDao l_wsDao = new WorkSheetDaoImpl();
		l_wsDao.createWorkSheet(l_newWorkSheet);

		assertEquals(1, l_newWorkSheet.getEntries().get(0)
				.getAttributeValues().size());
		
		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.WORKSHEET);
		JPATestTools.assertRowNumbers(2, DBEXEL_TABLES.ENTRY);
		// JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.ENTRY_VALUES);
		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.WORKSHEET_ATTRIBUTES);
		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.ATTRIBUTE);
		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.ATTRIBUTEVALUE);

	}
	
	//81: 	An attribute value for the same attribute should be unique by its value
		@Test
		public void testCreateOneValueForTwoEntries_2() {
			WorkSheet l_newWorkSheet = new WorkSheet("My WorkSheet");

			Attribute l_wsAtt = new Attribute("Column 1");
			Entry l_entry1 = new Entry();
			Entry l_entry2 = new Entry();

			l_newWorkSheet.attachAttribute(l_wsAtt);

			l_newWorkSheet.addEntry(l_entry1);
			l_newWorkSheet.addEntry(l_entry2);

			AttributeValue l_av1 = new AttributeValue(l_wsAtt, "Val 1");
			l_entry1.attachAttributeValue(l_av1);
			l_entry2.attachAttributeValue(new AttributeValue(l_wsAtt, "Val 1"));
				
			WorkSheetDao l_wsDao = new WorkSheetDaoImpl();
			l_wsDao.createWorkSheet(l_newWorkSheet);

			assertEquals(1, l_newWorkSheet.getEntries().get(0)
					.getAttributeValues().size());
			
			JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.WORKSHEET);
			JPATestTools.assertRowNumbers(2, DBEXEL_TABLES.ENTRY);
			// JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.ENTRY_VALUES);
			JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.WORKSHEET_ATTRIBUTES);
			JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.ATTRIBUTE);
			JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.ATTRIBUTEVALUE);

		}
}
