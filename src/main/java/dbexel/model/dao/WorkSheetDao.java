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

import java.util.List;

import dbexel.model.mapping.WorkSheet;

/**
 * @author Baubak
 * 
 */
public interface WorkSheetDao {

	/**
	 * This method fetches all the worksheets in the database
	 * 
	 * @return List of WorkSheets
	 */
	public List<WorkSheet> fetchAllWorkSheets();

	/**
	 * This method fetches a worksheet given its ID
	 * 
	 * @param in_workSheetId
	 * @return a WorkSheet
	 */
	public WorkSheet fetchWorkSheet(Long in_workSheetId);

	/**
	 * This method updates the worksheet in the context
	 * @param in_workSheet
	 */
	public void updateWorkSheet(WorkSheet in_workSheet);

	/**
	 * This method creates the worksheet in the context
	 * @param in_workSheet
	 */
	public void createWorkSheet(WorkSheet in_workSheet);
	
	/**
	 * This method deletes the given WorkSheet
	 * @param in_workSheet
	 */
	public void deleteWorkSheet(WorkSheet in_workSheet);

}
