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

import dbexel.model.mapping.Attribute;

/**
 * Dao interface for Attributes
 * @author gandomi
 *
 */
public interface AttributeDao {

	/**
	 * The DAO for deleting an attribute
	 * @param in_attribute
	 */
	void deleteAttribute(Attribute in_attribute);

	/**
	 * The DAO for returning the Attribute data from the database
	 *  
	 * @param in_attributeId
	 * @return
	 */
	Attribute fetchAttribute(Long in_attributeId);

	
	/**
	 * The DAO for changing the attribute
	 * 
	 * @param in_attribute
	 */
	void updateAttribute(Attribute in_attribute);

}
