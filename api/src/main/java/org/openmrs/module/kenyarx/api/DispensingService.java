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

package org.openmrs.module.kenyarx.api;

import org.openmrs.DrugOrder;
import org.openmrs.module.kenyarx.Dispensing;

import java.util.List;

/**
 * Interface for dispensing services
 */
public interface DispensingService {

	/**
	 * Gets the dispensings for the given drug order if there are any
	 * @param drugOrder the drug order
	 * @return the dispensings
	 */
	List<Dispensing> getDispensingsForOrder(DrugOrder drugOrder);

	/**
	 * Saves the given dispensing
	 * @param dispensing the dispensing
	 */
	void saveDispensing(Dispensing dispensing);
}