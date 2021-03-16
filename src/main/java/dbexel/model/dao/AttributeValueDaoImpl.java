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

import java.util.Iterator;

import dbexel.model.mapping.AttributeValue;
import dbexel.model.mapping.Entry;

/**
 * @author Baubak
 * 
 */
public class AttributeValueDaoImpl extends JPAResourceDao implements
		AttributeValueDao {

	public void createAttributeValue(AttributeValue in_AttributeValue) {
		beginTransaction();
		getEm().persist(in_AttributeValue);
		commitTransaction();
	}

	public void deleteAttributeValue(AttributeValue in_attributeValue) {

		//Detach attributeValue from Attribute from
		in_attributeValue.getAttribute().getAttrValues().remove(in_attributeValue);
		
		for (Iterator<Entry> lt_entryIterator=in_attributeValue.getEntryList().iterator();lt_entryIterator.hasNext();) {
			Entry lt_entry = lt_entryIterator.next();
			lt_entryIterator.remove();
			lt_entry.getAttributeValues().remove(in_attributeValue);
		}
		
		beginTransaction();		
		getEm().flush();
		getEm().remove(in_attributeValue);
		commitTransaction();
	}


}
