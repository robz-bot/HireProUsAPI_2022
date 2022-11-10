/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.BusinessUnit;

/**
 * @author Sihab.
 *
 */
public interface BusinessUnitRepository extends MongoRepository<BusinessUnit, String> {

	/**
	 * @param businessUnitName
	 * @return
	 */
	List<BusinessUnit> findByBusinessUnitNameRegex(String businessUnitName, Sort sort);

	/**
	 * @param businessUnitId
	 * @return
	 */
	BusinessUnit findById(long businessUnitId);

	/**
	 * @param businessUnitName
	 * @return
	 */
	BusinessUnit getBusinessUnitByBusinessUnitName(String businessUnitName);

	/**
	 * @param parseLong
	 */
	void deleteById(long parseLong);

	/**
	 * @param buShortName
	 * @return
	 */
	BusinessUnit getBusinessUnitByBuShortName(String buShortName);

	/**
	 * @param string
	 * @return
	 */
	BusinessUnit getBusinessUnitByBusinessUnitNameRegex(String string);

	/**
	 * @param buName
	 * @param managerId 
	 * @return
	 */
//	Object getBusinessUnitByBusinessUnitNameIgnoreCase(String buName);

	Object getBusinessUnitByBusinessUnitNameAndManagerIdIgnoreCase(String buName, long managerId);

	Object getBusinessUnitByBusinessUnitNameAndManagerId(String buName, long managerId);
}