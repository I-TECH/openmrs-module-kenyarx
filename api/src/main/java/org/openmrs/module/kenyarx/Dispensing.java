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

package org.openmrs.module.kenyarx;

import org.openmrs.Concept;
import org.openmrs.DrugOrder;

import java.util.Date;

/**
 * Represents the dispensing of a single drug
 */
public class Dispensing {

	private DrugOrder order;

	private Concept dispensedConcept;

	private Double dispensedQuantity;

	private String dispensedUnits;

	private Date dispensedDate;

	public DrugOrder getOrder() {
		return order;
	}

	public void setOrder(DrugOrder order) {
		this.order = order;
	}

	public Concept getDispensedConcept() {
		return dispensedConcept;
	}

	public void setDispensedConcept(Concept dispensedConcept) {
		this.dispensedConcept = dispensedConcept;
	}

	public Double getDispensedQuantity() {
		return dispensedQuantity;
	}

	public void setDispensedQuantity(Double dispensedQuantity) {
		this.dispensedQuantity = dispensedQuantity;
	}

	public String getDispensedUnits() {
		return dispensedUnits;
	}

	public void setDispensedUnits(String dispensedUnits) {
		this.dispensedUnits = dispensedUnits;
	}

	public Date getDispensedDate() {
		return dispensedDate;
	}

	public void setDispensedDate(Date dispensedDate) {
		this.dispensedDate = dispensedDate;
	}
}