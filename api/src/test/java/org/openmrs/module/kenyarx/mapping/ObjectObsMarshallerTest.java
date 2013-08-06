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

package org.openmrs.module.kenyarx.mapping;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyarx.Dispensing;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashSet;

/**
 * Tests for {@link ObjectObsMarshaller}
 */
public class ObjectObsMarshallerTest extends BaseModuleContextSensitiveTest {

	@Autowired
	private ObjectObsMarshaller objectObsMarshaller;

	@Before
	public void setup() throws Exception {
		executeDataSet("test-data.xml");
	}

	@Test
	public void getConceptForClass_shouldReturnConceptIfMappingExists() {
		Concept concept = objectObsMarshaller.getConceptForClass(Dispensing.class);
		Assert.assertEquals(new Integer(1442), concept.getId());
	}

	@Test(expected = RuntimeException.class)
	public void getConceptForClass_shouldThrowExceptionIfMappingDoesntExist() {
		objectObsMarshaller.getConceptForClass(String.class);
	}

	@Test
	public void marshal_shouldConvertToObs() throws Exception {
		Patient patient = Context.getPatientService().getPatient(7);
		Concept aspirin = Context.getConceptService().getConcept(71617);
		Date date = new Date();

		Dispensing dispensing = new Dispensing();
		dispensing.setDispensedConcept(aspirin);
		dispensing.setDispensedQuantity(123.0);
		dispensing.setDispensedUnits("ml");

		Obs obs = objectObsMarshaller.marshal(dispensing, null, date, patient);
		Assert.assertNotNull(obs);
		Assert.assertEquals(patient, obs.getPerson());
		Assert.assertEquals(date, obs.getObsDatetime());

		Obs drugObs = objectObsMarshaller.findMember(obs, Context.getConceptService().getConcept(1282));
		Assert.assertNotNull(drugObs);
		Assert.assertEquals(patient, drugObs.getPerson());
		Assert.assertEquals(date, drugObs.getObsDatetime());
		Assert.assertEquals(aspirin, drugObs.getValueCoded());

		Obs quantityObs = objectObsMarshaller.findMember(obs, Context.getConceptService().getConcept(1443));
		Assert.assertNotNull(quantityObs);
		Assert.assertEquals(patient, quantityObs.getPerson());
		Assert.assertEquals(date, quantityObs.getObsDatetime());
		Assert.assertEquals(new Double(123.0), quantityObs.getValueNumeric());

		Obs unitsObs = objectObsMarshaller.findMember(obs, Context.getConceptService().getConcept(1444));
		Assert.assertNotNull(unitsObs);
		Assert.assertEquals(patient, unitsObs.getPerson());
		Assert.assertEquals(date, unitsObs.getObsDatetime());
		Assert.assertEquals("ml", unitsObs.getValueText());
	}

	@Test(expected = RuntimeException.class)
	public void marshal_shouldThrowExceptionIfMappingDoesntExist() throws Exception {
		objectObsMarshaller.marshal(new HashSet<String>(), null, null, null);
	}

	@Test
	public void unmarshal_shouldConvertToObject() throws Exception {
		Patient patient = Context.getPatientService().getPatient(7);
		Concept aspirin = Context.getConceptService().getConcept(71617);
		Date date = new Date();

		Obs obs = new Obs();
		obs.setConcept(Context.getConceptService().getConcept(1442));
		obs.setPerson(patient);
		obs.setObsDatetime(date);

		Obs drugObs = new Obs();
		drugObs.setConcept(Context.getConceptService().getConcept(1282));
		drugObs.setValueCoded(aspirin);
		drugObs.setPerson(patient);
		drugObs.setObsDatetime(date);

		obs.addGroupMember(drugObs);

		Dispensing dispensing = objectObsMarshaller.unmarshal(obs, Dispensing.class);
		Assert.assertEquals(aspirin, dispensing.getDispensedConcept());
	}
}