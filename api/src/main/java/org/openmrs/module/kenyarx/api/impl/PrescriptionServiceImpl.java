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

import org.openmrs.DrugOrder;
import org.openmrs.module.kenyarx.connector.PharmacyConnector;
import org.openmrs.module.kenyarx.api.PrescriptionService;
import org.openmrs.module.kenyarx.api.db.DispensingDAO;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of the prescription service
 */
public class PrescriptionServiceImpl implements PrescriptionService {

	private DispensingDAO prescriptionDAO;

	@Autowired
	private PharmacyConnector pharmacyConnector;

	/**
	 * Sets the DAO
	 * @param prescriptionDAO the DAO
	 */
	public void setPrescriptionDAO(DispensingDAO prescriptionDAO) {
		this.prescriptionDAO = prescriptionDAO;
	}

	/**
	 * @see PrescriptionService#recordOrder(org.openmrs.DrugOrder)
	 */
	@Override
	public void recordOrder(DrugOrder order) {
		// Notify the pharmacy
		pharmacyConnector.recordPrescription(order);
	}
}