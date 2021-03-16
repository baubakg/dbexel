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
import java.util.List;

import dbexel.model.mapping.WorkSheet;
import dbexel.model.mapping.Attribute;
import dbexel.model.mapping.Entry;

/**
 * @author Baubak
 * 
 */
public class WorkSheetDaoImpl extends JPAResourceDao implements WorkSheetDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dbexel.model.dao.WorkSheetDao#fetchAllWorkSheets()
	 */
	@SuppressWarnings("unchecked")
	public List<WorkSheet> fetchAllWorkSheets() {
		return (List<WorkSheet>) getEm().createQuery("FROM WorkSheet")
				.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dbexel.model.dao.WorkSheetDao#fetchWorkSheet(java.lang.Long)
	 */
	public WorkSheet fetchWorkSheet(final Long in_workSheetId) {
		// WorkSheet l_ws =
		// getEntityManager().createQuery("from WorkSheet where ws_Id = ")
		WorkSheet l_ws = getEm().find(WorkSheet.class, in_workSheetId);
		return l_ws;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbexel.model.dao.WorkSheetDao#updateWorkSheet(dbexel.model.mapping.WorkSheet
	 * )
	 */
	public void updateWorkSheet(WorkSheet in_workSheet) {
		beginTransaction();
		persistAttributes(in_workSheet);
		persistEntries(in_workSheet);

		getEm().persist(in_workSheet);
		getEm().flush();
		commitTransaction();

	}

	/**
	 * This method persists the attributes that depend on this worksheet.
	 * 
	 * @param in_workSheet
	 */
	private void persistAttributes(WorkSheet in_workSheet) {
		for (Attribute lt_wsAttrib : in_workSheet.getAttributes()) {
			getEm().persist(lt_wsAttrib);
		}
	}

	/**
	 * This method persists the entries that depend on this worksheet
	 */
	private void persistEntries(WorkSheet in_workSheet) {
		for (Entry lt_wsEntry : in_workSheet.getEntries()) {
			getEm().persist(lt_wsEntry);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dbexel.model.dao.WorkSheetDao#createWorkSheet(dbexel.model.mapping.WorkSheet
	 * )
	 */
	public void createWorkSheet(WorkSheet in_workSheet) {
		beginTransaction();
		persistAttributes(in_workSheet);
		
		persistEntries(in_workSheet);
		
		in_workSheet.setWs_Id(null);
		getEm().persist(in_workSheet);
		getEm().flush();
		commitTransaction();
		
	}

	public void deleteWorkSheet(WorkSheet in_workSheet) {		

		// Remove entries
		WorkSheetEntryDao l_entryDao = new WorkSheetEntryDaoImpl();
		
		for (Iterator<Entry> lt_wsEntryIterator = in_workSheet
				.getEntries().iterator(); lt_wsEntryIterator.hasNext();) {
			Entry lt_entry = lt_wsEntryIterator.next();
			lt_wsEntryIterator.remove();

			l_entryDao.deleteWorkSheetEntry(lt_entry);
		}
					
		AttributeDao l_attributeDao = new AttributeDaoImpl();
		
		for (Iterator<Attribute> lt_attribIterator = in_workSheet
				.getAttributes().iterator(); lt_attribIterator.hasNext();) {
			Attribute lt_attribute = lt_attribIterator.next();
			lt_attribIterator.remove();
			
			lt_attribute.getWorkSheet().getAttributes().remove(lt_attribute);
			l_attributeDao.deleteAttribute(lt_attribute);
		}		
		
		beginTransaction();
		
		getEm().remove(in_workSheet);
		getEm().flush();
		commitTransaction();
	}

}
