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
import org.junit.Assert;

import dbexel.model.mapping.DBEXEL_TABLES;

/**
 * @author gandomi
 *
 */
public class JPATestTools extends JPAResourceDao {

	/**
	 * Returns the result of a select statement
	 * @param in_table
	 * @return
	 */
	public static List<Object> fetchTableData(DBEXEL_TABLES in_table) {
		return fetchTableData(in_table.toString());
	}
	
	
	/**
	 * Returns the result of a select statement and a given value
	 * @param in_table
	 * @param in_cols
	 * @return 
	 */
	public static String fetchTableData(String in_cols, String in_criteria) {
		getEm().clear();
		
		String x = getEm().createNativeQuery("SELECT "+in_cols+" FROM "+in_criteria ).getSingleResult().toString(); 
		return x; 
	}
	
	/**
	 * Returns the result of a select statement
	 * @param in_table
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<Object> fetchTableData(String in_criteria) {
		
		return getEm().createNativeQuery("SELECT * FROM "+in_criteria ).getResultList();
	}
	
	
	/**
	 * Returns the number of rows in the table
	 * @param in_table
	 * @return
	 */
	public static int fetchTableSize(DBEXEL_TABLES in_table) {
		return fetchTableData(in_table).size();
		
	}
	
	/**
	 * Returns the number of rows in the table with a where clause
	 * @param in_table
	 * @return
	 */
	public static int fetchTableSize(DBEXEL_TABLES in_table,String in_criteria) {
		return fetchTableData(in_table.toString() +" "+in_criteria).size();
		
	}


	/**
	 * Given a table and the expected number of rows we see if we have the expected number of rows 
	 * @param in_expectedRows
	 * @param in_table
	 */
	public static void assertRowNumbers(int in_expectedRows, DBEXEL_TABLES in_table) {
		Assert.assertEquals("Table row missmatch for table "+in_table.toString(), in_expectedRows, fetchTableSize(in_table));
	}

	
	/**
	 * This method cleans all data in all of the tables 
	 */
	public static void cleanAllData() {
		getEm().getTransaction().begin();
		getEm().flush();
		getEm().createNativeQuery("DELETE FROM "+DBEXEL_TABLES.ENTRY_VALUES.name()).executeUpdate();
		getEm().flush();
		getEm().createNativeQuery("DELETE FROM "+DBEXEL_TABLES.ATTRIBUTEVALUE.name()).executeUpdate();
		
		getEm().createNativeQuery("DELETE FROM "+DBEXEL_TABLES.WORKSHEET_ENTRIES.name()).executeUpdate();
		getEm().flush();
		getEm().createNativeQuery("DELETE FROM "+DBEXEL_TABLES.ENTRY.name()).executeUpdate();
		getEm().flush();
		getEm().createNativeQuery("DELETE FROM "+DBEXEL_TABLES.WORKSHEET_ATTRIBUTES.name()).executeUpdate();
		getEm().flush();
		getEm().createNativeQuery("DELETE FROM "+DBEXEL_TABLES.ATTRIBUTE.name()).executeUpdate();
		getEm().flush();
		getEm().createNativeQuery("DELETE FROM "+DBEXEL_TABLES.WORKSHEET.name()).executeUpdate();
		
		getEm().getTransaction().commit();
	}
	
	/**
	 * Prints the rowcounts of all of the tables
	 */
	public static void printRowCounts(String in_Title) {
		System.out.println(in_Title +" : starting....");
		for (DBEXEL_TABLES lt_table:DBEXEL_TABLES.values()) {
			System.out.println("Row count for the table "+lt_table.name()+": "+fetchTableSize(lt_table));
		}
		System.out.println(in_Title +" : finished!");
	}

	
	/**
	 * A method that checks if the system is correctly empty
	 */
	public void assertSystemEmpty() {
		for (DBEXEL_TABLES lt_table : DBEXEL_TABLES.values()) {
			assertRowNumbers(0, lt_table);
		}
	}
	
}
