/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.Vendor;

/**
 * @author Sihab.
 *
 */
public interface VendorRepository extends MongoRepository<Vendor, String> {

	/**
	 * @param id
	 * @return
	 */
	Vendor findById(long id);

	/**
	 * @param parseLong
	 */
	void deleteById(long parseLong);

	/**
	 * @param year
	 * @param orderByRunningNumberDesc
	 * @return
	 */
	Vendor findFirstByYear(int year, Sort orderByRunningNumberDesc);

	/**
	 * @param vendorId
	 * @return
	 */
	Vendor findByVendorId(String vendorId);

	/**
	 * @param isActive
	 * @param orderByUpdatedDateTimeDesc
	 * @return
	 */
	List<Vendor> findByActive(String isActive, Sort orderByUpdatedDateTimeDesc);

}
