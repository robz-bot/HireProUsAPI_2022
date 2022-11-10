/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.promantus.hireprous.entity.Customer;

/**
 * @author Sihab.
 *
 */
public interface CustomerRepository extends MongoRepository<Customer, String> {

	/**
	 * @param customerName
	 * @return
	 */
	List<Customer> findByCustomerNameRegex(String customerName, Sort sort);

	/**
	 * @param id
	 * @return
	 */
	Customer findById(long id);

	/**
	 * @param customerName
	 * @return
	 */
	Customer getCustomerByCustomerName(String customerName);

	/**
	 * @param parseLong
	 */
	void deleteById(long parseLong);

	/**
	 * @param customerName
	 * @param location
	 * @param region
	 * @return
	 */
	Customer getCustomerByCustomerNameAndLocationAndRegion(String customerName, String location, String region);

	/**
	 * @param customerName
	 * @param location
	 * @param region
	 * @return
	 */
	Customer getCustomerByCustomerNameIgnoreCaseAndLocationIgnoreCaseAndRegionIgnoreCase(String customerName,
			String location, String region);

}
