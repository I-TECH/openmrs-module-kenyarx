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

package org.openmrs.module.kenyarx.connector.impl;

import org.openmrs.DrugOrder;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyarx.DispensedOrder;
import org.openmrs.module.kenyarx.api.DispensingService;
import org.openmrs.module.kenyarx.connector.PharmacyConnector;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Implementation of the prescription handler which converts all drug orders (prescriptions) to dispensed orders,
 * with the following assumptions:
 *  1. The prescription is fulfilled exactly as requested
 *  2. The prescription is fulfilled immediately
 */
@Component
public class StubDispensingSystem implements PharmacyConnector {

	/**
	 * @see org.openmrs.module.kenyarx.connector.PharmacyConnector#recordPrescription(org.openmrs.DrugOrder)
	 */
	@Override
	public void recordPrescription(DrugOrder order) {

		DispensedOrder dispensedOrder = new DispensedOrder();
		dispensedOrder.setOrder(order);
		dispensedOrder.setConcept(order.getConcept());
		dispensedOrder.setDose(order.getDose());
		dispensedOrder.setUnits(order.getUnits());
		dispensedOrder.setDispensedDate(new Date());

		Context.getService(DispensingService.class).saveDispensedOrder(dispensedOrder);
	}
}