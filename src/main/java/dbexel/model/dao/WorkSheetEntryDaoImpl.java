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
public class WorkSheetEntryDaoImpl extends JPAResourceDao implements
		WorkSheetEntryDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dbexel.model.dao.WorkSheetEntryDao#createEntry(dbexel.model.mapping.
	 * Entry)
	 */
	public void createWorkSheetEntry(Entry in_wsEntry) {
		beginTransaction();

		for (AttributeValue lt_attAttributeValue : in_wsEntry
				.getAttributeValues()) {
			getEm().persist(lt_attAttributeValue);
		}
		// getEm().persist(in_wsEntry.getWorkSheet());
		getEm().persist(in_wsEntry);
		commitTransaction();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbexel.model.dao.WorkSheetEntryDao#fetchWorkSheetEntry(java.lang.Long)
	 */
	public Entry fetchWorkSheetEntry(Long entryId) {
		return getEm().find(Entry.class, entryId);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbexel.model.dao.WorkSheetEntryDao#updateWorkSheet(dbexel.model.mapping
	 * .Entry)
	 */
	public void updateWorkSheet(Entry in_wsEntry) {
		beginTransaction();
		getEm().persist(in_wsEntry);
		commitTransaction();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbexel.model.dao.WorkSheetEntryDao#deleteWorkSheetEntry(dbexel.model.
	 * mapping.Entry)
	 */
	public void deleteWorkSheetEntry(Entry in_wsEntry) {
		AttributeValueDao l_avDao = new AttributeValueDaoImpl();

		// Remove attributevalue from entry
		for (Iterator<AttributeValue> lt_attributeValueIterator = in_wsEntry
				.getAttributeValues().iterator(); lt_attributeValueIterator
				.hasNext();) {

			AttributeValue lt_attrValue = lt_attributeValueIterator.next();
			lt_attributeValueIterator.remove();
			l_avDao.deleteAttributeValue(lt_attrValue);
		}

		// detach entry from worksheet
		in_wsEntry.getWorkSheet().getEntries().remove(in_wsEntry);

		beginTransaction();
		getEm().flush();
		getEm().remove(in_wsEntry);
		commitTransaction();
	}

}
