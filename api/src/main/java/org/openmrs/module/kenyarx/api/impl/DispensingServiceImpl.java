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

package org.openmrs.module.kenyarx.api.impl;

import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Obs;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyarx.DispensedOrder;
import org.openmrs.module.kenyarx.KenyaRxConstants;
import org.openmrs.module.kenyarx.api.DispensingService;
import org.openmrs.module.kenyarx.api.db.DispensingDAO;

import java.util.List;

/**
 * Implementation of the dispensing service
 */
public class DispensingServiceImpl implements DispensingService {

	private DispensingDAO dispensingDAO;

	/**
	 * Sets the DAO
	 * @param dispensingDAO the DAO
	 */
	public void setDispensingDAO(DispensingDAO dispensingDAO) {
		this.dispensingDAO = dispensingDAO;
	}

	/**
	 * @see org.openmrs.module.kenyarx.api.DispensingService#getDispensedOrder(org.openmrs.DrugOrder)
	 */
	@Override
	public DispensedOrder getDispensedOrder(DrugOrder drugOrder) {
		Concept medDispConcept = Context.getConceptService().getConceptByUuid(KenyaRxConstants.DISPENSED_DRUG_CONCEPT_UUID);
		List<Obs> obs = dispensingDAO.getObsByOrderAndConcept(drugOrder, medDispConcept);

		// TODO convert obs into object

		return null;
	}

	/**
	 * @see DispensingService#saveDispensedOrder(org.openmrs.module.kenyarx.DispensedOrder)
	 * @param dispensedOrder the dispensed order
	 */
	@Override
	public void saveDispensedOrder(DispensedOrder dispensedOrder) {

		// TODO convert object into obs
	}
}