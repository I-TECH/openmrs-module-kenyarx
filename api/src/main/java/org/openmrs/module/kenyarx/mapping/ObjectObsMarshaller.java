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

import org.apache.commons.beanutils.PropertyUtils;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Handles marshalling between objects and obs
 */
@Component
public class ObjectObsMarshaller {

	@Autowired(required = false)
	private Set<ClassConceptMapping> mappings;

	/**
	 * Gets the concept mapped to the given class
	 * @param clazz the class
	 * @return the concept
	 */
	public Concept getConceptForClass(Class<?> clazz) {
		ClassConceptMapping mapping = getMapping(clazz);
		return getConcept(mapping.getConcept());
	}

	/**
	 * Marshals the given object into an obs
	 * @param object the object
	 * @param concept the concept (may be null of object's class is explicitly mapped)
	 * @param <T> the class of the object
	 * @return the obs
	 */
	public <T> Obs marshal(T object, Concept concept, Date obsDatetime, Person person) throws Exception {
		Class<T> clazz = (Class<T>) object.getClass();

		Obs obs = new Obs();
		obs.setConcept(concept);
		obs.setPerson(person);
		obs.setObsDatetime(obsDatetime);

		// Handle simple data types first
		if (clazz.equals(String.class)) {
			obs.setValueText((String) object);
		} else if (clazz.equals(Double.class)) {
			obs.setValueNumeric((Double) object);
		} else if (clazz.equals(Date.class)) {
			obs.setValueDatetime((Date) object);
		} else if (clazz.equals(Concept.class)) {
			obs.setValueCoded((Concept) object);
		}
		else {
			ClassConceptMapping mapping = getMapping(clazz);
			obs.setConcept(getConcept(mapping.getConcept()));

			for (Map.Entry<String, String> memberMapping : mapping.getMemberMappings().entrySet()) {
				String propertyName = memberMapping.getKey();
				Concept memberConcept = getConcept(memberMapping.getValue());

				Object propertyValue = PropertyUtils.getProperty(object, propertyName);

				Obs memberObs = marshal(propertyValue, memberConcept, obsDatetime, person);
				memberObs.setObsGroup(obs);

				obs.addGroupMember(memberObs);
			}
		}

		return obs;
	}

	/**
	 * Un-marshals the given obs into a new object instance
	 * @param obs the obs
	 * @param clazz the class of the object
	 * @param <T> the class of the object
	 * @return the new object instance
	 */
	public <T> T unmarshal(Obs obs, Class<T> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
		// Handle simple data types first
		if (clazz.equals(String.class)) {
			return (T) obs.getValueText();
		} else if (clazz.equals(Double.class)) {
			return (T) obs.getValueNumeric();
		} else if (clazz.equals(Date.class)) {
			return (T) obs.getValueDatetime();
		} else if (clazz.equals(Concept.class)) {
			return (T) obs.getValueCoded();
		}

		T object = clazz.newInstance();

		ClassConceptMapping mapping = getMapping(clazz);

		for (Map.Entry<String, String> memberMapping : mapping.getMemberMappings().entrySet()) {
			String propertyName = memberMapping.getKey();
			Concept memberConcept = getConcept(memberMapping.getValue());

			Obs propertyObs = findMember(obs, memberConcept);

			if (propertyObs != null) {
				Class propertyType = PropertyUtils.getPropertyType(object, propertyName);

				Object propertyValue = unmarshal(propertyObs, propertyType);

				PropertyUtils.setProperty(object, propertyName, propertyValue);
			}
		}

		return object;
	}

	/**
	 * Finds the mapping for the specified class
	 * @param clazz the class
	 * @return the mapping
	 */
	private ClassConceptMapping getMapping(Class<?> clazz) {
		if (mappings != null) {
			for (ClassConceptMapping mapping : mappings) {
				if (mapping.getTargetClass().equals(clazz)) {
					return mapping;
				}
			}
		}
		throw new RuntimeException("No mapping found for " + clazz.getCanonicalName());
	}

	/**
	 * Finds the member of an obs group with the given concept
	 * @param obsGroup the obs group
	 * @param concept the concept
	 * @return
	 */
	protected Obs findMember(Obs obsGroup, Concept concept) {
		for (Obs candidate : obsGroup.getGroupMembers(false)) {
			if (candidate.getConcept().equals(concept)) {
				return candidate;
			}
		}
		return null;
	}

	/**
	 * Gets a concept
	 * @param identifier the identifier (always UUIDs for now)
	 * @return the concept
	 */
	private Concept getConcept(String identifier) {
		Concept concept = Context.getConceptService().getConceptByUuid(identifier);
		if (concept == null) {
			throw new IllegalArgumentException("No such concept with identifier " + identifier);
		}

		return concept;
	}
}
