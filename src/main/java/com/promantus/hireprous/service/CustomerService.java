/**********************************************************************************************
 * Copyright 2021 Promantus Private Limited.
 * All rights reserved.
 **********************************************************************************************/
package com.promantus.hireprous.service;

import java.util.List;

import com.promantus.hireprous.dto.CustomerDto;

/**
 * @author Sihab.
 *
 */
public interface CustomerService {

	/**
	 * @param customerDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	CustomerDto addCustomer(final CustomerDto customerDto, final String lang) throws Exception;

	/**
	 * @param customerDto
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	CustomerDto updateCustomer(final CustomerDto customerDto, final String lang) throws Exception;

	/**
	 * @return
	 * @throws Exception
	 */
	List<CustomerDto> getAllCustomers() throws Exception;

	/**
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	CustomerDto getCustomerById(String customerId) throws Exception;

	/**
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	CustomerDto deleteCustomerById(String customerId) throws Exception;

	/**
	 * @param keyword
	 * @return
	 * @throws Exception
	 */
	List<CustomerDto> searchCustomer(String keyword) throws Exception;

	/**
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	String getCustomerNameById(long customerId) throws Exception;

	/**
	 * @param customerDto
	 * @return
	 * @throws Exception
	 */
	Boolean checkCustomer(final CustomerDto customerDto) throws Exception;

	/**
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	String getCustomerNameInDetailById(long customerId) throws Exception;
	
	byte[] downloadCustomerDetails(List<CustomerDto> customerDtoList, String lang) throws Exception;
	
	
	List<CustomerDto> searchCustomerForDownload(CustomerDto customerDto) throws Exception;



}
