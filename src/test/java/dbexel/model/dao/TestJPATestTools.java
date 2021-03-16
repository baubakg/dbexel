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

import org.junit.BeforeClass;
import org.junit.Test;

import dbexel.model.mapping.AttributeValue;
import dbexel.model.mapping.DBEXEL_TABLES;
import dbexel.model.mapping.WorkSheet;
import dbexel.model.mapping.Attribute;
import dbexel.model.mapping.Entry;

/**
 * This class tests the tools we have for testing the JPA framework
 * @author gandomi
 *
 */
public class TestJPATestTools {

	@BeforeClass
	public static void cleanUp() {
		JPATestTools.cleanAllData();
	}
	
	//Testing a delete all method
	@Test
	public void testCleanAllData() {
		WorkSheet l_workSheet = fetchTestWorkSheet("testDeleteAll");
		
		WorkSheetDao wsDao = new WorkSheetDaoImpl();
		wsDao.createWorkSheet(l_workSheet);
		
		JPATestTools.assertRowNumbers(1, DBEXEL_TABLES.WORKSHEET);	
		JPATestTools.assertRowNumbers(5, DBEXEL_TABLES.WORKSHEET_ATTRIBUTES);
		JPATestTools.assertRowNumbers(5, DBEXEL_TABLES.ATTRIBUTE);
		JPATestTools.assertRowNumbers(2, DBEXEL_TABLES.WORKSHEET_ENTRIES);
		JPATestTools.assertRowNumbers(2, DBEXEL_TABLES.ENTRY);
		JPATestTools.assertRowNumbers(10, DBEXEL_TABLES.ENTRY_VALUES);
		JPATestTools.assertRowNumbers(10, DBEXEL_TABLES.ATTRIBUTEVALUE);
		
		JPATestTools.cleanAllData();
		
		JPATestTools.assertRowNumbers(0, DBEXEL_TABLES.WORKSHEET);	
		JPATestTools.assertRowNumbers(0, DBEXEL_TABLES.WORKSHEET_ATTRIBUTES);
		JPATestTools.assertRowNumbers(0, DBEXEL_TABLES.ATTRIBUTE);
		JPATestTools.assertRowNumbers(0, DBEXEL_TABLES.WORKSHEET_ENTRIES);
		JPATestTools.assertRowNumbers(0, DBEXEL_TABLES.ENTRY);
		JPATestTools.assertRowNumbers(0, DBEXEL_TABLES.ENTRY_VALUES);
		JPATestTools.assertRowNumbers(0, DBEXEL_TABLES.ATTRIBUTEVALUE);
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
