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

import dbexel.model.mapping.Entry;

/**
 * @author Baubak
 *
 */
public interface WorkSheetEntryDao {

	/**
	 * This method creates an entry for the Entry
	 * @param l_wsEntry
	 */
	void createWorkSheetEntry(Entry in_wsEntry);

	/**
	 * This method retrieves an entry from the database given its Id
	 * @param entryId
	 * @return {@link Entry}
	 */
	Entry fetchWorkSheetEntry(Long entryId);

	/**
	 * This method updates the worksheet entry
	 * @param in_wsEntry
	 */
	void updateWorkSheet(Entry in_wsEntry);

	/**
	 * This method deletes the entry from the system
	 * @param wsEntry
	 */
	void deleteWorkSheetEntry(Entry wsEntry);

}
