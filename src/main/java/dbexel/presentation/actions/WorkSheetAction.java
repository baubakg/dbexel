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
package dbexel.presentation.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import dbexel.model.dao.WorkSheetDao;
import dbexel.model.dao.WorkSheetDaoImpl;
import dbexel.model.mapping.WorkSheet;

public class WorkSheetAction extends ActionSupport implements SessionAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5278778872437401023L;
	
	private List<WorkSheet> workSheetList = new ArrayList<WorkSheet>();
	private WorkSheetDao workSheetDao;
	private Long newWorkSheetId;
	private Map<String, Object> session;

	public String fetchWorkSheets() {
		if (session.containsKey(AwareKeys.New_WorkSheet_ID)) {
			setNewWorkSheetId((Long) session.get(AwareKeys.New_WorkSheet_ID));
		} 
		
		workSheetList = getWorkSheetDao().fetchAllWorkSheets();
		return ActionSupport.SUCCESS;
	}

	/**
	 * @param workSheetList
	 *            the workSheetList to set
	 */
	public void setWorkSheetList(List<WorkSheet> workSheetList) {
		this.workSheetList = workSheetList;
	}

	/**
	 * @return the workSheetList
	 */
	public List<WorkSheet> getWorkSheetList() {
		return workSheetList;
	}

	public void setWorkSheetDao(WorkSheetDao workSheetDao) {
		this.workSheetDao = workSheetDao;
	}

	public WorkSheetDao getWorkSheetDao() {
		if (workSheetDao==null) 
			 return new WorkSheetDaoImpl();
		return workSheetDao;
	}
	

	public Long getNewWorkSheetId() {
		return newWorkSheetId;
	}

	public void setNewWorkSheetId(Long newWorkSheetId) {
		this.newWorkSheetId = newWorkSheetId;
	}

	@Override
	public void setSession(Map<String, Object> in_session) {
		session = in_session;
		
	}
}
