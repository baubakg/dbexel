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
package dbexel.system.tools;

import dbexel.model.dao.JPATestTools;
import dbexel.model.dao.WorkSheetDao;
import dbexel.model.dao.WorkSheetDaoImpl;
import dbexel.model.mapping.AttributeValue;
import dbexel.model.mapping.WorkSheet;
import dbexel.model.mapping.Attribute;
import dbexel.model.mapping.Entry;
import dbexel.presentation.actions.AttributeTypes;

/**
 * @author gandomi
 *
 */
public class DBExelTestTools {

	/**
	 * This method returns a test worksheet with fake id's
	 * @param in_name
	 * @return
	 */
	public static WorkSheet fetchTestWorkSheet(String in_name) {
		WorkSheet l_workSheet = new WorkSheet(in_name);

		Attribute l_wsAttr1 = new Attribute("Column 3");
		l_wsAttr1.setType(AttributeTypes.Text);

		Attribute l_wsAttr2 = new Attribute("Column 5");
		l_wsAttr2.setType(AttributeTypes.Text);
		
		Attribute l_wsAttr3 = new Attribute("Column 7");
		l_wsAttr3.setType(AttributeTypes.Text);
		
		Attribute l_wsAttr4 = new Attribute("Column 11");
		l_wsAttr4.setType(AttributeTypes.Text);
		
		Attribute l_wsAttr5 = new Attribute("Column 13");
		l_wsAttr5.setType(AttributeTypes.Number);
		
		// Add attributes
		l_workSheet.attachAttribute(l_wsAttr1);
		l_workSheet.attachAttribute(l_wsAttr2);
		l_workSheet.attachAttribute(l_wsAttr3);
		l_workSheet.attachAttribute(l_wsAttr4);
		l_workSheet.attachAttribute(l_wsAttr5);

		// Add Antries
		l_workSheet.addEntry(new Entry());
		l_workSheet.addEntry(new Entry());

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
						new AttributeValue(l_wsAttr5, "13"));

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
						new AttributeValue(l_wsAttr5, "23"));

		return l_workSheet;
	}

	
	/**
	 * This method returns a test worksheet with fake id's
	 * @param in_name
	 * @return
	 */
	public static WorkSheet fetchTestWorkSheetWithId(String in_name) {
		WorkSheet l_workSheet = fetchTestWorkSheet(in_name);
		
		l_workSheet.setWs_Id(new Long(17));

		//set Ids for the attributes
		l_workSheet.getAttributes().get(0).setAttrId(new Long(8));
		l_workSheet.getAttributes().get(1).setAttrId(new Long(9));
		l_workSheet.getAttributes().get(2).setAttrId(new Long(23));
		l_workSheet.getAttributes().get(3).setAttrId(new Long(27));
		l_workSheet.getAttributes().get(4).setAttrId(new Long(31));

		//set Ids for the entries
		l_workSheet.getEntries().get(0).setEntryId(new Long(37));
		l_workSheet.getEntries().get(1).setEntryId(new Long(41));

		return l_workSheet;
	}
	

	public static void main(String[] args) {
		JPATestTools.cleanAllData();

		WorkSheetDao l_wsDao = new WorkSheetDaoImpl();

		l_wsDao.createWorkSheet(fetchTestWorkSheet("Visual WorkSheet"));
}

// This method tests the get last cell method

}
