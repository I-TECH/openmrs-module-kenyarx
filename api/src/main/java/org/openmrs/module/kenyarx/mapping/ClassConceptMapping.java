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

import java.util.Map;

/**
 * Represents a mapping between a class and concepts
 */
public class ClassConceptMapping {

	private Class<?> targetClass;

	private String concept;

	private Map<String, String> memberMappings;

	/**
	 * Gets the target class
	 * @return the target class
	 */
	public Class<?> getTargetClass() {
		return targetClass;
	}

	/**
	 * Sets the target class
	 * @param targetClass the target class
	 */
	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}

	/**
	 * Gets the concept
	 * @return the concept
	 */
	public String getConcept() {
		return concept;
	}

	/**
	 * Sets the concept
	 * @param concept the concept
	 */
	public void setConcept(String concept) {
		this.concept = concept;
	}

	/**
	 * Gets the member mappings
	 * @return the member mappings
	 */
	public Map<String, String> getMemberMappings() {
		return memberMappings;
	}

	/**
	 * Sets the member mappings
	 * @param memberMappings the member mappings
	 */
	public void setMemberMappings(Map<String, String> memberMappings) {
		this.memberMappings = memberMappings;
	}
}