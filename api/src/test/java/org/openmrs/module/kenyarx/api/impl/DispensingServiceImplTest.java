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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyarx.Dispensing;
import org.openmrs.module.kenyarx.api.DispensingService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Tests for {@link DispensingServiceImpl}
 */
public class DispensingServiceImplTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private DispensingService dispensingService;

	@Before
	public void setup() throws Exception {
		executeDataSet("test-data.xml");
	}

	@Test
	public void getDispensingsForOrder_shouldReturnAllDispensingsForThatOrder() {
		Patient patient = Context.getPatientService().getPatient(7);
		Concept aspirin = Context.getConceptService().getConcept(71617);

		DrugOrder order = new DrugOrder();
		order.setOrderType(Context.getOrderService().getOrderType(2));
		order.setOrderer(Context.getUserService().getUser(1));
		order.setPatient(patient);
		order.setConcept(aspirin);
		order.setStartDate(new Date());
		Context.getOrderService().saveOrder(order);

		Assert.assertEquals(0, dispensingService.getDispensingsForOrder(order).size());
	}

	@Test
	public void saveDispensing_shouldSaveDispensing() {
		Patient patient = Context.getPatientService().getPatient(7);
		Concept aspirin = Context.getConceptService().getConcept(71617);

		DrugOrder order = new DrugOrder();
		order.setOrderType(Context.getOrderService().getOrderType(2));
		order.setOrderer(Context.getUserService().getUser(1));
		order.setPatient(patient);
		order.setConcept(aspirin);
		order.setStartDate(new Date());
		Context.getOrderService().saveOrder(order);

		Date dispensedDate = new Date();

		Dispensing dispensing = new Dispensing();
		dispensing.setOrder(order);
		dispensing.setDispensedConcept(aspirin);
		dispensing.setDispensedQuantity(123.0);
		dispensing.setDispensedUnits("ml");
		dispensing.setDispensedDate(dispensedDate);

		dispensingService.saveDispensing(dispensing);

		List<Obs> medOrderObss = Context.getObsService().getObservationsByPersonAndConcept(patient, Context.getConceptService().getConcept(1442));
		Assert.assertEquals(1, medOrderObss.size());

		Obs medOrderObs = medOrderObss.get(0);
		Assert.assertEquals(patient, medOrderObs.getPerson());
		Assert.assertEquals(dispensedDate, medOrderObs.getObsDatetime());
	}
}