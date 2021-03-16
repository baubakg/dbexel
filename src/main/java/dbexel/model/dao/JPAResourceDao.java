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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class JPAResourceDao {

	protected static final EntityManagerFactory emf;
	protected static EntityManager em;

	static {
		try {
			emf =
					Persistence
							.createEntityManagerFactory("DBExelPersistenceUnit");
			em = getEmf().createEntityManager();
			//BasicConfigurator.configure();
		} catch (Exception ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}	
	
	@SuppressWarnings("unused")
	private EntityManagerFactory getEntityManagerFactory() {
        return (EntityManagerFactory) emf;
    }
	
	@SuppressWarnings("unused")
	private EntityManager getEntityManager() {
		return (EntityManager) getEmf().createEntityManager();
	}
	
	/**
	 * @return the emf
	 */
	private static EntityManagerFactory getEmf() {
		return emf;
	}

	/**
	 * @return the em
	 */
	protected static EntityManager getEm() {
		return em;
	}

	protected static void setEm(EntityManager in_em) {
		em = in_em;
	}

	protected void beginTransaction() {
		getEm().getTransaction().begin();
	}
	
	protected void commitTransaction() {
		getEm().getTransaction().commit();
	}
	
	public void cleanup() {
		if (getEm().isOpen())
			getEm().close();
	}
	
	protected void flush() {
		getEm().flush();
	}
}
