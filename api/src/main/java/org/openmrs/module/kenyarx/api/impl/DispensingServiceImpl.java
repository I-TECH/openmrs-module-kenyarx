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
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyarx.Dispensing;
import org.openmrs.module.kenyarx.api.DispensingService;
import org.openmrs.module.kenyarx.api.db.DispensingDAO;
import org.openmrs.module.kenyarx.mapping.ObjectObsMarshaller;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the dispensing service
 */
public class DispensingServiceImpl implements DispensingService {

	@Autowired
	private ObjectObsMarshaller objectObsMarshaller;

	private DispensingDAO dispensingDAO;

	/**
	 * Sets the DAO
	 * @param dispensingDAO the DAO
	 */
	public void setDispensingDAO(DispensingDAO dispensingDAO) {
		this.dispensingDAO = dispensingDAO;
	}

	/**
	 * @see org.openmrs.module.kenyarx.api.DispensingService#getDispensingsForOrder(org.openmrs.DrugOrder)
	 */
	@Override
	public List<Dispensing> getDispensingsForOrder(DrugOrder drugOrder) throws APIException {
		Concept concept = objectObsMarshaller.getConceptForClass(Dispensing.class);
		List<Obs> obss = dispensingDAO.getObsByOrderAndConcept(drugOrder, concept);

		List<Dispensing> dispensings = new ArrayList<Dispensing>();
		for (Obs obs : obss) {
			try {
				Dispensing dispensing = objectObsMarshaller.unmarshal(obs, Dispensing.class);
				dispensing.setOrder(drugOrder);
				dispensing.setDispensedDate(obs.getObsDatetime());
				dispensings.add(dispensing);
			}
			catch (Exception ex) {
				throw new APIException(ex);
			}
		}
		return dispensings;
	}

	/**
	 * @see DispensingService#saveDispensing(org.openmrs.module.kenyarx.Dispensing)
	 * @param dispensing the dispensing
	 */
	@Override
	public void saveDispensing(Dispensing dispensing) {
		try {
			Patient patient = dispensing.getOrder().getPatient();
			Obs newObs = objectObsMarshaller.marshal(dispensing, null, dispensing.getDispensedDate(), patient);
			newObs.setOrder(dispensing.getOrder());

			Context.getObsService().saveObs(newObs, null);
		}
		catch (Exception ex) {
			throw new APIException(ex);
		}
	}
}