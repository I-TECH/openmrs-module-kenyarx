/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.kenyarx.api.db.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.module.kenyarx.api.db.DispensingDAO;

import java.util.List;

/**
 * Hibernate implementation of the prescription DAO
 */
public class HibernateDispensingDAO implements DispensingDAO {

	private SessionFactory sessionFactory;

	/**
	 * Sets the session factory
	 * @param sessionFactory the session factory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Convenience method to get current session
	 * @return the session
	 */
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * @see org.openmrs.module.kenyarx.api.db.DispensingDAO#getObsByOrderAndConcept(org.openmrs.Order, org.openmrs.Concept)
	 */
	@Override
	public List<Obs> getObsByOrderAndConcept(Order order, Concept concept) {
		return getCurrentSession().createCriteria(Obs.class)
				.add(Restrictions.eq("order", order))
				.add(Restrictions.eq("concept", concept))
				.add(Restrictions.eq("voided", false))
				.list();
	}
}