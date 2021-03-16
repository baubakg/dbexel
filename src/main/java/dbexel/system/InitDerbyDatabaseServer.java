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
package dbexel.system;

import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import dbexel.model.dao.WorkSheetDao;
import dbexel.model.dao.WorkSheetDaoImpl;

public class InitDerbyDatabaseServer implements ServletContextListener {

	public void contextInitialized(ServletContextEvent in_sce) {
		System.out.println("Derby : Starting Embedded Network Server");
		System.setProperty("derby.drda.startNetworkServer","true");

		// Booting derby
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();			
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Derby : Embedded Network Server Started");
		
		System.out.println("Derby : Creating Meta Data");
		WorkSheetDao l_wsDao = new WorkSheetDaoImpl();
		l_wsDao.fetchAllWorkSheets().size();
		System.out.println("Derby : Meta Data Created");
	}

	public void contextDestroyed(ServletContextEvent in_sce) {
        boolean gotSQLExc = false;  
		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException e) {
		    gotSQLExc = true;  
        }  

        if (!gotSQLExc) {  
            System.out.println("Database did not shut down normally");  
        } else {  
            System.out.println("Database shut down normally");  
        } 
        
        System.out.println("Derby : Stopped Embedded Network server");  
		
	}

}
