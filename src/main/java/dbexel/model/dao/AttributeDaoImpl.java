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
import dbexel.model.mapping.Attribute;

/**
 * @author gandomi
 * 
 */
public class AttributeDaoImpl extends JPAResourceDao implements AttributeDao {

	/**
	 * The DAO for deleting an attribute
	 * 
	 */
	public void deleteAttribute(Attribute in_attribute) {

		in_attribute.getWorkSheet().getAttributes().remove(in_attribute);
		
		AttributeValueDao l_avDao =  new AttributeValueDaoImpl();

		for (Iterator<AttributeValue> lt_avIterator = in_attribute
				.getAttrValues().iterator(); lt_avIterator.hasNext();) {
			
			AttributeValue lt_attributeValue = lt_avIterator.next();

			lt_avIterator.remove();
			l_avDao.deleteAttributeValue(lt_attributeValue);
		}

		beginTransaction();

		getEm().remove(in_attribute);
		getEm().flush();
		commitTransaction();

	}

	/**
	 * The DAO for returning the Attribute data from the database
	 */
	public Attribute fetchAttribute(Long in_attributeId) {
		Attribute l_attr = getEm().find(Attribute.class, in_attributeId);
		return l_attr;

	}

	
	/**
	 * The DAO for changing the attribute
	 */
	public void updateAttribute(Attribute in_attribute) {
		beginTransaction();
		getEm().persist(in_attribute);
		commitTransaction();
	}

}
